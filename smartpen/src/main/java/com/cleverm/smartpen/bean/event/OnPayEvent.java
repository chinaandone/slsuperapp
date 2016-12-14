package com.cleverm.smartpen.bean.event;

import com.cleverm.smartpen.app.SimpleAppActivity;

/**
 * Created by xiong,An android project Engineer,on 22/7/2016.
 * Data:22/7/2016  下午 02:03
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnPayEvent {

    private SimpleAppActivity.MessageCode code;

    public OnPayEvent(SimpleAppActivity.MessageCode code){
        this.code=code;
    }

    public SimpleAppActivity.MessageCode getCode() {
        return code;
    }

    public void setCode(SimpleAppActivity.MessageCode code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "OnPayEvent{" +
                "code=" + code +
                '}';
    }
}
