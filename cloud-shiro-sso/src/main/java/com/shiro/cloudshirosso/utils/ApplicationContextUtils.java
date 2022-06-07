package com.shiro.cloudshirosso.utils;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 将spring容器放在类的静态变量context里面,可以在任何地方获取到容器里面的bean。
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 无法获取时会抛出异常
     *
     * @param beanName 需要获取的bean的名称
     * @return 返回对应的bean
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
