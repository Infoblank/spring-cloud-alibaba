package com.minio.cloudminio.utils;

import com.minio.cloudminio.config.fileconfig.UploadProperties;
import io.minio.MinioClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
    @SuppressWarnings("all")
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        MinioUtil.minioClient = getBean("minioClient", MinioClient.class);
        MinioUtil.uploadProperties = getBean(UploadProperties.class);
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

}
