package com.cleverm.smartpen.util.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiong,An android project Engineer,on 25/8/2016.
 * Data:25/8/2016  下午 03:35
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public enum  Time {

    INSTANCE;

    public final String convertTimestamp(String timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(Long.parseLong(timestamp)));
    }
}
