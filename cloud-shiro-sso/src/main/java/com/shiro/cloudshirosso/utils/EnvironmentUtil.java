package com.shiro.cloudshirosso.utils;

import lombok.NonNull;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 获取系统的环境设置
 */
@Component
public class EnvironmentUtil implements EnvironmentAware {

    public static Environment localeEnvironment;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        localeEnvironment = environment;
    }

    public static String getProperty(String key) {
        return localeEnvironment.getProperty(key);
    }
}
