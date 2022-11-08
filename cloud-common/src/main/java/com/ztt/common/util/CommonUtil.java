package com.ztt.common.util;

import com.ztt.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * spring注入bean的两种模式：full和lite
 * full：@Configuration+@bean ioc当中的是代理的bean
 * lite: 没有Configuration的bean ioc当中是原始的类型
 */
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
        // clear的话会清除所以的MDC键,导致清除了sleuth的跟踪,所以只清除特定的键
        //MDC.clear();
        MDC.remove(CommonConstant.REQUEST_ID);
        log.info("清除MDC日志请求ID");
    }

    public static String regularExpressionsExtractData(String message) {
        Pattern compile = Pattern.compile("\\[?<=\\[\\][^\\]]+");
        String group = compile.matcher(message).group();
        return group;
    }
}
