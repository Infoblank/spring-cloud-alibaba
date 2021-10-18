package com.ztt.common.response;

import java.io.Serializable;

/**
 * @author ZTT
 */

public class CloudResponseEntity<T> implements Serializable {
    private boolean status;
    private String message;
    private int code;
    private T data;
    private long timestamp;

    public CloudResponseEntity() {
        this.timestamp = System.currentTimeMillis();
    }

    public CloudResponseEntity(boolean status, String message, int code, T data) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> CloudResponseEntity<T> success(T data) {
        CloudResponseEntity<T> cloudResponseEntity = new CloudResponseEntity<>();
        cloudResponseEntity.setStatus(true);
        cloudResponseEntity.setCode(ReturnCode.RC100.getCode());
        cloudResponseEntity.setMessage(ReturnCode.RC100.getMessage());
        cloudResponseEntity.setData(data);
        return cloudResponseEntity;
    }

    public static <T> CloudResponseEntity<T> fail(int code, String message) {
        CloudResponseEntity<T> cloudResponseEntity = new CloudResponseEntity<>();
        cloudResponseEntity.setStatus(false);
        cloudResponseEntity.setCode(code);
        cloudResponseEntity.setMessage(message);
        return cloudResponseEntity;
    }
}
