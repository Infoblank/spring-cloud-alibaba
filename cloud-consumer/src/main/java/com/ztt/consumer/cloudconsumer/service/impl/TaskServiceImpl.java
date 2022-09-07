package com.ztt.consumer.cloudconsumer.service.impl;

import com.ztt.common.aop.ClearId;
import com.ztt.consumer.cloudconsumer.service.ConsumerService;
import com.ztt.consumer.cloudconsumer.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ConsumerService consumerService;

    @Override
    @Async
    @ClearId
    public void task() {
        try {
            Thread.sleep(0);
            consumerService.test();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
