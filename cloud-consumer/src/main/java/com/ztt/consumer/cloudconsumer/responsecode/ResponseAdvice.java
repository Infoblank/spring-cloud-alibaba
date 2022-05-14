package com.ztt.consumer.cloudconsumer.responsecode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * RestControllerAdvice:1,全局异常处理;2,全局数据绑定;3,全局数据预处理
 */
@RestControllerAdvice
@Slf4j
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        // objectMapper.set
        this.objectMapper = objectMapper;
    }

    /**
     * 返回true则表示需要执行beforeBodyWrite,否则就不执行
     *
     * @param returnType    e
     * @param converterType e
     * @return e
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("supports已经返回true马上执行beforeBodyWrite{}", returnType.getParameterType());
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, @NonNull MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        assert body != null;
        log.info("ResponseAdvice.supports已经执行完毕开始beforeBodyWrite{}", returnType.getParameterType());
        if (body instanceof String) {
            try {
                return this.objectMapper.writeValueAsString(ResultData.success(body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        if (body instanceof ResultData) {
            return body;
        }
        return ResultData.success(body);
    }
}
