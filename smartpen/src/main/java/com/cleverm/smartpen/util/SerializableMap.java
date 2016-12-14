package com.cleverm.smartpen.util;

import java.io.Serializable;

import java.util.Map;

/**
 * Created by xiong,An android project Engineer,on 2016/2/29.
 * Data:2016-02-29  17:23
 * Base on clever-m.com(JAVA Service)
 * Describe: 序列化的map
 * Version:1.0
 * Open source
 */
public class SerializableMap implements Serializable{

    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
