package com.cleverm.smartpen.bean.event;

/**
 * Created by xiong,An android project Engineer,on 18/5/2016.
 * Data:18/5/2016  下午 04:27
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnRobotShowEvent{

    private int penCode;
    private int result;

    public OnRobotShowEvent(int penCode,int result){
        this.penCode=penCode;
        this.result=result;
    }

    public int getPenCode() {
        return penCode;
    }

    public int getResult() {
        return result;
    }
}
