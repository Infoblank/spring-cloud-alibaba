package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.common.entity.CommonUser;
import com.ztt.consumer.cloudconsumer.interfaceprovider.ProviderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
        log.info("服务消费者方法这些了");
        long start = System.currentTimeMillis();
        //使用Feign客户端调用其他服务的接口
        String list = providerClient.list();
        long end = System.currentTimeMillis();
        return "响应结果：" + list + ",耗时:" + (end - start) / 1000 + "秒";
    }

    @RequestMapping(path = "hello", name = "返回服务端的端口")
    public String hello() {
        return this.providerClient.hello();
    }

    @RequestMapping(path = "hello2/{name}", name = "传递参数到服务端,参数直接在path里面")
    public String hello2(@PathVariable("name") String name) {
        return this.providerClient.hello2(name);
    }

    @RequestMapping(path = "hello5/{name}/{password}", name = "传递参数到服务端,双参数path里面")
    public Map<Object, Object> hello5(@PathVariable("name") String name, @PathVariable("password") String password) {
        log.info("调用hello5,参数:{}", name + password);
        return this.providerClient.hello5(name, password);
    }

    @RequestMapping(path = "hello3", name = "传递参数到服务端,参数包括(对象和字符串)")
    public String hello3() {
        CommonUser user = new CommonUser();
        user.setName("ztt");
        user.setPassword("123456");
        String name = "hello3";
        return this.providerClient.hello3(user, name);
    }

    @RequestMapping(path = "hello4", name = "传递参数到服务端,参数包括(对象和字符串)")
    public String hello4(@RequestBody CommonUser user) {
        log.info("调用hello4,参数:{}", user.toString());
        return this.providerClient.hello3(user, null);
    }
}
