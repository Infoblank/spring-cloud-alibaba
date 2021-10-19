package com.ztt.cloudprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ZTT
 */
@EnableFeignClients(basePackages = {"com.ztt.cloudprovider.clouddaointerface"})
@SpringBootApplication
public class CloudProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudProviderApplication.class, args);
    }

}
