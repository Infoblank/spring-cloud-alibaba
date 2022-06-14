package com.ztt.consumer.cloudconsumer.executor;

import lombok.NonNull;
import org.slf4j.MDC;

import java.util.concurrent.Executor;

/**
 * 解决线程异步场景下RequestId的打印问题
 */

public class ConsumerExecutor implements Executor {
    private final Executor executor;

    public ConsumerExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        final String requestId = MDC.get("REQUEST_ID");
        executor.execute(() -> {
            MDC.put("REQUEST_ID", requestId);
            try {
                command.run();
            } finally {
                MDC.remove("REQUEST_ID");
            }
        });
    }
}
