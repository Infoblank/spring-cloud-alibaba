package com.ztt.consumer.cloudconsumer.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ConditionalOnProperty(name = {"whether.enable.thread.pool"}, havingValue = "true")
public class ThreadPoolTaskExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.setMaxPoolSize(16);
        threadPoolTaskExecutor.setKeepAliveSeconds(10);
        threadPoolTaskExecutor.setQueueCapacity(1000);
        threadPoolTaskExecutor.setThreadNamePrefix("consumer cloud thread pool:");
        return threadPoolTaskExecutor;
    }
}
