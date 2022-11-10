package com.ztt.common.exception;


import com.ztt.common.exception.custom.CustomServiceException;
import com.ztt.responsecode.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CloudFeignException {


    @ExceptionHandler(CustomServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData exception(Exception e) {
        if (e instanceof CustomServiceException feignException) {
            return feignException.getResultData();
        }
        return null;
    }
}
