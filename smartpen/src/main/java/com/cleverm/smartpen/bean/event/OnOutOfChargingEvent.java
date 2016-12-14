package com.cleverm.smartpen.bean.event;

/**
 * Created by xiong,An android project Engineer,on 3/8/2016.
 * Data:3/8/2016  下午 02:05
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnOutOfChargingEvent {

    private boolean isCharging;

    public OnOutOfChargingEvent(boolean isCharging){
        this.isCharging=isCharging;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean isCharging) {
        this.isCharging = isCharging;
    }
}
