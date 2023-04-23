package com.ztt.common.config.feign;

import com.ztt.common.util.EnvironmentUtil;
import com.ztt.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 通过feign 传递请求头数据
 */

@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    public static final ArrayList<String> CHECK_LIST = new ArrayList<>();

    static {
        CHECK_LIST.add("accept");
        CHECK_LIST.add("content-length");
        CHECK_LIST.add("accept-encoding");
    }

    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void apply(RequestTemplate template) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (Objects.nonNull(headerNames)) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if (checkTransmit(name)) {
                    continue;
                }
                String values = request.getHeader(name);
                template.header(name, values);
            }
        }
        customHeaders(template);
    }


    public void customHeaders(RequestTemplate template) {
        template.header(CommonConstant.APPLICATION_NAME, EnvironmentUtil.getLocationAppName());
        // template.header(CommonConstant.REQUEST_ID, RequestIdUtils.getRequestId());
        template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        // 如果设置了Transfer-Encoding为chunked,content-length将被忽略
        template.header(HttpHeaders.TRANSFER_ENCODING, "chunked");
        // 当feign返回了异常消息后,压缩算法导致了数据乱码无法解码
        template.header(HttpHeaders.ACCEPT_ENCODING, "identity");
        //log.info("feign的头部设置了请求ID:{}", RequestIdUtils.getRequestId());
    }

    private boolean checkTransmit(String name) {
        // Accept值不传递，避免出现需要响应xml的情况
        // 服务之间携带信息,content-length长度可能和body不一致导致写入出错,直接过滤掉该属性
        return CHECK_LIST.contains(name.toLowerCase());
    }
}
