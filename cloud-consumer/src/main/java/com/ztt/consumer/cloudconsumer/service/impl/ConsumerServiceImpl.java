package com.ztt.consumer.cloudconsumer.service.impl;

import com.ztt.consumer.cloudconsumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void test() {
        log.info("ConsumerServiceImpl.test()");
    }
}
