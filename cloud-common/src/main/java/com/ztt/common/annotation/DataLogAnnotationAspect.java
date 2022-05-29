package com.ztt.common.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
@Slf4j
public class DataLogAnnotationAspect {
    @Pointcut("@annotation(com.ztt.common.annotation.aop.DataLog)")
    public void pointCut() {
    }

    @Before(value = "pointCut()")
    public void beforePointCut(JoinPoint joinPoint) {
        log.info("beforePointCut(),joinPoint={}", joinPoint.getSourceLocation());
        // 方法内发生异常直接就报错了,然后就结束了方法的调用
       // int i = 9 / 0;
    }

    /**
     * AfterThrowing 针对的是Pointcut()所在的方法,而不是其value所对应的方法。
     *
     * @param joinPoint 连接点
     * @param e         异常
     */
    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        log.info(joinPoint + ",发生了异常:" + e.getMessage());
    }

    @After(value = "pointCut()", argNames = "joinPoint")
    public void AfterPointCut(JoinPoint joinPoint) {
        log.info("AfterPointCut,joinPoint="+joinPoint);
    }
}
