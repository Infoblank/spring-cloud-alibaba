package com.ztt.cloudprovider;

import cn.hutool.core.codec.Base62;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.StrPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

//@SpringBootTest
@Slf4j
class CloudProviderApplicationTests {

    private static final ThreadLocal<SimpleDateFormat> formatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("mm:ss");

    @Test
    void contextLoads() {
        test();
    }

    void test() {
        String name = Base62.encode(FileNameUtil.getPrefix("/user/张.jpg")) + StrPool.DOT + FileNameUtil.extName("/user/张.jpg");
        String name2 = name.contains(StrPool.DOT) ? Base62.decodeStr(FileNameUtil.getPrefix(name)) + StrPool.DOT + FileNameUtil.extName(name) : Base62.decodeStr(name);
        System.out.println("name = " + name);
        System.out.println("name2 = " + name2);
    }


    @Test
    public void context() {
        Predicate<Integer> predicate = x -> x > 5;
        boolean test = predicate.test(7);

        Predicate<Integer> predicate1 = x -> x > 7;
        Predicate<Integer> and = predicate.and(predicate1);
        boolean test1 = and.test(7);
        System.out.println("test1 = " + test1);
        System.out.println("test = " + test);
    }

    @Test
    public void __c1() throws InterruptedException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        // 创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        // 执行 10 次时间格式化
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            // 线程池执行任务
            threadPool.execute(() -> {
                // 创建时间对象
                Date date = new Date(finalI * 1000);
                // 格式化时间
                //String result = formatThreadLocal.get().format(date);
                String format = simpleDateFormat.format(date);
                // 打印结果
                log.info("第:{}次执行结果:{}", finalI, format);
            });
        }
        // 任务执行完之后关闭线程池
        threadPool.shutdown();
    }

    public static void main(String[] args) {
        // 创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        // 执行 10 次时间格式化
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            // 线程池执行任务
            threadPool.execute(() -> {
                // 创建时间对象
                Date date = new Date(finalI * 1000);
                // 格式化时间
                //String result = formatThreadLocal.get().format(date);
                LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                String format = dateTimeFormatter.format(localDateTime);
                // 打印结果
                log.info("第:{}次执行结果:{}", finalI, format);
            });
        }
        // 任务执行完之后关闭线程池
        threadPool.shutdown();
    }


    // 测试清除内容
    @Test
    public void __c2() {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            hashMap.put(String.valueOf(i), new ArrayList<>());
        }
        for (int i = 0; i < 2; i++) {
            hashMap.get(String.valueOf(i)).add(String.valueOf(i));
        }
        log.info("清除前第一个list{}", hashMap.get("0").toArray());
        log.info("清除前第二个list{}", hashMap.get("1").toArray());

        for (int i = 0; i < 2; i++) {
            hashMap.get(String.valueOf(i)).clear();
        }

        log.info("清除后第一个list{}", hashMap.get("0").toArray());
        log.info("清除后第二个list{}", hashMap.get("1").toArray());

    }


}




