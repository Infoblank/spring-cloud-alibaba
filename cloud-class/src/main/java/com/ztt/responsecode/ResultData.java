package com.ztt.responsecode;

import lombok.Data;
import lombok.ToString;

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

    private String dataType;

    public ResultData() {
        this.operationTimestamp = System.nanoTime();
    }

    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(ReturnCode.RC200.getCode());
        resultData.setMessage(ReturnCode.RC200.getMessage());
        resultData.setData(data);
        resultData.setDataType(getDataType(data));
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
        resultData.setDataType(getDataType(data));
        return resultData;
    }


    public static <T> String getDataType(T data) {
        return data == null ? "other" : data.getClass().getSimpleName();
    }

}
