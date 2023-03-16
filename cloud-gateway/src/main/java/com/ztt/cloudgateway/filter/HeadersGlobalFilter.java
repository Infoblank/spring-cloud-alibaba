package com.ztt.cloudgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class HeadersGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest exchangeRequest = exchange.getRequest();
        HttpHeaders headers = exchangeRequest.getHeaders();
        ServerHttpRequest.Builder mutate = exchangeRequest.mutate();
        List<String> token = headers.get("accessToken");
        if (Objects.isNull(token) || token.size() == 0) {
            log.info("当前请求没有携带accessToken,重新设置accessToken.");
            mutate.header("accessToken", "a@@jansjd");
        }
        ServerHttpRequest build = mutate.build();
        return chain.filter(exchange.mutate().request(build).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
