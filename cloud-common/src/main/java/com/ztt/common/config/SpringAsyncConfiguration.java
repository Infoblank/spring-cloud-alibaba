package com.ztt.common.config;

import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * //@ConditionalOnBean 当容器有指定Bean的条件下
 * //@ConditionalOnClass 当容器有指定类的条件下
 * //@ConditionalOnExpression 基于SpEL表达式作为判断条件
 * //@ConditionalOnJava 基于JVM版本作为判断条件
 * //@ConditionalOnJndi 在JDNI存在的条件下查找指定位置
 * //@ConditionalOnMissingBean 当容器没有指定Bean的情况下
 * //@ConditionalOnMissingClass 当容器没有指定类的情况下
 * //@ConditionalOnNotWebApplication 当前项目不是Web项目的条件下
 * //@ConditionalOnProperty 指定的属性是否有指定的值
 * //@ConditionalOnResource 类路径是否有指定的值
 * //@ConditionalOnSingleCandidate 当前指定Bean在容器中只有一个，或者虽然有多个但是指定首选Bean
 * //@ConditionalOnWebApplication 当前项目是Web项目的情况下
 */
//@ConditionalOnMissingBean(ThreadPoolTaskExecutor.class)
//@Configuration
//@EnableAsync
// 是否开启sleuth
//@ConditionalOnProperty(prefix = "cloud.sleuth", havingValue = "false", name = "enable")
public class SpringAsyncConfiguration implements AsyncConfigurer {

    /*@Resource
    private ThreadPoolTaskExecutorProperty tp;

    *//**
     * 应用内部的线程池,做一些基本的配置
     *
     * @return threadPoolTaskExecutor 线程池
     *//*
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

    *//**
     * 返回线程池
     *
     * @return threadPoolTaskExecutor
     *//*
    // @Bean
    @Override
    public Executor getAsyncExecutor() {
        return threadPoolTaskExecutor();
    }

    *//**
     * 异常处理器,返回的方法为void的时候使用
     *
     * @return CloudAsyncUncaughtExceptionHandler
     *//*
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CloudAsyncUncaughtExceptionHandler();
    }*/
}
