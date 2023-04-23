package com.ztt.cloudgateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBufAllocator;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 对请求body做处理，其实可以不需要，因为本来也没有做任何处理
 */
@Slf4j
@Component
public class ModifyRequestBodyGlobalFilter implements GlobalFilter, Ordered {

    private final DataBufferFactory dataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //构建新数据流,当body为空时,构建空流
        ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            @SuppressWarnings("all")
            public @NonNull Flux<DataBuffer> getBody() {
                Flux<DataBuffer> body = super.getBody();
                InputStreamHolder holder = new InputStreamHolder();
                // 表示body被Consumer订阅
                body.subscribe(buffer -> holder.inputStream = buffer.asInputStream());
                if (Objects.nonNull(holder.inputStream)) {
                    try {
                        // 解析JSON的节点
                        JsonNode jsonNode = objectMapper.readTree(holder.inputStream);
                        Assert.isTrue(jsonNode instanceof ObjectNode, "JSON格式异常");
                        ObjectNode objectNode = (ObjectNode) jsonNode;
                        // JSON节点最外层写入新的属性 CLOUD-APPLICATION
                        DataBuffer dataBuffer = dataBufferFactory.allocateBuffer();
                        String json = objectNode.toString();
                        log.info("请求body修改完成,最终的JSON数据为:{}", json);
                        dataBuffer.write(json.getBytes(StandardCharsets.UTF_8));
                        return Flux.just(dataBuffer);
                    } catch (Exception e) {
                        throw new IllegalStateException("参数无法解析,请检查参数是否正确。");
                    }
                } else {
                    return super.getBody();
                }
            }
        };
        // 使用修改后的ServerHttpRequestDecorator重新生成一个新的ServerWebExchange
        return chain.filter(exchange.mutate().request(requestDecorator).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }


    private static class InputStreamHolder {
        InputStream inputStream;
    }
}

