package com.ztt.consumer.cloudconsumer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

// @SpringBootTest
@Slf4j
class CloudConsumerApplicationTests {

    String[] citys = new String[]{"杭州", "台州", "宁波", "舟山", "温州", "绍兴", "湖州", "金华", "衢州", "丽水", "嘉兴"};

    // 源文件的目录
    String fileDir = "/Users/zhangtingting/Downloads/LTE/";
    // 生成文件的目录
    String inputFile = "/Users/zhangtingting/Downloads/ph/";


    int fileTotal = 0;

    @Test
    void context() {
        File file = new File(fileDir);
        String[] strings = file.list();
        long startTime = System.nanoTime();
        assert strings != null;
        File filea = new File(inputFile);
        if (!filea.exists()) {
            boolean mkdirs = filea.mkdirs();
            if (mkdirs) {
                log.info("目录:{}创建成功....", inputFile);
            }
        }
        File file_a;
        for (String city : citys) {
            String path = inputFile + city + "/";
            file_a = new File(path);
            if (!file_a.exists()) {
                boolean mkdirs = file_a.mkdirs();
                if (mkdirs) {
                    log.info("创建目录:{}", path);
                }
            }
        }

        for (String string : strings) {
            contextLoads(string);
        }
        Duration timeTakenToReady = Duration.ofNanos(System.nanoTime() - startTime);
        long seconds = timeTakenToReady.getSeconds();
        log.info("一共解析文件:{}个,耗时:{}s", fileTotal, seconds);
    }


    synchronized void contextLoads(String fileName) {
        try (FileReader fileReader = new FileReader(fileDir + fileName, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (count == 0) {
                    writeData(fileName, line, true);
                    count++;
                } else {
                    writeData(fileName, line, false);
                }
            }
            log.info("文件{}分解完毕", fileName);
            fileTotal++;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param fileName 文件名称
     * @param line     写入的数据
     * @param isFist   是否第一行
     */

    public void writeData(String fileName, String line, boolean isFist) {
        String filePath;
        for (String city : citys) {
            String fileSeparator = System.getProperty("file.separator");
            filePath = inputFile + fileSeparator + city + fileSeparator + city + "_" + fileName;
            try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)
            ) {
                // 写入对应地市的数据,地市不对应不需要写入到文件
                String lineSeparator = System.getProperty("line.separator");
                if (isFist) {
                    fileWriter.append(line).append(lineSeparator);
                    log.info("{}的第一列写完毕....", filePath);
                } else {
                    if (line.contains("|" + city + "|")) {
                        fileWriter.append(line).append(lineSeparator);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void __c() {
        int task = 10;
        ExecutorService service = Executors.newFixedThreadPool(task);
        List<Future<Object>> list = new ArrayList<>();
        for (int i = 0; i < task; i++) {
            MyCall call = new MyCall((new Random().nextInt(10) + 1) * 10, i);
            Future submit = service.submit(call);
            list.add(submit);
        }
        list.forEach(v -> {
            if (v.isDone()) {
                try {
                    log.info("完成" + v.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    log.info("未完成" + v.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @SuppressWarnings("rawtypes")
    static class MyCall implements Callable {
        private final int sleep;

        private final int index;

        public MyCall(int sleep, int index) {
            this.sleep = sleep;
            this.index = index;
        }

        @Override
        public Object call() {
            try {
                log.info("index:{},sleep:{}", index, sleep);
                if (this.sleep > 20) {
                    throw new RuntimeException("手动报错" + index);
                }
                Thread.sleep(this.sleep);
            } catch (Exception e) {
                //throw new RuntimeException(e);
                return e.getMessage();
            }
            return "" + index;
        }
    }
}
