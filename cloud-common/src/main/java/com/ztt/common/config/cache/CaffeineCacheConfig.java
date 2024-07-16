package com.ztt.common.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {


    /**
     * Caffeine配置说明:
     * <p>
     * initialCapacity=[integer]: 初始的缓存空间大小
     * <p>
     * maximumSize=[long]: 缓存的最大条数
     * <p>
     * maximumWeight=[long]: 缓存的最大权重
     * <p>
     * expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
     * <p>
     * expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期
     * <p>
     * refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
     * <p>
     * recordStats：开发统计功能
     * <p>
     * 只需要配置Caffeine 即可,CaffeineCacheConfiguration会自动配置一个CaffeineCacheManager来被CacheAutoConfiguration引用
     *
     * @return Caffeine
     */
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).initialCapacity(100).maximumSize(1000);
    }

}
