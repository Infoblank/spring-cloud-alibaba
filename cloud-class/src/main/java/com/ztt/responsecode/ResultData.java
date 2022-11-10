package com.ztt.responsecode;

import lombok.*;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultData {

    private int status;
    private String message;

    private Object data;

    private long operationTimestamp;

    private String requestPath;
    /**
     * 唯一的响应id,贯彻整个的服务调用链
     */
    private String requestId;

    private String dataType;


    public static ResultData success(Object data) {
        ResultDataBuilder dataBuilder = ResultData.builder().status(ReturnCode.RC200.getCode()).message(ReturnCode.RC200.getMessage()).data(data).dataType(getDataType(data)).operationTimestamp(System.nanoTime());
        return dataBuilder.build();
    }

    /**
     * @param code    返回的code
     * @param message 返回消息
     * @param <T>     消息
     * @return ResultData
     */
    public static ResultData fail(int code, String message) {
        return ResultData.builder().status(code).message(message).operationTimestamp(System.nanoTime()).build();

    }

    public static ResultData fail(int code, String message, Object data) {
        return ResultData.builder().status(code).message(message).data(data).dataType(getDataType(data)).operationTimestamp(System.nanoTime()).build();
    }


    public static <T> String getDataType(T data) {
        return data == null ? "other" : data.getClass().getSimpleName();
    }

}
