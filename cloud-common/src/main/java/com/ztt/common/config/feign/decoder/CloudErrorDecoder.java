package com.ztt.common.config.feign.decoder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztt.common.exception.custom.CustomServiceException;
import com.ztt.common.util.EnvironmentUtil;
import com.ztt.responsecode.ResultData;
import com.ztt.responsecode.ReturnCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

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
        ResultData data;
        try {
            String responseBody = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
            String message = new String(responseBody.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            Object readValue;
            boolean isLink = true;
            try {
                readValue = objectMapper.readValue(message, Object.class);
            } catch (JsonProcessingException e) {
                readValue = message;
                isLink = false;
            }
            if (readValue instanceof ResultData || (isLink && ((LinkedHashMap<?, ?>) readValue).containsKey("status"))) {
                data = objectMapper.readValue(message, ResultData.class);
            } else {
                ResultData fail = ResultData.fail(ReturnCode.RC209.getCode(), ReturnCode.RC209.getMessage());
                fail.setData(message);
                data = fail;
            }
        } catch (IOException e) {
            log.error(EnvironmentUtil.getLocationAppName() + "CloudErrorDecoder:decode()发生错误{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return new CustomServiceException(methodKey, response.body().toString(), data);
    }
}