package com.ztt.common.cloudcommon;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

//@SpringBootTest

@Slf4j
class CloudCommonApplicationTests {

	public static int initCapacity = 500;

	public static int bufferInitCapacity = 1024;

	/**
	 * HeapByteBuffer所创建的字节缓冲区就是在JVM堆中的，即JVM内部所维护的字节数组。而DirectByteBuffer是直接操作操作系统本地代码创建的内存缓冲数组。
	 * DirectByteBuffer的使用场景：
	 * java程序与本地磁盘、socket传输数据
	 * 大文件对象，可以使用。不会受到堆内存大小的限制。
	 * 不需要频繁创建，生命周期较长的情况，能重复使用的情况。
	 * HeapByteBuffer的使用场景：
	 * 除了以上的场景外，其他情况还是建议使用HeapByteBuffer，没有达到一定的量级，实际上使用DirectByteBuffer是体现不出优势的。
	 *
	 * rewind() 读完的数据再重新读一遍
	 */
	@Test
	void contextLoads() {
		String mes = "NIO从入门到踹门！";
		// 创建一个固定大小的buffer(返回的是HeapByteBuffer)
		ByteBuffer allocate = ByteBuffer.allocate(bufferInitCapacity);
		byte[] mesBytes = mes.getBytes();
		// 将数据放到ByteBuffer
		allocate.put(mesBytes);
		// 切换为读模式 Buffer.clear()[清空]或者Buffer.compact()[压缩]可以变回写模式
		allocate.flip();
		byte[] tempBytes = new byte[mesBytes.length];
		int i = 0;
		while (allocate.hasRemaining()) {
			tempBytes[i++] = allocate.get();
		}
		log.info("{}", new String(tempBytes));
	}

