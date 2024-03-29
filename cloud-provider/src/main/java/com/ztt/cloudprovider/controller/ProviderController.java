package com.ztt.cloudprovider.controller;

import com.ztt.cloudprovider.clouddaointerface.CloudDaoService;
import com.ztt.common.entity.CommonUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZTT
 */
@RestController
@Slf4j
@RequestMapping(path = "/provider/v1")
public class ProviderController {
    @Value("${server.port}")
    Integer port;

    @Value("${spring.application.name}")
    String providerName;

    @Autowired
    private CloudDaoService cloudDaoService;

    @PostMapping(path = "/list")
    public List<String> list() {
        log.info("执行了cloud-provider的list....");
        List<String> list = new ArrayList<>();
        list.add("java技术爱好者");
        list.add("SpringCloud");
        list.add("没有人比我更懂了");
        return list;
    }

    @PostMapping(path = "hello")
    public String hello() {
        return "当前服务端口:" + this.port;
    }

    @PostMapping(path = "hello2/{name}", name = "hello2")
    public Map<Object, Object> hello2(@PathVariable("name") String name, HttpServletRequest request) {
        HashMap<Object, Object> map = new HashMap<>(3);
        map.put("methodName", "hello2");
        map.put("param", name);
        map.put("serverName", this.providerName);
        map.put("serverPost", this.port);
        map.put("url", request.getRequestURL().toString());
        return map;
    }

    @PostMapping(path = "hello3")
    public Map<Object, Object> hello3(@RequestBody CommonUser user, String name, HttpServletRequest request) {
        log.info("hello3调用,参数：{}，{}", user, name);
        HashMap<Object, Object> map = new HashMap<>(3);
        map.put("methodName", "hello3");
        map.put("param", new String[]{name, user.toString()});
        map.put("serverName", this.providerName);
        map.put("serverPost", this.port);
        map.put("url", request.getRequestURL().toString());
        return map;
    }

    @PostMapping(path = "hello5/{name}/{password}")
    public Map<Object, Object> hello5(@PathVariable("name") String name, @PathVariable("password") String password, HttpServletRequest request) {
        HashMap<Object, Object> map = new HashMap<>(3);
        map.put("methodName", "hello5");
        map.put("param", new String[]{name, password});
        map.put("serverName", this.providerName);
        map.put("serverPost", this.port);
        map.put("url", request.getRequestURL().toString());
        return map;
    }

    @PostMapping("sys")
    public List<CommonUser> sysUserList(String loginName, String password) {
        return this.cloudDaoService.sysUserList(loginName, password);
    }
}
