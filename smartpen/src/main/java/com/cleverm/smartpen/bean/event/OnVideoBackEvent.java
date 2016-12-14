package com.cleverm.smartpen.bean.event;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 11/8/2016.
 * Data:11/8/2016  下午 04:58
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnVideoBackEvent implements Serializable {

    private int videoId;

    public OnVideoBackEvent(int videoId){
        this.videoId=videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getVideoId() {
        return videoId;
    }
}
