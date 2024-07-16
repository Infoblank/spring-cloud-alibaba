package com.ztt.common.config.otherconfig;

import com.ztt.entity.LoginVal;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

@Slf4j
public class ContextTaskDecorator implements TaskDecorator {
    @Override
    public @NonNull Runnable decorate(@NonNull Runnable runnable) {
        // 获取父线程的
        LoginVal login = OauthContext.get();
        log.info("父线程信息：{}", login.toString());
        return () -> {
            try {
                OauthContext.set(login);
                runnable.run();
            } finally {
                // 线程结束清除信息，以防内存泄露
                OauthContext.clear();
            }
        };
    }
}
