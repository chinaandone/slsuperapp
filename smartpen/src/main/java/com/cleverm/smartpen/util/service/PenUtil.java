package com.cleverm.smartpen.util.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cleverm.smartpen.service.penService;

/**
 * Created by xiong,An android project Engineer,on 17/5/2016.
 * Data:17/5/2016  下午 01:58
 * Base on clever-m.com(JAVA Service)
 * Describe: 有线笔帮助类
 * Version:1.0
 * Open source
 */
public class PenUtil {


    private penService mPenService;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPenService = ((penService.penServiceBind) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static PenUtil INSTANCE = new PenUtil();

    private PenUtil() {

    }

    public static PenUtil getInstance() {
        return INSTANCE;
    }

    public void bindService(Context context) {
        context.bindService(new Intent(context, penService.class), mConn, context.BIND_AUTO_CREATE);
    }

    public void unbindServcie(Context context){
        context.unbindService(mConn);
    }

    public penService getPenServcie() {
        if(mPenService!=null){
            return mPenService;
        }
        return null;
    }

}
