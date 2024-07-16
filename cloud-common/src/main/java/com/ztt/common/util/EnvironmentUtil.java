package com.ztt.common.util;

import org.springframework.core.env.Environment;

import java.util.Objects;

public class EnvironmentUtil {

    public static Environment environment = null;

    public static String getLocationValue(String key) {
        initEnvironment();
        return environment.getProperty(key, "");
    }

    public static String getLocationPort() {
        initEnvironment();
        return environment.getProperty("local.server.port", "0");
    }

    public static String getLocationAppName() {
        initEnvironment();
        return environment.getProperty("spring.application.name", "appName");
    }

    public static void initEnvironment() {
        if (Objects.isNull(environment)) {
            environment = SpringApplicationContextHolder.getBean(Environment.class);
        }
    }

}
