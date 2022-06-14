package com.ztt.consumer.cloudconsumer.aop;

import com.ztt.common.constant.CommonConstant;
import com.ztt.consumer.cloudconsumer.responsecode.ResultData;
import com.ztt.consumer.cloudconsumer.util.RequestIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Slf4j
@Component
public class ApiMessageAdvisor {

    @Around("execution(public * com.ztt.consumer.cloudconsumer.controller..*.*(..))")
    public Object invokeAPI(ProceedingJoinPoint pjp) {
        String apiName = getApiName(pjp);
        printRequestParam(apiName, pjp);
        Object returnValue = null;
        try {
            returnValue = pjp.proceed();
            handleRequestId(returnValue);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            // 打印响应参数
            printResponse(apiName, returnValue);
        }
        return returnValue;
    }

    /**
     * 获取当前接口对应的类名和方法名
     *
     * @param pjp 切点
     * @return apiName
     */
    private String getApiName(ProceedingJoinPoint pjp) {
        String apiClassName = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();
        return apiClassName.concat(":").concat(methodName);
    }

    /**
     * 获取RequestId
     * 优先从header头获取，如果没有则自己生成
     *
     * @return RequestId
     */
    private String getRequestId() {
        // 因为如果有网关，则一般会从网关传递过来，所以优先从header头获取
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null && StringUtils.hasText(attributes.getRequest().getHeader(CommonConstant.X_REQUEST_ID))) {
            HttpServletRequest request = attributes.getRequest();
            String requestId = request.getHeader(CommonConstant.X_REQUEST_ID);
            RequestIdUtils.generateRequestId(requestId);
            return requestId;
        }
        String requestId = RequestIdUtils.getRequestId();
        if (requestId != null) {
            return requestId;
        }
        RequestIdUtils.generateRequestId();
        return RequestIdUtils.getRequestId();
    }

    /**
     * 打印请求参数信息
     *
     * @param apiName 接口名称
     * @param pjp     切点
     */
    private void printRequestParam(String apiName, ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (log.isInfoEnabled() && args != null && args.length > 0) {
            for (Object o : args) {
                if (!(o instanceof HttpServletRequest) && !(o instanceof HttpServletResponse) && !(o instanceof CommonsMultipartFile)) {
                    log.info("@@{} started, request: {}", apiName, o);
                }
            }
        }
    }

    /**
     * 填充RequestId
     *
     * @param returnValue 返回参数
     */
    private void handleRequestId(Object returnValue) {
        if (returnValue instanceof ResultData<?> resultData) {
            resultData.setRequestId(RequestIdUtils.getRequestId());
        }
    }

    /**
     * 打印响应参数信息
     *
     * @param apiName     接口名称
     * @param returnValue 返回值
     */
    private void printResponse(String apiName, Object returnValue) {
        if (log.isInfoEnabled()) {
            log.info("@@{} done, response: {}", apiName, returnValue.toString());
        }
    }
}
