package com.ztt.consumer.cloudconsumer.service.impl;

import com.ztt.common.constant.CommonConstant;
import com.ztt.common.util.RequestIdUtils;
import com.ztt.consumer.cloudconsumer.service.ConsumerService;
import com.ztt.consumer.cloudconsumer.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
    public void task() {
        try {
            Thread.sleep(0);
            String requestId = RequestIdUtils.getRequestId();
            MDC.put(CommonConstant.REQUEST_ID, requestId);
            Thread thread = Thread.currentThread();
            String name = thread.getName();
            log.info("子线程:{}获取到父线程的REQUEST_ID:{}", name, requestId);
            consumerService.test();
            log.info("TaskServiceImpl()执行了");
            MDC.clear();
            RequestIdUtils.removeRequestId();
            log.info("子线程:{}清除来自父线程的requestId:{}", name, requestId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
