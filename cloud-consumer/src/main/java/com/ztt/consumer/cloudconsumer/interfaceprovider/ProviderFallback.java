package com.ztt.consumer.cloudconsumer.interfaceprovider;

import com.ztt.api.provider.ProviderInterface;
import com.ztt.entity.CommonUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProviderFallback implements ProviderInterface {
    @Override
    public String list() {
        return null;
    }

    @Override
    public String hello() {
        return "熔断了Feign请求...";
    }

    @Override
    public String hello2(String name1) {
        return null;
    }

    @Override
    public Map<String, Object> hello3(CommonUser user, String name) {
        return null;
    }

    @Override
    public Map<Object, Object> hello5(String name, String password) {
        return null;
    }

    @Override
    public List<CommonUser> sysUserList(String loginName, String password) {
        return null;
    }
}
