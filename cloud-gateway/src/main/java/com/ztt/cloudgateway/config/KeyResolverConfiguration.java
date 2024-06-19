package com.ztt.cloudgateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class KeyResolverConfiguration {


	@Primary
	@Bean
	public KeyResolver userKeyResolver() {
		return exchange -> {
			ServerHttpRequest request = exchange.getRequest();
			HttpHeaders headers = request.getHeaders();
			String path = request.getPath().toString();
			log.info("网关开始限流,当前请求路径:{}", path + headers);
			return Mono.just(path);
		};
	}
}
