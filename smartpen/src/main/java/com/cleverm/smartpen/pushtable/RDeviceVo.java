package com.cleverm.smartpen.pushtable;



public class RDeviceVo {
    public static final int TYPE_CALLER = 1;
    public static final int TYPE_REPEATER = 2;
    public static final int TYPE_VIRTUAL = 3;

    private long deviceId;
    private int deviceType;
    private String name;
    private String description;
    private String parameter01;
    private String parameter02;

    public RDeviceVo() {

    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParameter01() {
        return parameter01;
    }

    public void setParameter01(String parameter01) {
        this.parameter01 = parameter01;
    }

    public String getParameter02() {
        return parameter02;
    }

    public void setParameter02(String parameter02) {
        this.parameter02 = parameter02;
    }

}