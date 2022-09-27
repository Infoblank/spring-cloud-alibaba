package com.ztt.common.config.feign.decoder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztt.common.exception.custom.CustomServiceException;
import com.ztt.common.responsecode.ResultData;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class CloudErrorDecoder implements ErrorDecoder {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        ResultData<?> data;
        try {
            var responseBody = IOUtils.toString(response.body().asInputStream(), "UTF-8");
            String message = new String(responseBody.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            data = objectMapper.readValue(message, ResultData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new CustomServiceException(methodKey, response.body().toString(), data);
    }
}