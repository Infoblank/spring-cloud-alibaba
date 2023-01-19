package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.common.util.SpringApplicationContextHolder;
import com.ztt.consumer.cloudconsumer.interfaceprovider.ProviderClient;
import com.ztt.entity.CommonUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZTT
 */
@RestController
@Slf4j
@RequestMapping(path = "/consumer")
public class ConsumerController {


    @Resource
    private ProviderClient providerClient;


    @RequestMapping(path = "/callProvider", name = "返回列表,得到响应的时间。")
    public String callProvider() {
        long start = System.currentTimeMillis();
        //使用Feign客户端调用其他服务的接口
        String list = providerClient.list();
        long end = System.currentTimeMillis();
        return "响应结果：" + list + ",耗时:" + (end - start) + "ms";
    }

    @RequestMapping("/callProvider2")
    public List<String> callProvider2() {
        log.info("服务消费者方法这些了");
        ArrayList<String> objects = new ArrayList<>();
        objects.add("callProvider2");
        objects.add("callProvider0");
        objects.add("callProvider1");
        String property = SpringApplicationContextHolder.getBean(Environment.class).getProperty("file.path");
        return objects;
    }


    @RequestMapping(path = "hello", name = "返回服务端的端口")
    public String hello() {
        return this.providerClient.hello();
    }


    @RequestMapping(path = "hello2/{name}", name = "传递参数到服务端,参数直接在path里面")
    public String hello2(@PathVariable("name") String name) {
        return this.providerClient.hello2(name);
    }

    @PostMapping(path = "hello5/{name}/{password}", name = "传递参数到服务端,双参数path里面")
    public Map<Object, Object> hello5(@PathVariable("name") String name, @PathVariable("password") String password) {
        log.info("调用hello5,参数:{}", name + password);
        return this.providerClient.hello5(name, password);
    }

    @PostMapping(path = "hello3", name = "传递参数到服务端,参数包括(对象和字符串)")
    public Map<String, Object> hello3() {
        CommonUser user = new CommonUser();
        user.setName("ztt");
        user.setPassword("123456");
        String name = "hello3";
        return this.providerClient.hello3(user, name);
    }

    @PostMapping(path = "hello4", name = "传递参数到服务端,参数包括(对象和字符串)")
    public Map<String, Object> hello4(@RequestBody CommonUser user) {
        log.info("调用hello4,参数:{}", user.toString());
        return this.providerClient.hello3(user, null);
    }

    @PostMapping("sys/user")
    List<CommonUser> sysUserList() {
        return this.providerClient.sysUserList("zhangtt", "sabdjhk@!@#");
    }
}
