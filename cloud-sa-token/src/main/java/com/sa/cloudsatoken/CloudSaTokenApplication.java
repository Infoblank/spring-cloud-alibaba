package com.sa.cloudsatoken;

import cn.dev33.satoken.SaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CloudSaTokenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSaTokenApplication.class, args);
        log.info("启动成功：sa-token配置如下{}", SaManager.getConfig());
    }

}
