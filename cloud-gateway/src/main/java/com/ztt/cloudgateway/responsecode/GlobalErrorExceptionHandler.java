package com.ztt.cloudgateway.responsecode;

import lombok.NonNull;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/*@Configuration
@Order(-3)*/
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public @NonNull Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        if (ex instanceof ResponseStatusException e) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }
        return null;
    }
}
