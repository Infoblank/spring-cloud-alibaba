package com.ztt.common.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 为每一次请求赋值唯一请求id和请求结束清除唯一id
 */
@Slf4j
public class RequestIdInterceptor implements HandlerInterceptor {
    /**
     * 在请求controller之前执行,只有当前返回true才会执行postHandle和afterCompletion
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return 返回true才会往下执行
     * @throws Exception e
     */
    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler)
            throws Exception {
        //CommonUtil.addRequestIdAndMDCId(request, response);
        return true;
    }

    /**
     * controller 执行完成后执行
     *
     * @param request      current HTTP request
     * @param response     current HTTP response
     * @param handler      the handler (or {@link HandlerMethod}) that started asynchronous
     *                     execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     *                     (can also be {@code null})
     * @throws Exception e
     */
    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("拦截器执行,请求路径{}", requestURI);
    }

    /**
     * 整个请求执行完后执行
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the handler (or {@link HandlerMethod}) that started asynchronous
     *                 execution, for type and/or instance examination
     * @param ex       any exception thrown on handler execution, if any; this does not
     *                 include exceptions that have been handled through an exception resolver
     * @throws Exception e
     */
    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler,
                                @Nullable Exception ex) throws Exception {
       // CommonUtil.clearRequestIdAndMDCId(request, response);
    }
}
