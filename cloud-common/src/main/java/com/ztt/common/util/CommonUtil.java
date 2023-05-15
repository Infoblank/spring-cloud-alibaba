package com.ztt.common.util;

import com.ztt.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Objects;

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
		String header = request.getHeader(CommonConstant.REQ_ID);
		if (Objects.isNull(header)) {
			header = RequestIdUtils.getRequestId();
		}
		MDC.put(CommonConstant.REQ_ID, header);
		String traceId = request.getHeader(CommonConstant.TRACE_ID);
		if (Objects.nonNull(traceId)) {
			MDC.put(CommonConstant.TRACE_ID, traceId);
		}
		String spanId = request.getHeader(CommonConstant.SPAN_ID);
		if (Objects.nonNull(spanId)) {
			MDC.put(CommonConstant.SPAN_ID, spanId);
		}
		log.info("添加MDC日志请求ID:{}", header);
	}


	/**
	 * 清除请求id和日志记录里面的id
	 */
	public static void clearRequestIdAndMDCId(HttpServletRequest request, HttpServletResponse response) {
		RequestIdUtils.removeRequestId();
		// clear的话会清除所以的MDC键,导致清除了sleuth的跟踪,所以只清除特定的键
		log.info("清除MDC日志请求ID:{}", MDC.get(CommonConstant.REQ_ID));
		log.info("MDC{}", MDC.getMDCAdapter().getCopyOfContextMap());
		//MDC.remove(CommonConstant.REQ_ID);
		MDC.clear();
	}
}
