package com.cleverm.smartpen.pushtable;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message {
    private Map<String, Object> headers = new HashMap<String, Object>();
    private MessageType messageType;
    private String body;
    private transient Headers headerWapper;

    public static Builder create() {
        return new Builder();
    }

    public static Message parse(String text) throws InvalidMessageException {
        try {
            return JSON.parseObject(text, Message.class);
        } catch (Exception e) {
            // TODO Auto-generated method stub
            throw new InvalidMessageException();
        }
    }

    public static String toRawString(Message message) {
        return JSON.toJSONString(message);
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean is(MessageType messageType) {
        return this.messageType == messageType;
    }

    public Headers headerWapper() {
        if (null == headerWapper) {
            headerWapper = new Headers(headers);
        }
        return headerWapper;
    }

    public static class Builder {
        private MessageType messageType = MessageType.NOTIFICATION;
        private Headers headers = new Headers();
        private String body;

        public Builder header(String headerName, Object headerValue) {
            headers.set(headerName, headerValue);
            return this;
        }

        public Builder boardNumber(String boardNumber) {
            headers.setBoardNumber(boardNumber);
            return this;
        }

        public Builder tags(List<String> tags) {
            headers.setTags(tags);
            return this;
        }

        public Builder body(String data) {
            this.body = data;
            return this;
        }

        public Builder messageType(MessageType type) {
            this.messageType = type;
            return this;
        }

        public Builder json(Object data) {
            this.body = JSON.toJSONString(data);
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.setMessageType(messageType);
            message.setHeaders(headers.getHeaders());
            message.setBody(body);
            return message;
        }
    }

}
