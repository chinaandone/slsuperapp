package com.cleverm.smartpen.util.common;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 09:41
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public enum  CheckDeviceInfo {

    INSTANCE;

    public String checkValidData(final String data) {
        String tempData = data;
        if (tempData == null || tempData.length() == 0) {
            tempData = DeviceText.NOT_FOUND_VAL;
        }
        return tempData;
    }


}
