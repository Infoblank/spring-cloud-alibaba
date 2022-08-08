package com.ztt.consumer.cloudconsumer.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

//@Component
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
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
