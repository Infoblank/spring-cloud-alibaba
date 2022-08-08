package com.minio.cloudminio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudMinioApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CloudMinioApplication.class);
        springApplication.addInitializers();
        springApplication.run(args);
       // SpringApplication.run(CloudMinioApplication.class, args);
    }

}
