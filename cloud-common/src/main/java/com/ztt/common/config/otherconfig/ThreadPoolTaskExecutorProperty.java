package com.ztt.common.config.otherconfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 线程池的默认配置
 */
@Component
@Data
public class ThreadPoolTaskExecutorProperty {

    @Value("${thread.pool.core-pool-size:8}")
    public int corePoolSize;
    @Value("${thread.pool.max-pool-size:16}")
    private int maxPoolSize;
    @Value("${thread.pool.keep-alive-seconds:10}")
    private int keepAliveSeconds;
    @Value("${thread.pool.queue-capacity:1000}")
    private int queueCapacity;
    @Value("${thread.pool.thread-name-prefix:'consumer cloud thread pool:'}")
    private String threadNamePrefix;
}
