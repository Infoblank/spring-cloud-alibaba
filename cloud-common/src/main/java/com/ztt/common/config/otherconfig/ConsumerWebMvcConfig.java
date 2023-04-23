package com.ztt.common.config.otherconfig;

import com.ztt.common.Interceptor.RequestIdInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@Slf4j
@SuppressWarnings("all")
public class ConsumerWebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置文件配置的静态资源路径装配对象
     */


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可以配置不拦截静态资源路径
        registry.addInterceptor(new RequestIdInterceptor()).addPathPatterns("/**");
    }

    /**
     * @param registry 静态资源映射器
     */
    /*@Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String[] locationMappings = webMvcConfigLocationsMapping.getLocationMappings();
        for (String locationMapping : locationMappings) {
            if (locationMapping.contains("html")) {
                registry.addResourceHandler(locationMapping).addResourceLocations("classpath:templates/");
            } else {
                registry.addResourceHandler(locationMapping).addResourceLocations("classpath:static/");
            }
        }
        log.info("添加静态资源路径{}", Arrays.toString(locationMappings));
    }*/

    /**
     * // @RequestBody 和 @RestController 会调用StringHttpMessageConverter消息转换器,其默认的编码是StandardCharsets.ISO_8859_1,导致出现中文乱码
     * 在此统一处理乱码的问题
     *
     * @param converters the list of configured converters to be extended
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> httpMessageConverter : converters) {
            if (httpMessageConverter instanceof StringHttpMessageConverter stringHttpMessageConverter) {
                stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
                stringHttpMessageConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN));
            }
        }
    }
}
