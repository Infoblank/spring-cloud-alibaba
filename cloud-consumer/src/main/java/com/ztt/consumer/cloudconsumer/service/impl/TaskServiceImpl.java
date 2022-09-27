package com.ztt.consumer.cloudconsumer.service.impl;

import com.ztt.consumer.cloudconsumer.interfaceprovider.ProviderClient;
import com.ztt.consumer.cloudconsumer.service.ConsumerService;
import com.ztt.consumer.cloudconsumer.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Resource
    private ConsumerService consumerService;

    @Resource
    private ProviderClient providerClient;

    @Override
    @Async
    //@ClearId
    public void task() {
        try {
            Thread.sleep(0);
            consumerService.test();
            providerClient.list();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String mvcHello() {
        return providerClient.hello();
    }
}
