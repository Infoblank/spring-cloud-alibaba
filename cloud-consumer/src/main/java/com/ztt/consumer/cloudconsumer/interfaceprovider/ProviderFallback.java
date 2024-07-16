package com.ztt.consumer.cloudconsumer.interfaceprovider;

import com.ztt.entity.CommonUser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProviderFallback implements ProviderClient {

    static HashMap<Object, Object> hashMap = new HashMap<>();

    static {
        hashMap.put("error", "ProviderFallback");
    }

    @Override
    public String list() throws Exception {
        throw new Exception("服务不在线请稍后再试！");
    }

    @Override
    public String hello() throws Exception {
        throw new Exception("服务不在线请稍后再试！");
    }

    @Override
    public String hello2(String name1) throws Exception {
        throw new Exception("服务不在线请稍后再试！");
    }

    @Override
    public Map<String, Object> hello3(CommonUser user, String name) throws Exception {
        throw new Exception("服务不在线请稍后再试！");
    }

    @Override
    public Map<Object, Object> hello5(String name, String password) throws Exception {
        throw new Exception("服务不在线请稍后再试！");
    }

    @Override
    public List<CommonUser> sysUserList(String loginName, String password) throws Exception {
        throw new Exception("服务不在线请稍后再试！");
    }
}
