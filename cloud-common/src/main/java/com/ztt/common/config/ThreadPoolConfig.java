package com.ztt.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceAsyncCustomizer;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池初始化配置 开启了sleuth是加载
 */
@Configuration
@EnableAsync
@Slf4j
//@ConditionalOnProperty(prefix = "cloud.sleuth", havingValue = "true", name = "enable")
public class ThreadPoolConfig {

    private static final ThreadPoolExecutor.CallerRunsPolicy runsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();


    @Autowired
    private BeanFactory beanFactory;

    /**
     * 解决异步线程池，链路跟踪丢失问题
     * <p>
     * 不通过实现接口的方式配置AsyncConfigurer，而是通过@Configuration 的方式注册一个sleuth中的LazyTraceAsyncCustomizer包装类型的AsyncConfigurer
     * <p>
     * 需要配置application.yaml spring: sleuth: async: configurer: enabled: false 将sleuth中默认的org.springframework.cloud.sleuth.instrument.async.AsyncDefaultAutoConfiguration.DefaultAsyncConfigurerSupport
     * 异步线程池的配置忽略掉。
     *
     * @return 异步配置类
     */
    @Bean
    public AsyncConfigurer asyncConfigurer() {
        //  在这里返回一个 LazyTraceAsyncCustomizer 类型的AsyncConfigurer
        return new LazyTraceAsyncCustomizer(this.beanFactory, new AsyncConfigurer() {
            @Override
            public Executor getAsyncExecutor() {
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                //配置核心线程数
                executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
                //配置最大线程数
                executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 20);
                //配置队列大小
                executor.setQueueCapacity(500);
                //配置线程池中的线程的名称前缀
                executor.setThreadNamePrefix("Yk-async-task-");
                //配置保存时间
                executor.setRejectedExecutionHandler(runsPolicy);
                //执行初始化
                executor.initialize();
                return executor;
            }

            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return (throwable, method, obj) -> log.error("{}", throwable.getMessage());
            }
        });
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Yk-task-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setQueueCapacity(1000);
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 20);
        threadPoolTaskExecutor.initialize();
        // sleuth 中延时跟踪执行
        return new LazyTraceThreadPoolTaskExecutor(beanFactory, threadPoolTaskExecutor);
    }
}
