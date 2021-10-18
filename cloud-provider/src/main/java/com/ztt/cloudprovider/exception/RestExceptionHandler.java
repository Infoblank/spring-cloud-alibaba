package com.ztt.cloudprovider.exception;

import com.ztt.common.response.CloudResponseEntity;
import com.ztt.common.response.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author ZTT
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CloudResponseEntity<String> exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return CloudResponseEntity.fail(ReturnCode.RC500.getCode(), e.getMessage());
    }
}
