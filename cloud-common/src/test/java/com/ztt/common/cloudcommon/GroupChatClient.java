package com.ztt.common.cloudcommon;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

@Slf4j
public class GroupChatClient {
	private Selector selector;

	private SocketChannel socketChannel;

	private String userName;

	public GroupChatClient() {
		try {
			//打开选择器
			this.selector = Selector.open();
			//连接服务器
			socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", GroupChatServer.PORT));
			//设置为非阻塞
			socketChannel.configureBlocking(false);
			//注册到选择器中
			socketChannel.register(selector, SelectionKey.OP_READ);
			//获取用户名
			userName = socketChannel.getLocalAddress().toString().substring(1);
			log.info("{}IS OK", userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//发送消息到服务端
	private void sendMsg(String msg) {
		msg = userName + "说：" + msg;
		StringBuilder builder = new StringBuilder(userName);
		builder.append("说:");
		builder.append(msg);
		try {
			socketChannel.write(ByteBuffer.wrap(builder.toString().getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//读取服务端发送过来的消息
	private void readMsg() {
		try {
			int count = selector.select();
			if (count > 0) {
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					//判断是读就绪事件
					if (selectionKey.isReadable()) {
						SocketChannel channel = (SocketChannel) selectionKey.channel();
						//创建一个缓冲区
						ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
						//从服务器的通道中读取数据到缓冲区
						channel.read(byteBuffer);
						//缓冲区的数据，转成字符串，并打印
						System.out.println(new String(byteBuffer.array()));
					}
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		GroupChatClient chatClinet = new GroupChatClient();
		//启动线程，读取服务器转发过来的消息
		new Thread(() -> {
			while (true) {
				chatClinet.readMsg();
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		//主线程发送消息到服务器
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String msg = scanner.nextLine();
			chatClinet.sendMsg(msg);
		}
	}
}
