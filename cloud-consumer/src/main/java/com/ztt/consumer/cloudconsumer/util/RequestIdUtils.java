package com.ztt.consumer.cloudconsumer.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

/**
 * 生成和存储唯一的请求id
 */
public class RequestIdUtils {
    private static final ThreadLocal<String> requestIdHolder = new ThreadLocal<>();

    private RequestIdUtils() {
    }

    public static void generateRequestId() {
        requestIdHolder.set(NanoIdUtils.randomNanoId());
    }

    public static void generateRequestId(String id) {
        requestIdHolder.set(id);
    }

    public static String getRequestId() {
        return requestIdHolder.get();
    }

    public static void removeRequestId() {
        requestIdHolder.remove();
    }
}
