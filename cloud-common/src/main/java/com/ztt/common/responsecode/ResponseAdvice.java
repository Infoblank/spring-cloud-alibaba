package com.ztt.common.responsecode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztt.common.aop.ClearId;
import com.ztt.common.constant.CommonConstant;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * RestControllerAdvice:1,全局异常处理;2,全局数据绑定;3,全局数据预处理
 *
 * 按道理只需要在网关做统一封装局可以了 这里主要是集成了很多不好重构 暂时不处理。
 */
@RestControllerAdvice
@Slf4j
@SuppressWarnings("all")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
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
        // 判断有某一个类型的注解才使用该类来转换消息
        boolean ann = (returnType.getContainingClass().isAnnotationPresent(ClearId.class) || returnType.hasMethodAnnotation(ClearId.class));
        // 如果有appName就不需要做结果封装处理
        String header = httpServletRequest.getHeader(CommonConstant.APPLICATION_NAME);
        return !Objects.nonNull(header);
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
        // 所以的服务都做了返回封装,有可能回导致取不到最开始进入的链路traceId (在链路里面的服务报错的情况下)
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
