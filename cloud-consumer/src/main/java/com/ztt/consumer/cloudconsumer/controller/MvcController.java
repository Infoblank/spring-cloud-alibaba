package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.common.config.otherconfig.OauthContext;
import com.ztt.consumer.cloudconsumer.interfaceprovider.GitHubFeign;
import com.ztt.consumer.cloudconsumer.service.TaskService;
import com.ztt.entity.LoginVal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RequestMapping("/consumer/mvc")
@RestController
public class MvcController {


    @Resource
    private TaskService taskService;


    @Resource
    private GitHubFeign gitHubFeign;


    @RequestMapping(value = "/test")
    public String mvcTest() {
        // 在多线程里面调用了feign的服务,无法回去到父请求对象,需要加上这一行代码 加在需要开始子线程任务开始的地方
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        LoginVal loginVal = new LoginVal();
        loginVal.setLoginKey("zhang");
        loginVal.setAppKey("key");
        loginVal.setLoginName("zhangTingTing");
        OauthContext.set(loginVal);
        for (int i = 0; i < 2; i++) {
            taskService.task();
        }
        log.info("MvcController.mvcTest()");
        return "mvcTest";
    }

    @PostMapping(value = "/hello")
    public String mvcHello() throws Exception {
        log.info("MvcController.mvcHello()");
        return taskService.mvcHello();
    }

    @GetMapping(path = "/git/{query}")
    public Map<String, Object> search(@PathVariable String query) {
        return gitHubFeign.searchRepositories(query);
    }

    @GetMapping(path = "/list")
    public void list() throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            strings.add("11111jsdnfkjsdnjk" + i);
        }
        String join = StringUtils.join(strings.toArray(), ",");
        log.info("{}", join);
        try (FileReader fileReader = new FileReader("");) {
            int read = fileReader.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/self")
    public String callSelf() {
        return "调用了Self方法...";
    }

}
