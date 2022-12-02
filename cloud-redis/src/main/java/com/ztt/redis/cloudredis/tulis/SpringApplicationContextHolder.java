package com.ztt.redis.cloudredis.tulis;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;


    @Override
    @SuppressWarnings("all")
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        RedisTool.redisTemplate = getBean("redisTemplate", RedisTemplate.class);
    }

    /**
     * 无法获取时会抛出异常
     *
     * @param beanName 需要获取的bean的名称
     * @return 返回对应的bean
     */
    public static @NonNull Object getBean(String beanName) {
        log.info("从上下文获取bean:[{}]", beanName);
        return context.getBean(beanName);
    }

    public static <T> @NonNull T getBean(String beanName, Class<T> className) {
        return context.getBean(beanName, className);
    }

    public static <T> @NonNull T getBean(Class<T> className) {
        return context.getBean(className);
    }

    public static Environment getEnvironment() {
        return context.getEnvironment();
    }
}