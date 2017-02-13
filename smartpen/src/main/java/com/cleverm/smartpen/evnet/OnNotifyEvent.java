package com.cleverm.smartpen.evnet;

import com.bleframe.library.bundle.OnChangedBundle;

/**
 * Created by xiong,An android project Engineer,on 15/6/2016.
 * Data:15/6/2016  下午 03:36
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnNotifyEvent {

    private OnChangedBundle mBundle;

    public OnNotifyEvent(OnChangedBundle bundle){
        this.mBundle=bundle;
    }

    public OnChangedBundle getBundle() {
        return mBundle;
    }

    public void setBundle(OnChangedBundle bundle) {
        mBundle = bundle;
    }
    
}
