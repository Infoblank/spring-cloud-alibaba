package com.minio.cloudminio.config.minioconfig;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Set;

@ConfigurationProperties(prefix = "minio")
@Component
@Data
public class MinioProperties implements InitializingBean {
    /**
     * 连接地址
     */
    private String endpoint;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 域名
     */
    private String nginxHost;

    private Duration connectTimeout = Duration.ofSeconds(10);

    private Duration writeTimeout = Duration.ofSeconds(60);

    private Duration readTimeout = Duration.ofSeconds(10);

    private boolean checkBucket = true;

    private boolean createBucketIfNotExist = true;

    // 分类
    private Set<String> classifications;

    private String bucketName;

    private String objectPrefix;

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(accessKey, "accessKey must not be empty.");
        Assert.hasText(secretKey, "secretKey must not be empty.");
        Assert.hasText(bucketName, "bucketName must not be empty.");
        Assert.hasText(objectPrefix, "objectPrefix must not be empty.");
    }
}
