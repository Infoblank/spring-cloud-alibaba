package com.ztt.common.annotation.method;

import com.ztt.common.annotation.aop.DataLog;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AopAnnotationDataLogMethod {

    // 测试DataLog注解的
    @DataLog
    public void TestDataLog() {
        log.info("测试注解DataLog");
        try {
            int i = 9/0;
        } catch (Exception e) {
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
    }
}
