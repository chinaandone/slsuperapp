package com.cleverm.smartpen.util.common;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.cleverm.smartpen.application.SmartPenApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 10:55
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public enum Phone {

    INSTANCE;

    public String audioValue() {
        AudioManager mAudioManager = (AudioManager) SmartPenApplication.getApplication().getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return String.valueOf(max);
    }

    public final String IMEI() {
        String result = null;
        if (Permission.INSTANCE.permission(Manifest.permission.READ_PHONE_STATE)) {
            result = ((TelephonyManager) Application.INSTANCE.application().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        if(result==null){
            result= Settings.Secure.getString(Application.INSTANCE.application().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return CheckDeviceInfo.INSTANCE.checkValidData(result);
    }

    public String padMode() {
        Build build = new Build();
        String model = build.MODEL;
        return model;
    }

    /**
     * 主板编号
     * @return
     */
    public final String board() {
        return CheckDeviceInfo.INSTANCE.checkValidData(Build.BOARD);
    }

    public String timeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }

    public final int batteryPercentage() {
        int percentage = 0;
        Intent batteryStatus = batteryStatusIntent();
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            percentage = (int) ((level / (float) scale) * 100);
        }

        return percentage;
    }

    private Intent batteryStatusIntent() {
        IntentFilter batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        return Application.INSTANCE.application().registerReceiver(null, batFilter);
    }

    public final boolean deviceCharging() {
        Intent batteryStatus = batteryStatusIntent();
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL;
    }

    /**
     * 号段http://www.jihaoba.com/tools/haoduan/
     * @param phone
     * @return
     */
    public final boolean isMobilePhone(String phone){
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[01678])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }




}
