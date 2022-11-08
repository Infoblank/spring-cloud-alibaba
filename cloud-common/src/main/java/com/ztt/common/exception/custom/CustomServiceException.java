package com.ztt.common.exception.custom;

import com.ztt.responsecode.ResultData;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class CustomServiceException extends RuntimeException {

    private String methodKey;

    private String message;

    private ResultData<?> resultData;

    public CustomServiceException(String methodKey, String message, ResultData<?> resultData) {
        super(message);
        this.methodKey = methodKey;
        this.message = message;
        this.resultData = resultData;
    }

}
