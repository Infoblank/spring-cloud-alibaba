package com.ztt.consumer.cloudconsumer;import org.springframework.boot.SpringApplication;import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.cloud.openfeign.EnableFeignClients;import org.springframework.scheduling.annotation.EnableAsync;/** * @author ZTT */@SpringBootApplication(scanBasePackages = {"com.ztt.common", "com.ztt.consumer"})@EnableFeignClients(basePackages = {"com.ztt.consumer.cloudconsumer.interfaceprovider"})@EnableAsyncpublic class CloudConsumerApplication {    public static void main(String[] args) {        SpringApplication.run(CloudConsumerApplication.class, args);    }}