package com.ztt.common.util;

import com.ztt.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CommonUtil {


    /**
     * 添加请求id和日志记录里面的id
     */
    public static void addRequestIdAndMDCId(HttpServletRequest request, HttpServletResponse response) {
        String requestId = RequestIdUtils.getRequestId();
        MDC.put(CommonConstant.REQUEST_ID, requestId);
        log.info("添加MDC日志请求ID:{}", requestId);
    }


    /**
     * 清除请求id和日志记录里面的id
     */
    public static void clearRequestIdAndMDCId(HttpServletRequest request, HttpServletResponse response) {
        RequestIdUtils.removeRequestId();
        MDC.clear();
        log.info("清除MDC日志请求ID");
    }
}
