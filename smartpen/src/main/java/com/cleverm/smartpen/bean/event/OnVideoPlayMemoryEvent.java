package com.cleverm.smartpen.bean.event;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 16/5/2016.
 * Data:16/5/2016  下午 03:27
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnVideoPlayMemoryEvent implements Serializable {
    private boolean shouldSeek;

    public OnVideoPlayMemoryEvent(boolean shouldSeek){
        this.shouldSeek=shouldSeek;
    }

    public boolean isShouldSeek() {
        return shouldSeek;
    }

    public void setShouldSeek(boolean shouldSeek) {
        this.shouldSeek = shouldSeek;
    }
}
