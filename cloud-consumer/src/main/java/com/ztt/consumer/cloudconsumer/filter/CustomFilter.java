package com.ztt.consumer.cloudconsumer.filter;


import com.ztt.common.constant.CommonConstant;
import com.ztt.consumer.cloudconsumer.util.RequestIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class CustomFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("过滤器执行");
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        Object attribute = request.getAttribute("x-request-id");
        if (Objects.isNull(attribute)) {
            RequestIdUtils.generateRequestId();
            String requestId = RequestIdUtils.getRequestId();
            MDC.put(CommonConstant.REQUEST_ID, requestId);
            request.setAttribute(CommonConstant.X_REQUEST_ID, requestId);
            log.info("生成REQUEST-ID");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
