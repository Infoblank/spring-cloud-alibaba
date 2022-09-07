package com.ztt.common.exception;


import com.ztt.common.responsecode.ResultData;
import com.ztt.common.responsecode.ReturnCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CloudFeignException {

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {
        if (e instanceof FeignException feignException) {
            String message = feignException.getMessage();
            return ResultData.fail(ReturnCode.RC500.getCode(), ReturnCode.RC500.getMessage(), message);
        }
        return null;
    }
}
