package com.ztt.consumer.cloudconsumer.config;

import com.ztt.consumer.cloudconsumer.filter.CustomFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<CustomFilter> customFilterFilterRegistrationBean() {
        FilterRegistrationBean<CustomFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CustomFilter());
        filterRegistrationBean.setOrder(-1);
        filterRegistrationBean.setUrlPatterns(List.of("/**"));
        return filterRegistrationBean;
    }

    @Bean
    public RestTemplate restTemplateBean() {
        return new RestTemplate();
    }

}
