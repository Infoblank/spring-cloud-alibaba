package com.ztt.common.config.feign.decoder;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignDecoder implements Decoder {
    // 代理默认的 decoder
    private final Decoder decoder;

    public FeignDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        // 这里读取后response里面的body无法再次读取,在31行重新封装了返回的response
        String responseJson = getResponseJson(response);
        log.info("feign返回数据:{}", responseJson);
        try {
            // 因为Response只能读取异常,所以重新回写body在把response传递到下一个Decoder
            return decoder.decode(response.toBuilder().body(responseJson, StandardCharsets.UTF_8).build(), type);
        } catch (DecodeException e) {
            log.info("DecodeException feign返回数据:{}", responseJson);
            throw new DecodeException(response.status(), e.getMessage(), response.request(), e);
        } catch (FeignException e) {
            log.info("FeignException feign返回数据:{}", responseJson);
            throw e;
        }
    }

    // 响应值转json字符串
    private String getResponseJson(Response response) throws IOException {
        try (InputStream inputStream = response.body().asInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
