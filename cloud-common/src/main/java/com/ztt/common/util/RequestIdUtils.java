package com.ztt.common.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.util.Objects;

/**
 * 生成和存储唯一的请求id
 */
public class RequestIdUtils {
    // private static final InheritableThreadLocal<String> requestIdHolder = new InheritableThreadLocal<>();
    private static final TransmittableThreadLocal<String> requestIdHolder = new TransmittableThreadLocal<>();

    private RequestIdUtils() {
    }

    public static void generateRequestId() {
        requestIdHolder.set(NanoIdUtils.randomNanoId());
    }

    public static void generateRequestId(String id) {
        requestIdHolder.set(id);
    }

    /**
     * 获取到当前请求的唯一请求id,只有出现NoHandlerFoundException的时候,requestIdHolder.get()为null
     *
     * @return 唯一字符串
     */
    public static String getRequestId() {
        String id = requestIdHolder.get();
        boolean aNull = Objects.isNull(id);
        if (aNull) {
            generateRequestId();
        }
        return requestIdHolder.get();
    }

    public static void removeRequestId() {
        requestIdHolder.remove();
    }
}
