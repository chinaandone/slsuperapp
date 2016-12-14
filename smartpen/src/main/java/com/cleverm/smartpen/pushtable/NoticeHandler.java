package com.cleverm.smartpen.pushtable;

import com.alibaba.fastjson.JSON;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class NoticeHandler<Tin, Tout> {

    private String noticeType;
    private Class<Tin> clazz;
    private Type type;

    @SuppressWarnings("unchecked")
    public NoticeHandler(String noticeType) {
        ArgumentUtil.checkNotNull(noticeType, "noticeType");
        this.noticeType = noticeType;
        this.clazz = getGenericType(0);
    }

    public NoticeHandler(String noticeType, Class<Tin> clazz) {
        ArgumentUtil.checkNotNull(noticeType, "noticeType");
        this.noticeType = noticeType;
        this.clazz = clazz;
    }

    public NoticeHandler(String noticeType, Type type) {
        ArgumentUtil.checkNotNull(noticeType, "noticeType");
        this.noticeType = noticeType;
        this.type = type;
    }

    @SuppressWarnings("rawtypes")
    public Class getGenericType(int index) {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public abstract Response<Tout> onSuccess(Headers headers, Tin in);

    public abstract void onError(int code, String message);

    public Response<Tout> handle(Headers headers, String body, String serverDataParseErrorTips) {
        Tin inParam = null;
        try {
            if (!StringUtils.isNullOrEmpty(body)) {
                if (clazz != null) {
                    inParam = parseByClass(body);
                } else if (type != null) {
                    inParam = parseByType(body);
                } else {
                    inParam = null;
                }
            }
            return this.onSuccess(headers, inParam);
        } catch (Exception exception) {
            this.onError(Response.RETURN_ERROR, serverDataParseErrorTips);
        }
        return null;
    }

    protected Tin parseByClass(String body) {
        return JSON.parseObject(body, clazz);
    }

    protected Tin parseByType(String body) {
        return JSON.parseObject(body, type);
    }
}
