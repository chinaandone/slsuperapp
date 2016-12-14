package com.cleverm.smartpen.util.common;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 09:48
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class EasyCommonInfo {

    private static class EasyDeviceInfoHolder {
        private static final EasyCommonInfo INSTANCE = new EasyCommonInfo();
    }

    private EasyCommonInfo() {
    }

    public static final EasyCommonInfo getInstance() {
        return EasyDeviceInfoHolder.INSTANCE;
    }


    public com.cleverm.smartpen.util.common.Application APPLICATION() {
        return com.cleverm.smartpen.util.common.Application.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.Permission PERMISSION() {
        return com.cleverm.smartpen.util.common.Permission.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.IP IP() {
        return com.cleverm.smartpen.util.common.IP.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.CheckDeviceInfo CHECK() {
        return com.cleverm.smartpen.util.common.CheckDeviceInfo.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.Phone PHONE() {
        return com.cleverm.smartpen.util.common.Phone.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.APP APP() {
        return com.cleverm.smartpen.util.common.APP.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.RAM RAM() {
        return com.cleverm.smartpen.util.common.RAM.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.ROM ROM() {
        return com.cleverm.smartpen.util.common.ROM.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.FileSteam FILE() {
        return com.cleverm.smartpen.util.common.FileSteam.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.Time TIME() {
        return com.cleverm.smartpen.util.common.Time.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.Test TEST() {
        return com.cleverm.smartpen.util.common.Test.INSTANCE;
    }

    public com.cleverm.smartpen.util.common.Empty EMPTY() {
        return com.cleverm.smartpen.util.common.Empty.INSTANCE;
    }

}
