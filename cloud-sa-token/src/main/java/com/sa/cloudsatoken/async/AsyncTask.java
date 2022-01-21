package com.sa.cloudsatoken.async;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncTask {
    @SneakyThrows
    @Async
    public void doTask() {
        long t1 = System.currentTimeMillis();
        Thread.sleep(2000);
        long t2 = System.currentTimeMillis();
        log.info("doTask cost {} ms", t2 - t1);
    }
}
