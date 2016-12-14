package com.cleverm.smartpen.util.common;

import com.cleverm.smartpen.application.SmartPenApplication;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 10:10
 * Base on clever-m.com(JAVA Service)
 * Describe:
 *
 * want to see more :
 *      What is an efficient way to implement a singleton pattern in Java?
 *      http://stackoverflow.com/questions/70689/what-is-an-efficient-way-to-implement-a-singleton-pattern-in-java
 *
 * Version:1.0
 * Open source
 */
public enum  Application {
    INSTANCE;
    public  android.app.Application application(){
        return SmartPenApplication.getApplication();
    }
}
