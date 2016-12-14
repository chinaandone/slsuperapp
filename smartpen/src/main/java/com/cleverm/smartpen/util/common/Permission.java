package com.cleverm.smartpen.util.common;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 10:10
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public  enum  Permission {

    INSTANCE;

    /**
     * 是否有权限
     * @param context
     * @param permission
     * @return
     */
    public boolean permission(final Context context, final String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否有权限
     * @param permission
     * @return
     */
    public boolean permission(final String permission) {
        return Application.INSTANCE.application().checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
