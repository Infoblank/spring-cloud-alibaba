package com.ztt.common.async;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * 线程池中的线程任务执行报错后的处理方法
 */
@Slf4j
public class CloudAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, @NonNull Method method, Object... params) {
        // 线程执行报错后,需要清除掉线程的请求id以面记录错误的id导致无法正确的查询到问题
        ex.printStackTrace();
        log.error("方法:{}发生错误:{},参数:{}", method, ex.getMessage(), params);
    }
}
