package com.cleverm.smartpen.evnet;

import android.bluetooth.BluetoothGatt;

/**
 * Created by xiong,An android project Engineer,on 15/6/2016.
 * Data:15/6/2016  下午 03:28
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OnFindServiceEvent {

    private BluetoothGatt mGatt;

    public OnFindServiceEvent(BluetoothGatt gatt){
        this.mGatt=gatt;
    }

    public BluetoothGatt getGatt() {
        return mGatt;
    }

    public void setGatt(BluetoothGatt gatt) {
        mGatt = gatt;
    }
}
