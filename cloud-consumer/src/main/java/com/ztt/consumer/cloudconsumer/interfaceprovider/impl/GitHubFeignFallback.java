package com.ztt.consumer.cloudconsumer.interfaceprovider.impl;

import com.ztt.consumer.cloudconsumer.interfaceprovider.GitHubFeign;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

//@Component
@Slf4j
public class GitHubFeignFallback implements GitHubFeign {
    @Override
    public Map<String, Object> searchRepositories(String q) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("error","请求报错...");
        return map;
    }
}
