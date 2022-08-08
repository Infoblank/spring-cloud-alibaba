package com.flux.springbootwebflux.router;

import com.flux.springbootwebflux.handler.TimeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    // Not annotated method is used as an override for a method annotated with NonNullApi
    public RouterFunction<ServerResponse> routerSayHi(TimeHandler timeHandler) {
        return RouterFunctions.route(RequestPredicates.GET("webflux/hi").and(RequestPredicates.accept(MediaType.ALL)), timeHandler::sayHi);

    }
}
