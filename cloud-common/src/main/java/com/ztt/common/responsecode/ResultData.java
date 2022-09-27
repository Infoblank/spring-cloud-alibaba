package com.ztt.common.responsecode;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@ToString
@Data
public class ResultData<T> {

    private int status;
    private String message;
    private T data;
    private long operationTimestamp;
    private String requestPath;
    /**
     * 唯一的响应id,贯彻整个的服务调用链
     */
    private String requestId;

    public ResultData() {
        this.operationTimestamp = System.currentTimeMillis();
        // 当前请求回去唯一的请求id
        // this.requestId = RequestIdUtils.getRequestId();
        this.requestId = MDC.get("traceId");
        this.requestPath = null;
    }

    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(ReturnCode.RC200.getCode());
        resultData.setMessage(ReturnCode.RC200.getMessage());
        resultData.setData(data);
        return resultData;
    }

    /**
     * @param code    返回的code
     * @param message 返回消息
     * @param <T>     消息
     * @return ResultData
     */
    public static <T> ResultData<T> fail(int code, String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(code);
        resultData.setMessage(message);
        return resultData;
    }

    public static <T> ResultData<T> fail(int code, String message, T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(code);
        resultData.setMessage(message);
        resultData.setData(data);
        return resultData;
    }

}
