package com.cleverm.smartpen.evnet;

import com.bleframe.library.bundle.onReadRemoteRssiBundle;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 4/7/2016.
 * Data:4/7/2016  上午 11:02
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnReadRSSIEvent implements Serializable {

    private com.bleframe.library.bundle.onReadRemoteRssiBundle mBundle;

    public OnReadRSSIEvent(onReadRemoteRssiBundle mBundle){
        this.mBundle=mBundle;
    }

    public onReadRemoteRssiBundle getBundle() {
        return mBundle;
    }

    public void setBundle(onReadRemoteRssiBundle bundle) {
        mBundle = bundle;
    }
}
