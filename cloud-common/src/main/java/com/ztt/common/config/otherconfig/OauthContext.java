package com.ztt.common.config.otherconfig;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.ztt.entity.LoginVal;

public class OauthContext {
    private static final TransmittableThreadLocal<LoginVal> loginValThreadLocal = new TransmittableThreadLocal<>();

    public static LoginVal get() {
        return loginValThreadLocal.get();
    }

    public static void set(LoginVal loginVal) {
        loginValThreadLocal.set(loginVal);
    }

    public static void clear() {
        loginValThreadLocal.remove();
    }
}
