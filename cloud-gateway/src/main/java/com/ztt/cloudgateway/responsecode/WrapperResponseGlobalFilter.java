package com.ztt.cloudgateway.responsecode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztt.cloudgateway.utils.GZIPUtils;
import com.ztt.responsecode.ResultData;
import com.ztt.responsecode.ReturnCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author ztt
 */
@Component
@Slf4j
public class WrapperResponseGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public int getOrder() {
        // -1 is response write filter, must be called before that
        return -2;
    }

    /**
     * 返回值处理
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("global filter HttpResponseBody，processing response results");
        // 这里可以增加一些业务判断条件，进行跳过处理
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        DataBufferFactory bufferFactory = response.bufferFactory();
        // 响应装饰
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public @NonNull Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                log.info("global filter HttpResponseBody,Response processing，getStatusCode={}", getStatusCode());
                if (getStatusCode() != null && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        // 如果响应过大，会进行截断，出现乱码，看api DefaultDataBufferFactory
                        // 有个join方法可以合并所有的流，乱码的问题解决
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer dataBuffer = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        // 释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        List<String> encodingList = exchange.getResponse().getHeaders().get(HttpHeaders.CONTENT_ENCODING);
                        boolean zip = encodingList != null && encodingList.contains("gzip");
                        // responseData就是response的值，就可查看修改了
                        String responseData = getResponseData(zip, content);

                        // 重置返回参数
                        String result;
                        try {
                            result = responseConversion(responseData, path);
                        } catch (JsonProcessingException e) {
                            log.error("重置返回参数出错,{}", e.getMessage());
                            result = responseData;
                        }
                        byte[] uppedContent = getUppedContent(zip, result);
                        response.getHeaders().setContentLength(uppedContent.length);
                        response.setStatusCode(HttpStatus.OK);

                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @SuppressWarnings("all")
    private String responseConversion(String result, String path) throws JsonProcessingException {
        try {
            log.info("响应结果为：{}", result);
            Object resultValue = this.objectMapper.readValue(result, Object.class);
            if (resultValue instanceof LinkedHashMap<?, ?> && ((LinkedHashMap<?, ?>) resultValue).containsKey("status")) {
                ((LinkedHashMap<String, Object>) resultValue).put("requestPath", path);
                ((LinkedHashMap<String, Object>) resultValue).put("dataType", "String");
                return this.objectMapper.writeValueAsString(resultValue);
            } else {
                ResultData build = ResultData.builder().status(ReturnCode.RC200.getCode()).message(ReturnCode.RC200.getMessage()).data(resultValue).dataType(resultValue.getClass().getSimpleName()).operationTimestamp(System.nanoTime()).requestPath(path).build();
                return this.objectMapper.writeValueAsString(build);
            }
        } catch (Exception e) {
            log.error("响应包装转换失败，异常信息为：", e);
            ResultData success = ResultData.success(result);
            success.setRequestPath(path);
            return this.objectMapper.writeValueAsString(success);
        }
    }

    private String getResponseData(boolean zip, byte[] content) {
        String responseData;
        if (zip) {
            responseData = GZIPUtils.uncompressToString(content);
        } else {
            responseData = new String(content, StandardCharsets.UTF_8);
        }
        return responseData;
    }

    private byte[] getUppedContent(boolean zip, String result) {
        byte[] uppedContent;
        if (zip) {
            uppedContent = GZIPUtils.compress(result);
        } else {
            uppedContent = result.getBytes(StandardCharsets.UTF_8);
        }
        return uppedContent;
    }

}


