package com.ztt.consumer.cloudconsumer.interfaceprovider;


import com.ztt.consumer.cloudconsumer.interfaceprovider.impl.GitHubFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Feign Get请求参数名称必须定义value
 */
@FeignClient(name = "github-client", url = "https://api.github.com", fallback = GitHubFeignFallback.class)
public interface GitHubFeign {

    @GetMapping(value = "/search/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> searchRepositories(@RequestParam("q") String q);

}
