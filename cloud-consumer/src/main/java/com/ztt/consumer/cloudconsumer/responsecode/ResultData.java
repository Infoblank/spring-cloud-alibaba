package com.ztt.consumer.cloudconsumer.responsecode;

public class ResultData<T> {

    private int status;
    private String message;
    private T data;
    private long operationTimestamp;

    /**
     * 唯一的响应id,贯彻整个的服务调用链
     */
    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getOperationTimestamp() {
        return operationTimestamp;
    }

    public void setOperationTimestamp(long operationTimestamp) {
        this.operationTimestamp = operationTimestamp;
    }

    public ResultData() {
        this.operationTimestamp = System.currentTimeMillis();
    }

    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(ReturnCode.RC100.getCode());
        resultData.setMessage(ReturnCode.RC100.getMessage());
        resultData.setData(data);
        return resultData;
    }

    /**
     *
     * @param code 返回的code
     * @param message 返回消息
     * @param <T> 消息
     * @return ResultData
     */
    public static <T> ResultData<T> fail(int code, String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(code);
        resultData.setMessage(message);
        return resultData;
    }

    public static <T> ResultData<T> fail(int code, String message,T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(code);
        resultData.setMessage(message);
        resultData.setData(data);
        return resultData;
    }

}
