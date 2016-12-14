package com.cleverm.smartpen.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.ScanUtil;

/**
 * Created by 95 on 2015/12/21.
 */
public class penService extends Service implements WandAPI.OnScanListener, WandAPI.onConnectListener {

    public static final String DEMO = "DEMO";

    public static final String TAG = penService.class.getSimpleName();
    private WandAPI mWandAPI;
    private MessageListener messageListener;
    public static final String VIDEO_ACTIVITY_KEY = "video_activity_key";
    public static final String VIDEO_ACTIVITY_ISSEND = "video_activity_isSend";
    public static final String WEATHER = "weather";
    public static final String HEADLINE = "headline";
    public static final String HAPPY = "happy";
    public static final String SHOP = "shop";
    public static final String DISCOUNT = "discount";
    public static final String MAGAZINE = "magazine";
    public static final String VIDEO_ENTERTAINMENT = "video_entertainment";
    public static final String UN_KNOW = "un_know";
    public static final String GAME_ACTIVITY = "game_activity";
    public static final String GAME_URL = "game_url";

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }



    @Override
    public void onScan(int id) {
        ScanUtil.getInstance().onScan(penService.this,id,messageListener);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mWandAPI = new WandAPI(this, this);
        mWandAPI.onCreate();
        mWandAPI.setOnConnectListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new penServiceBind();
    }


    @Override
    public void onDestroy() {
        mWandAPI.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect() {
        QuickUtils.sendSMSToService(Constant.PEN_PULL_OUT_SMS);
    }

    public class penServiceBind extends Binder {
        public penService getService() {
            return penService.this;
        }
    }


    public interface MessageListener {
        void receiveData(int id, boolean isSend);
    }









}
