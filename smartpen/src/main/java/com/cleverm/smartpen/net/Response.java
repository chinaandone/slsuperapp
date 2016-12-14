package com.cleverm.smartpen.net;

import com.alibaba.fastjson.JSON;

public class Response<T> extends Message implements Status {
    private int code;
    private transient T result;

    public Response() {
    }

    public Response(String message, int code, T result) {
        this.setBody(message);
        this.code = code;
        this.result = result;
    }

    public Response(T result) {
        this.code = RETURN_SUCCESS;
        this.result = result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Response successResult() {
        return new Response("OK");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Response errorResult(String message) {
        return new Response(message, RETURN_ERROR, null);
    }

    public static Response<?> parse(String text) {
        try {
            return JSON.parseObject(text, Response.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ACK;
    }

    public String getMessage() {
        return getBody();
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean success() {
        return code == RETURN_SUCCESS;
    }
}
