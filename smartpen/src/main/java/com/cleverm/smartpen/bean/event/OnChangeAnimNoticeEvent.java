package com.cleverm.smartpen.bean.event;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 18/5/2016.
 * Data:18/5/2016  下午 01:34
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnChangeAnimNoticeEvent implements Serializable{

    private int imageResoure;
    private String text;
    private Status type;
    public enum Status {
        SHOW,HIDE
    }


    public OnChangeAnimNoticeEvent(int imageResoure, String text,Status type){
        this.imageResoure=imageResoure;
        this.text=text;
        this.type=type;
    }

    public int getImageResoure() {
        return imageResoure;
    }

    public String getText() {
        return text;
    }

    public Status getType() {
        return type;
    }
}
