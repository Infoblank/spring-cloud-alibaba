package com.ztt.cloudgateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztt.responsecode.ResultData;
import com.ztt.responsecode.ReturnCode;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Order(-10)
@Slf4j
@Component
@RequiredArgsConstructor
public class JsonErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public @NonNull Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        if (response.isCommitted()) {
            //对于已经committed(提交)的response，就不能再使用这个response向缓冲区写任何东西
            return Mono.error(ex);
        }
        //Object attribute = exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);
        // header set 响应JSON类型数据，统一响应数据结构（适用于前后端分离JSON数据交换系统）
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 按照异常类型进行翻译处理，翻译的结果易于前端理解
        String message;
        String data;
        if (ex instanceof NotFoundException notFoundException) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            message = notFoundException.getMessage();
            data = "您请求的服务不存在";
        } else if (ex instanceof WebExchangeBindException webExchangeBindException) {
            // 参数验证
            response.setStatusCode(webExchangeBindException.getStatusCode());
            message = webExchangeBindException.getLocalizedMessage();
            data = ReturnCode.RC202.getMessage();
        } else if (ex instanceof ResponseStatusException responseStatusException) {
            response.setStatusCode(responseStatusException.getStatusCode());
            if (responseStatusException.getStatusCode().value() == ReturnCode.RC404.getCode()) {
                message = ReturnCode.RC404.getMessage();
            } else {
                message = responseStatusException.getLocalizedMessage();
            }
            data = "";
        }/* else if (ex instanceof GateWayException) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            message = ex.getMessage();
        }*/ else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            message = ex.getLocalizedMessage();
            data = "请联系管理员";
        }
        log.error("JsonErrorWebExceptionHandler...");
        ex.printStackTrace();
        ResultData build = ResultData.builder().status(Objects.requireNonNull(response.getStatusCode()).value()).dataType("String").message(message).requestPath(request.getPath().toString()).data(data).operationTimestamp(System.nanoTime()).build();
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsString(build).getBytes(StandardCharsets.UTF_8));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
