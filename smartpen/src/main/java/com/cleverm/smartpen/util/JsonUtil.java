package com.cleverm.smartpen.util;


import com.alibaba.fastjson.JSON;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by x on 2016/2/15.
 */
public class JsonUtil {

    public static final <T> List<T> parser(String result, Class<T> clazz) throws JSONException {
        JSONObject json = new JSONObject(result);
        String data = json.getString("data");
        List list = JSON.parseArray(data, clazz);
        return list;
    }

    public static final <T> T parserObject(String result,Class<T> clazz) throws JSONException{
        JSONObject json = new JSONObject(result);
        String data = json.getString("data");
        T object = JSON.parseObject(data, clazz);
        return object;
    }

}
