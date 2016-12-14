package com.cleverm.smartpen.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cleverm.smartpen.application.SmartPenApplication;

/**
 * Created by xiong,An android project Engineer,on 2016/3/3.
 * Data:2016-03-03  10:31
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class NetWorkUtil {

    /**
     * 获得网络连接是否可用
     * @return
     */
    public static boolean hasNetwork() {
        ConnectivityManager con = (ConnectivityManager) SmartPenApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo workinfo = con.getActiveNetworkInfo();
        if (workinfo == null || !workinfo.isAvailable()) {
            return false;
        }
        return true;
    }
}
