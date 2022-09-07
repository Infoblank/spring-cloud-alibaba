package com.ztt.common.exception.utils;


import com.ztt.common.responsecode.ResultData;
import com.ztt.common.responsecode.ReturnCode;
import lombok.NonNull;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
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
        String localizedMessage = e.getLocalizedMessage();
        if (Objects.isNull(localizedMessage)) {
            localizedMessage = e.getCause().getLocalizedMessage();
        }
        log.error("全局异常信息 ex={}", localizedMessage);
        // 全局处理资源路径找不到的问题
        if (e instanceof NoHandlerFoundException exception) {
            String httpMethod = exception.getHttpMethod();
            String url = exception.getRequestURL();
            HashMap<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("httpMethod", httpMethod);
            errorMessage.put("url", url);
            if (url.contains("js") || url.contains("css") || url.contains("html")) {
                return ResultData.fail(ReturnCode.RESOURCES_ACCESS_LIMITATION.getCode(), ReturnCode.RESOURCES_ACCESS_LIMITATION.getMessage(), errorMessage.toString());
            }
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
        ArrayList<String> putException = putException(e);
        return ResultData.fail(ReturnCode.RC500.getCode(), ReturnCode.RC500.getMessage(), putException.toString());
    }

    /**
     * 参数验证的全局处理器
     *
     * @param e 异常
     * @return 返回错误结果
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
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


    public ArrayList<String> putException(@NonNull Exception exception) {
        StackTraceElement[] trace = exception.getStackTrace();
        ArrayList<String> list = new ArrayList<>();
        for (StackTraceElement element : trace) {
            String className = element.getClassName();
            // 暂时只得到代码本身的错误
            if (className.contains("com.ztt")) {
                int lineNumber = element.getLineNumber();
                String methodName = element.getMethodName();
                String fileName = element.getFileName();
                String name = element.getClassName();
                assert fileName != null;
                if (fileName.contains(".java")) {
                    String message = "在文件:" + name + "." + fileName.split("\\.")[1] + "的方法" + methodName + "第" + lineNumber + "行发生了{" + exception.getMessage() + "}错误。";
                    list.add(message);
                }
            }
        }
        return list;
    }
}
