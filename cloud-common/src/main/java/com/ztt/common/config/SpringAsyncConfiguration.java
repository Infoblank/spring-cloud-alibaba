package com.ztt.common.config;

import com.ztt.common.async.CloudAsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class SpringAsyncConfiguration implements AsyncConfigurer {


    @Resource
    private ThreadPoolTaskExecutorProperty tp;

    /**
     * 应用内部的线程池,做一些基本的配置
     *
     * @return threadPoolTaskExecutor 线程池
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(tp.getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(tp.getMaxPoolSize());
        threadPoolTaskExecutor.setKeepAliveSeconds(tp.getKeepAliveSeconds());
        threadPoolTaskExecutor.setQueueCapacity(tp.getQueueCapacity());
        threadPoolTaskExecutor.setThreadNamePrefix(tp.getThreadNamePrefix());
        return threadPoolTaskExecutor;
    }

    /**
     * 返回线程池
     *
     * @return threadPoolTaskExecutor
     */
    @Bean
    @Override
    public Executor getAsyncExecutor() {
        return threadPoolTaskExecutor();
    }

    /**
     * 异常处理器,返回的方法为void的时候使用
     *
     * @return CloudAsyncUncaughtExceptionHandler
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CloudAsyncUncaughtExceptionHandler();
    }
}
