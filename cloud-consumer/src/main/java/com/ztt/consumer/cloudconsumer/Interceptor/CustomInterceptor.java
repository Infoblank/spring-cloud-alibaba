package com.ztt.consumer.cloudconsumer.Interceptor;

import com.ztt.consumer.cloudconsumer.util.RequestIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CustomInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler)
            throws Exception {
        log.info("拦截器执行,{}", "preHandle");
        return true;
    }

    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        log.info("拦截器执行,{}", "postHandle");
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler,
                                @Nullable Exception ex) throws Exception {
        log.info("拦截器执行,{}", "CustomInterceptor:afterCompletion");
        // 清除掉请求ID
        RequestIdUtils.removeRequestId();
        // 清除MDC,即日志的[%X{REQUEST_ID}]
        MDC.clear();
        log.info("清除RequestId,MDC");
    }
}
