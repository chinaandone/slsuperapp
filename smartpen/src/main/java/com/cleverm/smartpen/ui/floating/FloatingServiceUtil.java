package com.cleverm.smartpen.ui.floating;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cleverm.smartpen.service.ScreenLockListenService;
import com.cleverm.smartpen.service.penService;
import com.cleverm.smartpen.util.service.PenUtil;

/**
 * Created by xiong,An android project Engineer,on 17/5/2016.
 * Data:17/5/2016  下午 02:18
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FloatingServiceUtil {

    private FloatingService mFloatingService;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFloatingService =  ((FloatingService.ChatHeadServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static FloatingServiceUtil INSTANCE = new FloatingServiceUtil();

    private FloatingServiceUtil() {

    }

    public static FloatingServiceUtil getInstance() {
        return INSTANCE;
    }

    public void bindService(Context context) {
        context.bindService(new Intent(context, FloatingService.class), mConn, context.BIND_AUTO_CREATE);
    }

    public void unbindServcie(Context context){
        context.unbindService(mConn);
    }

    public FloatingService getFloatingServcie() {
        if(mFloatingService!=null){
            return mFloatingService;
        }
        return null;
    }

}
