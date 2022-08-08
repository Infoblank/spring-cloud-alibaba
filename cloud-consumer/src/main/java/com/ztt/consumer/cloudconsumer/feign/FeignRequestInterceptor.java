package com.ztt.consumer.cloudconsumer.feign;

import com.ztt.common.constant.CommonConstant;
import com.ztt.common.util.RequestIdUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 通过feign 传递请求头数据
 */

@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Value("${spring.application.name:'appName'}")
    private String appName;
    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void apply(RequestTemplate template) {
        if (request != null && request.getHeaderNames() != null) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                // Accept值不传递，避免出现需要响应xml的情况
                if ("Accept".equalsIgnoreCase(name)) {
                    continue;
                }
                String values = request.getHeader(name);
                template.header(name, values);
            }
        }
        template.header(CommonConstant.APPLICATION_NAME, appName);
        template.header(CommonConstant.REQUEST_ID, RequestIdUtils.getRequestId());
        template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        log.info("feign的头部设置了请求ID:{}", RequestIdUtils.getRequestId());
    }
}
