package com.ztt.consumer.cloudconsumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;

@Configuration
@ConfigurationProperties(prefix = "mv")
public class WebMvcConfigLocationsMapping {

    private String[] locationMappings;


    public void setLocationMappings(String[] locationMappings) {
        this.locationMappings = locationMappings;
    }

    public String[] getLocationMappings() {
        return this.locationMappings;
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        return converter;
    }
}
