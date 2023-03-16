package com.minio.cloudminio.config.minioconfig;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Slf4j
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    private MinioProperties minioProperties;

    @Autowired
    public void setMinioProperties(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean(name = "minioClient")
    public MinioClient minioClient() throws ServerException,
            InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder().endpoint(minioProperties.getEndpoint()).credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();
        minioClient.setTimeout(
                minioProperties.getConnectTimeout().toMillis(),
                minioProperties.getWriteTimeout().toMillis(),
                minioProperties.getReadTimeout().toMillis());
        if (minioProperties.isCheckBucket()) {
            String bucketName = minioProperties.getBucketName();
            BucketExistsArgs existsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            boolean bucketExists = minioClient.bucketExists(existsArgs);
            if (!bucketExists) {
                if (minioProperties.isCreateBucketIfNotExist()) {
                    MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                    minioClient.makeBucket(makeBucketArgs);
                } else {
                    throw new IllegalStateException("Bucket does not exist: " + bucketName);
                }
            }
        }
        return minioClient;
    }

}
