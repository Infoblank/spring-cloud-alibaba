package com.shiro.cloudshirosso.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 将spring容器放在类的静态变量context里面,可以在任何地方获取到容器里面的bean。
 */
@Component
@Slf4j
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException bean获取异常
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        log.info("ApplicationContextUtils的ApplicationContext[context]初始化...");
        RedisTool.redisTemplate = (RedisTemplate<String, Object>) getBean("customRedisTemplate");
        log.info("RedisTool的RedisTemplate[redisTemplate]初始化成功...");
    }

    /**
     * 无法获取时会抛出异常
     *
     * @param beanName 需要获取的bean的名称
     * @return 返回对应的bean
     */
    public static Object getBean(String beanName) {
        log.info("从上下文获取bean:[{}]", beanName);
        return context.getBean(beanName);
    }
}
