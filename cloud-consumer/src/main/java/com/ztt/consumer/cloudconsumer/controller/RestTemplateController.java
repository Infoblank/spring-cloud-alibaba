package com.ztt.consumer.cloudconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/consumer")
public class RestTemplateController {

    @Autowired
    private RestTemplate template;

    /*@Autowired
    private LoadBalancerClient loadBalancerClient;*/

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/restTemplate")
    public String restTemplate() {
        //template.exchange("");
       // ServiceInstance choose = loadBalancerClient.choose("cloud-provider");
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-provider");
        String url = "http://" + instances.get(0).getHost() + ":" + instances.get(0).getPort() + "/provider/v1/list";
        return Objects.requireNonNull(template.postForObject(url, new ArrayList<>(), List.class)).toString();
    }
}
