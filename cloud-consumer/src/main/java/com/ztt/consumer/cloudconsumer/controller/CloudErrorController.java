package com.ztt.consumer.cloudconsumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping
@RestController
public class CloudErrorController {

    @RequestMapping("favicon.ico")
    public void error() {
    }
}
