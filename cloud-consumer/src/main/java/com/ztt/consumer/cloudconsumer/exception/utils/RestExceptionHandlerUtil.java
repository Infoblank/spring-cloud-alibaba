package com.ztt.consumer.cloudconsumer.exception.utils;


import com.ztt.consumer.cloudconsumer.responsecode.ResultData;
import com.ztt.consumer.cloudconsumer.responsecode.ReturnCode;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author zhangtingting
 */
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
        log.error("全局异常信息 ex={}", e.getLocalizedMessage());
        // 全局处理资源路径找不到的问题
        if (e instanceof NoHandlerFoundException exception) {
            String httpMethod = exception.getHttpMethod();
            String url = exception.getRequestURL();
            HashMap<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("httpMethod", httpMethod);
            errorMessage.put("url", url);
            return ResultData.fail(ReturnCode.RC404.getCode(), ReturnCode.RC404.getMessage(), errorMessage.toString());
        } else if (e instanceof HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
            String httpMethod = httpRequestMethodNotSupportedException.getMethod();
            String[] methods = httpRequestMethodNotSupportedException.getSupportedMethods();
            HashMap<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("当前请求方式", httpMethod);
            assert methods != null;
            errorMessage.put("实际需要的请求方式", Arrays.toString(methods));
            return ResultData.fail(ReturnCode.RC405.getCode(), ReturnCode.RC405.getMessage(), errorMessage.toString());
        }
        return ResultData.fail(ReturnCode.RC500.getCode(), e.getLocalizedMessage());
    }

    /**
     * 参数验证的全局处理器
     *
     * @param e 异常
     * @return 返回错误结果
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResultData<String> handleValidatedException(Exception e) {
        if (e instanceof BindException bindException) {
            log.error("BindException->{}", bindException.getMessage());
            return ResultData.fail(ReturnCode.PARAMETER_VALIDATION_FAILED.getCode(), ReturnCode.PARAMETER_VALIDATION_FAILED.getMessage(), bindException.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(",")));
        } else if (e instanceof HttpMessageNotReadableException httpMessageNotReadableException) {
            log.error("HttpMessageNotReadableException->{}", httpMessageNotReadableException.getMessage());
            return ResultData.fail(HttpStatus.BAD_REQUEST.value(), httpMessageNotReadableException.getLocalizedMessage());
        } else {
            return null;
        }
    }

    /*@ExceptionHandler(value = {SaTokenException.class})
    public ResultData<String> saTokenException(Exception e) {
        ResultData<String> data = new ResultData<>();
        if (e instanceof NotLoginException) {
            NotLoginException ex = (NotLoginException) e;
            log.info("未登录到系统,不允许获取系统资源.");
            return ResultData.fail(ReturnCode.NOT_INVALID_TOKEN.getCode(), ReturnCode.NOT_INVALID_TOKEN.getMessage(),
                    ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getLocalizedMessage());
        }
        return data;
    }*/

}
