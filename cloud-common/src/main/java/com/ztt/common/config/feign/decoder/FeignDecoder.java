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
        String responseJson = getResponseJson(response);
        try {
            return decoder.decode(response, type);
        } catch (DecodeException e) {
            log.info("feign 返回数据:{}", responseJson);
            throw new DecodeException(response.status(), e.getMessage(), response.request(), e);
        } catch (FeignException e) {
            log.info("feign 返回数据:{}", responseJson);
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
