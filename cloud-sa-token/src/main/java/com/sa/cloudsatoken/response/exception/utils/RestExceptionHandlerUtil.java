package com.sa.cloudsatoken.response.exception.utils;

import com.sa.cloudsatoken.response.ResultData;
import com.sa.cloudsatoken.response.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandlerUtil {
    /**
     * 默认全局异常处理。
     *
     * @param e the e
     * @return ResultData
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return ResultData.fail(ReturnCode.RC500.getCode(), e.getLocalizedMessage());
    }

    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResultData<String> handleValidatedException(Exception e) throws IOException {
        ResultData<String> data = null;
        if (e instanceof BindException ) {
            BindException bindException = (BindException)e;
            data = ResultData.fail(HttpStatus.BAD_REQUEST.value(), bindException.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining()));
        } else if (e instanceof MethodArgumentNotValidException ) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            data = ResultData.fail(HttpStatus.BAD_REQUEST.value(), methodArgumentNotValidException.
                    getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(",")));
        } else if (e instanceof HttpMessageNotReadableException ) {
            HttpMessageNotReadableException validationException = (HttpMessageNotReadableException)e;
            log.error("HttpMessageNotReadableException->{}",validationException.getMessage());
            data = ResultData.fail(HttpStatus.BAD_REQUEST.value(), validationException.getLocalizedMessage());
        }
        return data;
    }
}
