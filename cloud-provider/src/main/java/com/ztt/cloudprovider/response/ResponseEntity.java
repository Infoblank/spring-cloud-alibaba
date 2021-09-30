package com.ztt.cloudprovider.response;

import java.io.Serializable;

/**
 * @author ZTT
 */

public class ResponseEntity<T> implements Serializable {
    private boolean status;
    private String message;
    private int code;
    private T data;
    private long timestamp;

    public ResponseEntity() {
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseEntity(boolean status, String message, int code, T data) {
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

    public static <T> ResponseEntity<T> success(T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setStatus(true);
        responseEntity.setCode(ReturnCode.RC100.getCode());
        responseEntity.setMessage(ReturnCode.RC100.getMessage());
        responseEntity.setData(data);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> fail(int code, String message) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setStatus(false);
        responseEntity.setCode(code);
        responseEntity.setMessage(message);
        return responseEntity;
    }
}
