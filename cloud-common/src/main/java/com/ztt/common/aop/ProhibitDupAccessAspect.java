package com.ztt.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Enumeration;

@Aspect
@Component
public class ProhibitDupAccessAspect {

	private final static Logger logger = LoggerFactory.getLogger(ProhibitDupAccessAspect.class);

	@Pointcut("@annotation(com.ztt.common.aop.ProhibitDupAccess)")
	public void cut() {
	}


	@Around("cut()")
	public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request;
		if (attributes != null) {
			request = attributes.getRequest();
			logger.info("<=====================================");
			logger.info("请求来源：" + request.getRemoteAddr());
			logger.info("请求路径：" + request.getRequestURL().toString());
			logger.info("请求方式：" + request.getMethod());
			logger.info("响应方法：" + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
			logger.info("请求参数：" + Arrays.toString(pjp.getArgs()));
			logger.info("--------------------------------------");
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = headerNames.nextElement();
				String value = request.getHeader(key);
				logger.info(key + "：" + value);
			}
			logger.info("=====================================>");
		}

		Object result = pjp.proceed();
		logger.info("响应结果为{}", result);
		return result;
	}
}
