package com.ztt.common.aop;

import com.ztt.common.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 用于清除子线程从父线程继承过来的id,如果是同步请求基本上不会用到,
 * 在异步请求的时候使用注解
 */
@Component
@Aspect
@Slf4j
public class ClearIdAspect {

    @Pointcut("@annotation(com.ztt.common.aop.ClearId)")
    public void clearId() {
    }


    @Before("clearId()")
    public void before() {
        CommonUtil.addRequestIdAndMDCId(null, null);
        log.info("clearId注解从父线程获取请求Id");
    }

    @After("clearId()")
    public void after() {
        CommonUtil.clearRequestIdAndMDCId(null, null);
        log.info("clearId注解清除子线程的请求Id");
    }

    /**
     * 此处也可以清除掉子线程的id
     *
     * @param e 参数
     * @throws Exception 异常
     */
    @AfterThrowing(pointcut = "clearId()", throwing = "e")
    public void afterThrowing(Exception e) throws Exception {
        log.error("发生了错误:{}ClearId注解清除子线程id", e.getMessage());
        throw e;
    }

}
