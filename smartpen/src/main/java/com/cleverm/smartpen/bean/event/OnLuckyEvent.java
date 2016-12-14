package com.cleverm.smartpen.bean.event;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 3/9/2016.
 * Data:3/9/2016  上午 10:13
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnLuckyEvent implements Serializable {


    public enum Type {
        defaultPrizeList,
        ServerPrizeList,
        defaultPrizeId,
        ServerPrizeId,

        gotPrizeSuccess,
        gotPrizeFail;

    }


    private Type type;
    private Object obj;

    public OnLuckyEvent(Type type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
