package com.ztt.common.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ClearId {
}
