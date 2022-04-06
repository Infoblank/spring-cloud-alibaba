package com.sa.cloudsatoken;

public class ReturnsResult {

    private String head;

    private String data;

    private String orderRequest;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(String orderRequest) {
        this.orderRequest = orderRequest;
    }

    public ReturnsResult() {
    }

    @Override
    public String toString() {
        return "ReturnsResult{" +
                "head='" + head + '\'' +
                ", data='" + data + '\'' +
                ", orderRequest='" + orderRequest + '\'' +
                '}';
    }
}
