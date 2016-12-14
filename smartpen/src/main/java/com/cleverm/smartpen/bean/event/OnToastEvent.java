package com.cleverm.smartpen.bean.event;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 22/7/2016.
 * Data:22/7/2016  上午 11:42
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnToastEvent implements Serializable{

    private String message;

    public OnToastEvent(String message){
        this.message=message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "OnToastEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
