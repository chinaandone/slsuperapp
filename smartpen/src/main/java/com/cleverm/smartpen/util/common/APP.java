package com.cleverm.smartpen.util.common;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.application.SmartPenApplication;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 11:15
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public enum  APP {

    INSTANCE;

    /**
     * 系统版本
     *
     * @return
     */
    public final String versionOs() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取版本Name
     * @return
     */
    public final String versionName() {
        try {
            PackageInfo pi = Application.INSTANCE.application().getPackageManager().getPackageInfo(SmartPenApplication.getApplication().getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return SmartPenApplication.getApplication().getString(R.string.version_unknown);
        }
    }

    /**
     * 获取版本code
     * @return
     */
    public final int versionCode() {
        try {
            PackageInfo pi = Application.INSTANCE.application().getPackageManager().getPackageInfo(SmartPenApplication.getApplication().getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 00000;
        }
    }

    public final String appName() {
        String result;
        final PackageManager pm = Application.INSTANCE.application().getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo(Application.INSTANCE.application().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        result = ai != null ? (String) pm.getApplicationLabel(ai) : null;
        return CheckDeviceInfo.INSTANCE.checkValidData(result);
    }

    public final String packageName() {
        return CheckDeviceInfo.INSTANCE.checkValidData(Application.INSTANCE.application().getPackageName());
    }

    public final boolean debug(){
        ApplicationInfo info= Application.INSTANCE.application().getApplicationInfo();
        return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
    }


}