	/**
	 * FileChannel，读写文件中的数据。
	 * SocketChannel，通过TCP读写网络中的数据。
	 * ServerSockectChannel，监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
	 * DatagramChannel，通过UDP读写网络中的数据
	 * <p>
	 * Channel本身并不存储数据，只是负责数据的运输。必须要和Buffer一起使用
	 */
	@Test
	void fileChannel() {
		File file = new File("/Users/zhangtingting/Desktop/未命名.txt");
		try (FileInputStream fileInputStream = new FileInputStream(file); FileChannel channel = fileInputStream.getChannel(); FileOutputStream outputStream = new FileOutputStream("2.txt"); FileChannel outputStreamChannel = outputStream.getChannel()) {
			long length = file.length();
			ByteBuffer byteBuffer;
			if (length > 1000) {
				// 文件超过大小使用 DirectByteBuffer
				byteBuffer = ByteBuffer.allocateDirect((int) file.length());
			} else {
				byteBuffer = ByteBuffer.allocate((int) file.length());
			}
			channel.read(byteBuffer);
			byteBuffer.flip();
			int write = outputStreamChannel.write(byteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 阻塞式 (要做到非阻塞还需要使用选择器Selector)
	 * 通过 telnet 127.0.0.1 6666 测试
	 *
	 * @throws IOException 异常
	 */

	@Test
	void socketChannel() throws IOException {
		ServerSocketChannel open = ServerSocketChannel.open();
		InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
		open.bind(address);
		ByteBuffer allocate = ByteBuffer.allocate(bufferInitCapacity);
		while (true) {
			SocketChannel accept = open.accept();
			while (accept.read(allocate) != -1) {
				System.out.println(new String(allocate.array()));
				allocate.clear();
			}
		}
	}

	/**
	 * 通道间的数据传输
	 * <p>
	 * transferTo:把源通道的数据传输到目的通道中
	 * transferFrom:把来自源通道的数据传输到目的通道
	 */
	@Test
	void transferToAndTransferFrom() {
		File file = new File("/Users/zhangtingting/Desktop/未命名.txt");
		try (FileInputStream fileInputStream = new FileInputStream(file); FileChannel channel = fileInputStream.getChannel(); FileOutputStream outputStream = new FileOutputStream("4.txt"); FileChannel outputStreamChannel = outputStream.getChannel()) {
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
			//
			channel.transferTo(0, byteBuffer.limit(), outputStreamChannel);
			//
			outputStreamChannel.transferFrom(channel, 0, byteBuffer.limit());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分散读取和聚合写入
	 */

	@Test
	void distributedReadsAndAggregatedWrites() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		File file = new File("/Users/zhangtingting/Desktop/未命名.txt");
		try (FileInputStream fileInputStream = new FileInputStream(file); FileChannel channel = fileInputStream.getChannel(); FileOutputStream outputStream = new FileOutputStream("5.txt"); FileChannel outputStreamChannel = outputStream.getChannel()) {
			//创建三个缓冲区，分别都是50
			ByteBuffer one = ByteBuffer.allocate(initCapacity);
			ByteBuffer two = ByteBuffer.allocate(initCapacity);
			ByteBuffer three = ByteBuffer.allocate(initCapacity);
			ByteBuffer[] byteBuffers = new ByteBuffer[]{one, two, three};
			long read;
			long sumLength = 0;
			int readSum = 0;
			while ((read = channel.read(byteBuffers)) != -1) {
				sumLength += read;
				Arrays.stream(byteBuffers).map(buffer -> "position=" + buffer.position() + ",limit=" + buffer.limit()).forEach(log::info);

				//切换模式
				Arrays.stream(byteBuffers).forEach(Buffer::flip);
				//聚合写入到文件输出通道
				outputStreamChannel.write(byteBuffers);
				//清空缓冲区
				Arrays.stream(byteBuffers).forEach(Buffer::clear);
				readSum++;
			}
			log.info("总长度:{}", sumLength);
			log.info("读取次数:{}", readSum);
			stopWatch.stop();
			log.info("执行时间:{}毫秒...", readSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void selectorServer() {
		try (//打开一个ServerSocketChannel
		     ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		     //打开一个选择器
		     Selector selector = Selector.open();) {
			InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
			//绑定地址
			serverSocketChannel.bind(address);
			//设置为非阻塞
			serverSocketChannel.configureBlocking(false);
			//serverSocketChannel注册到选择器中,监听连接事件
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			//循环等待客户端的
			while (true) {
				//等待3秒，（返回0相当于没有事件）如果没有事件，则跳过
				if (selector.select(3000) == 0) {
					log.info("服务器等待3秒，没有连接");
					continue;
				}
				//如果有事件selector.select(3000)>0的情况,获取事件
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				//获取迭代器遍历
				Iterator<SelectionKey> it = selectionKeys.iterator();
				while (it.hasNext()) {
					//获取到事件
					SelectionKey selectionKey = it.next();
					//判断如果是连接事件
					if (selectionKey.isAcceptable()) {
						//服务器与客户端建立连接，获取socketChannel
						SocketChannel socketChannel = serverSocketChannel.accept();
						//设置成非阻塞
						socketChannel.configureBlocking(false);
						//把socketChannel注册到selector中，监听读事件，并绑定一个缓冲区
						socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(bufferInitCapacity));
					}
					//如果是读事件
					if (selectionKey.isReadable()) {
						//获取通道
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						//获取关联的ByteBuffer
						ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
						//打印从客户端获取到的数据
						socketChannel.read(buffer);
						System.out.println("from 客户端：" + new String(buffer.array()));
					}
					//从事件集合中删除已处理的事件，防止重复处理
					it.remove();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	void selectorClient() throws IOException {
		try (SocketChannel socketChannel = SocketChannel.open()) {
			InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
			socketChannel.configureBlocking(false);
			//连接服务器
			boolean connect = socketChannel.connect(address);
			//判断是否连接成功
			if (!connect) {
				//等待连接的过程中
				while (!socketChannel.finishConnect()) {
					log.info("连接服务器需要时间，期间可以做其他事情...");
				}
			}
			String msg = "hello java技术爱好者！";
			ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
			//把byteBuffer数据写入到通道中
			socketChannel.write(byteBuffer);
		}
		//让程序卡在这个位置，不关闭连接
		int read = System.in.read();
	}
}
