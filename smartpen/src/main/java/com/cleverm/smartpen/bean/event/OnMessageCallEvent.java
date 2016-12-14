package com.cleverm.smartpen.bean.event;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 16/5/2016.
 * Data:16/5/2016  下午 02:26
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnMessageCallEvent implements Serializable {

    private int id;
    private boolean isSend;

    public OnMessageCallEvent(int id, boolean isSend){
        this.id=id;
        this.isSend=isSend;
    }

    public int getId() {
        return id;
    }

    public boolean getIsSend() {
        return isSend;
    }

    @Override
    public String toString() {
        return "OnMessageCallEvent{" +
                "id=" + id +
                ", isSend=" + isSend +
                '}';
    }
}
