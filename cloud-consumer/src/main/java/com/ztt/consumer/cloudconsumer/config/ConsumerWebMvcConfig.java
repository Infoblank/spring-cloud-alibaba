package com.ztt.consumer.cloudconsumer.config;

import com.ztt.consumer.cloudconsumer.Interceptor.CustomInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConsumerWebMvcConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor()).
                excludePathPatterns("/hello").
                addPathPatterns("/**");
    }
}
