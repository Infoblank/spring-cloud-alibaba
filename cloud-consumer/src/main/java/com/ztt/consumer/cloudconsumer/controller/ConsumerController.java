package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.common.util.SpringApplicationContextHolder;
import com.ztt.consumer.cloudconsumer.interfaceprovider.ProviderClient;
import com.ztt.entity.CommonUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "ConsumerController", description = "用户信息接收器")
@ApiResponses(@ApiResponse(responseCode = "200", description = "接口信息返回成功"))
public class ConsumerController {


    @Resource
    private ProviderClient providerClient;


    @Operation(summary = "获取服务返回信息",description = "接口响应时间")
    @RequestMapping(path = "/callProvider", name = "返回列表,得到响应的时间。")
    public String callProvider() throws Exception {
        long start = System.nanoTime();
        //使用Feign客户端调用其他服务的接口
        String list = providerClient.list();
        long end = System.nanoTime();
        return "响应结果:" + list + ",耗时:" + (end - start) + "ms";
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
    public String hello() throws Exception {
        return this.providerClient.hello();
    }


    @RequestMapping(path = "hello2/{name}", name = "传递参数到服务端,参数直接在path里面")
    public String hello2(@PathVariable("name") String name) throws Exception {
        return this.providerClient.hello2(name);
    }

    @PostMapping(path = "hello5/{name}/{password}", name = "传递参数到服务端,双参数path里面")
    public Map<Object, Object> hello5(@PathVariable("name") String name, @PathVariable("password") String password) throws Exception {
        log.info("调用hello5,参数:{}", name + password);
        return this.providerClient.hello5(name, password);
    }

    @PostMapping(path = "hello3", name = "传递参数到服务端,参数包括(对象和字符串)")
    public Map<String, Object> hello3() throws Exception {
        CommonUser user = new CommonUser();
        user.setName("ztt");
        user.setPassword("123456");
        String name = "hello3";
        return this.providerClient.hello3(user, name);
    }

    /**
     * map接收参数的时候只能是Map而不能是HashMap或者其它的子类
     *
     * @param map 参数
     * @return 结果集
     */
    @PostMapping(path = "hello4", name = "传递参数到服务端,参数包括(对象和字符串)")
    public Map<String, Object> hello4(@RequestBody Map<String, Object> map) throws Exception {
        log.info("调用hello4,参数:{}", map.toString());
        return this.providerClient.hello3(null, null);
    }

    @PostMapping(path = "hello6", name = "传递参数到服务端")
    public Map<String, Object> hello5(@RequestBody CommonUser user) throws Exception {
        log.info("调用hello4,参数:{}", user.toString());
        return this.providerClient.hello3(user, null);
    }

    @PostMapping("sys/user")
    List<CommonUser> sysUserList() throws Exception {
        return this.providerClient.sysUserList("zhangtt", "sabdjhk@!@#");
    }
}
