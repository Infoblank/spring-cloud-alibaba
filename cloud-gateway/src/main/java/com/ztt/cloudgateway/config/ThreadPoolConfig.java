package com.ztt.cloudgateway.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
//@ConditionalOnProperty(prefix = "cloud.sleuth", havingValue = "true", name = "enable")
public class ThreadPoolConfig {

    private static final ThreadPoolExecutor.CallerRunsPolicy runsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();


    @Resource
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
    /*@Bean
    public AsyncConfigurer asyncConfigurer() {
        //  在这里返回一个 LazyTraceAsyncCustomizer 类型的AsyncConfigurer
        new LazyTraceExecutor(this.beanFactory, threadPoolTaskExecutor());
        return new LazyTraceAsyncCustomizer(this.beanFactory, new AsyncConfigurer() {
            @Override
            public Executor getAsyncExecutor() {
                return threadPoolTaskExecutor();
            }

            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return (ex, method, params) -> {
                    log.error("Async Exception**************************************************************");
                    log.error("method happen: {}", method);
                    log.error("method params: {}", params);
                    log.error("Exception class: {}", ex.getClass().getName());
                    log.error("ex.getMessage(): {}", ex.getMessage());
                    log.error("**************************************************************");
                };
            }
        });
    }*/

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Gateway-Yk-task-");
        // 拒绝处理程序
        threadPoolTaskExecutor.setRejectedExecutionHandler(runsPolicy);
        // 队列大小
        threadPoolTaskExecutor.setQueueCapacity(1000);
        //配置核心线程数
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        //配置最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 20);
        //执行初始化
        threadPoolTaskExecutor.initialize();
        log.info("网关初始化ThreadPoolTaskExecutor");
        return threadPoolTaskExecutor;
    }
}
