package com.cleverm.smartpen.bean.event;

import android.app.Activity;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 20/5/2016.
 * Data:20/5/2016  下午 12:35
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnDestoryActivityEvent implements Serializable{

    private Activity activity;

    public OnDestoryActivityEvent(Activity activity){
        this.activity=activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
