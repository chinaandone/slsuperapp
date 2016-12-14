package com.cleverm.smartpen.util.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.cleverm.smartpen.application.SmartPenApplication;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 10:47
 * Base on clever-m.com(JAVA Service)
 * Describe: 内存
 * Version:1.0
 * Open source
 */
public enum  RAM {

    INSTANCE;

    public final String information(){
        return String.valueOf(Math.round(convertToMb(availRAM())))+"MB"+"/"+String.valueOf(Math.round(convertToMb(totalRAM()))+"MB");
    }

    public final long totalRAM() {
        long totalMemory = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager =
                    (ActivityManager) Application.INSTANCE.application().getSystemService(Activity.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            totalMemory = mi.totalMem;
        } else {
            RandomAccessFile reader;
            String load;
            try {
                reader = new RandomAccessFile("/proc/meminfo", "r");
                load = reader.readLine().replaceAll("\\D+", "");
                totalMemory = (long) Integer.parseInt(load);
                reader.close();
            } catch (IOException e) {
            }
        }
        return totalMemory;
    }

    private final long availRAM(){
        ActivityManager am = (ActivityManager) SmartPenApplication.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }


    /**
     * 转化为KB
     *
     * @param valInBytes the val in bytes
     * @return the float
     */
    public final float convertToKb(long valInBytes) {
        return (float) valInBytes / 1024;
    }

    /**
     * 转化为MB
     *
     * @param valInBytes the val in bytes
     * @return the float
     */
    public final float convertToMb(long valInBytes) {
        return (float) valInBytes / (1024 * 1024);
    }

    /**
     * 转化为 GB
     *
     * @param valInBytes the val in bytes
     * @return the float
     */
    public final float convertToGb(long valInBytes) {
        return (float) valInBytes / (1024 * 1024 * 1024);
    }

    /**
     * 转化为 TB
     *
     * @param valInBytes the val in bytes
     * @return the float
     */
    public final float convertToTb(long valInBytes) {
        return (float) valInBytes / (1024 * 1024 * 1024 * 1024);
    }
}
