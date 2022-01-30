package com.sa.cloudsatoken.response.exception.utils;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
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
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ValidationException;
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
        if (e instanceof NoHandlerFoundException){
            // 全局处理资源路径找不到的问题
            return  ResultData.fail(ReturnCode.RC404.getCode(),ReturnCode.RC404.getMessage(),e.getLocalizedMessage());
        }
        return ResultData.fail(ReturnCode.RC500.getCode(), e.getLocalizedMessage());
    }

    /**
     * 参数验证的全局处理器
     * @param e 异常
     * @return 返回错误结果
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResultData<String> handleValidatedException(Exception e) {
        ResultData<String> data = null;
        if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            log.error("BindException->{}", bindException.getMessage());
            data = ResultData.fail(ReturnCode.PARAMETER_VALIDATION_FAILED.getCode(),ReturnCode.PARAMETER_VALIDATION_FAILED.getMessage(), bindException.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(",")));
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            log.error("MethodArgumentNotValidException->{}", methodArgumentNotValidException.getMessage());
            data = ResultData.fail(HttpStatus.BAD_REQUEST.value(), methodArgumentNotValidException.
                    getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(",")));
        } else if (e instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException httpMessageNotReadableException = (HttpMessageNotReadableException) e;
            log.error("HttpMessageNotReadableException->{}", httpMessageNotReadableException.getMessage());
            data = ResultData.fail(HttpStatus.BAD_REQUEST.value(), httpMessageNotReadableException.getLocalizedMessage());
        }
        return data;
    }

    @ExceptionHandler(value = {SaTokenException.class})
    public ResultData<String> saTokenException(Exception e){
        ResultData<String> data = new ResultData<>();
        if (e instanceof NotLoginException){
            NotLoginException ex =  (NotLoginException)e;
            log.info("未登录到系统,不允许获取系统资源.");
            data = ResultData.fail(ReturnCode.NOT_INVALID_TOKEN.getCode(),ReturnCode.NOT_INVALID_TOKEN.getMessage(),ex.getLocalizedMessage());
        }
        return data;
    }

}
