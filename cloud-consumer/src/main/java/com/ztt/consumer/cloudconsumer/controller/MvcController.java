package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.consumer.cloudconsumer.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mvc")
@RestController
public class MvcController {


    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/test")
    public String mvcTest() {
        for (int i = 0; i < 2; i++) {
            taskService.task();
        }
        log.info("MvcController.mvcTest()");
        return "mvcTest";
    }
}
