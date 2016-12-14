package com.cleverm.smartpen.util.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cleverm.smartpen.service.ScreenLockListenService;
import com.cleverm.smartpen.service.penService;

/**
 * Created by xiong,An android project Engineer,on 17/5/2016.
 * Data:17/5/2016  下午 02:18
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class ScreenLockUtil {

    private ScreenLockListenService.ScreenLockListenServiceStub mStub;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mStub = (ScreenLockListenService.ScreenLockListenServiceStub) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static ScreenLockUtil INSTANCE = new ScreenLockUtil();

    private ScreenLockUtil() {

    }

    public static ScreenLockUtil getInstance() {
        return INSTANCE;
    }

    public void bindService(Context context) {
        context.bindService(new Intent(context, ScreenLockListenService.class), mConn, context.BIND_AUTO_CREATE);
    }

    public void unbindServcie(Context context) {
        context.unbindService(mConn);
    }


    public ScreenLockListenService.ScreenLockListenServiceStub getStub() {
        return mStub;
    }

}
