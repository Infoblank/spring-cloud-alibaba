package com.ztt.common.config.feign;

import com.ztt.common.config.feign.decoder.FeignDecoder;
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
import org.springframework.web.context.request.RequestContextListener;

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

    /**
     * 在子线程调用feign的过程当中,获取到HttpServletRequest对象头没有任何东西,
     * 导致了在FeignRequestInterceptor当中获取getHeaderNames报错,无法正常调用feign的服务
     *
     * @return RequestContextListener 返回一个上下文请求监听对象
     */

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
