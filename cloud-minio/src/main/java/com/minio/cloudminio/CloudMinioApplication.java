package com.minio.cloudminio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.minio.cloudminio", "com.ztt.common"})
@EnableFeignClients(basePackages = {"com.minio.cloudminio.dao"})
public class CloudMinioApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CloudMinioApplication.class);
        springApplication.addInitializers();
        springApplication.run(args);
        // SpringApplication.run(CloudMinioApplication.class, args);
    }

}
