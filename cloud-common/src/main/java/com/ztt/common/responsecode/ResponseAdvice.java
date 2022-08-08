package com.ztt.common.responsecode;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * RestControllerAdvice:1,全局异常处理;2,全局数据绑定;3,全局数据预处理
 */
@RestControllerAdvice
@Slf4j
@SuppressWarnings("all")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
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
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("ResponseAdvice:supports:{}", returnType.getParameterType());
        // 判断有某一个类型的注解才使用该类来转换消息
        boolean ann = (returnType.getContainingClass().isAnnotationPresent(ResponseBody.class) || returnType.hasMethodAnnotation(ResponseBody.class));
        // 消息转换器的类型
        return true;
    }

    /**
     * 通用的返回数据处理方法
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        assert body != null;
        if (body instanceof String) {
            try {
                return this.objectMapper.writeValueAsString(ResultData.success(body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("返回数据:[" + body.toString() + "]转化为JSON出错.");
            }
        }
        if (body instanceof ResultData<?> data) {
            // 这个位置主要是发生异常后进入,手动的ResultData基本上都是异常处理
            return data;
        }
        return ResultData.success(body);
    }
}
