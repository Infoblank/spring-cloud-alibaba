package com.ztt.consumer.cloudconsumer.config;

import com.ztt.consumer.cloudconsumer.feign.decoder.FeignDecoder;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    // 自定义解码器
    @Bean
    public Decoder decoder(ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        return new FeignDecoder(
                new OptionalDecoder(
                        new ResponseEntityDecoder(
                                new SpringDecoder(messageConverters, customizers))));
    }


}
