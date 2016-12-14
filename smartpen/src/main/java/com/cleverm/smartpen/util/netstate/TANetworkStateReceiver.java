package com.cleverm.smartpen.util.netstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by xiong,An android project Engineer,on 25/7/2016.
 * Data:25/7/2016  上午 09:38
 * Base on clever-m.com(JAVA Service)
 * @Description 是一个检测网络状态改变的，需要配置 <receiver
 *              android:name="com.cleverm.smartpen.util.netstate.TANetworkStateReceiver" >
 *              <intent-filter> <action
 *              android:name="android.net.conn.CONNECTIVITY_CHANGE" /> <action
 *              android:name="android.gzcpc.conn.CONNECTIVITY_CHANGE" />
 *              </intent-filter> </receiver>
 *
 *              需要开启权限 <uses-permission
 *              android:name="android.permission.CHANGE_NETWORK_STATE" />
 *              <uses-permission
 *              android:name="android.permission.CHANGE_WIFI_STATE" />
 *              <uses-permission
 *              android:name="android.permission.ACCESS_NETWORK_STATE" />
 *              <uses-permission
 *              android:name="android.permission.ACCESS_WIFI_STATE" />
 * Version:1.0
 * Open source
 */
public class TANetworkStateReceiver extends BroadcastReceiver {


    private static final String TAG="TANetworkStateReceiver";

    private static Boolean networkAvailable = false;
    private static TANetWorkUtil.NetType netType;
    private static ArrayList<TANetChangeObserver> netChangeObserverArrayList = new ArrayList<TANetChangeObserver>();
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public final static String TA_ANDROID_NET_CHANGE_ACTION = "ta.android.net.conn.CONNECTIVITY_CHANGE";
    private static BroadcastReceiver receiver;

    private static BroadcastReceiver getReceiver() {
        if (receiver == null) {
            receiver = new TANetworkStateReceiver();
        }
        return receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        receiver = TANetworkStateReceiver.this;
        Log.i(TAG,"网络状态广播监听事件");
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)
                || intent.getAction().equalsIgnoreCase(
                TA_ANDROID_NET_CHANGE_ACTION)) {
            Log.i(TAG, "网络状态改变.");
            if (!TANetWorkUtil.isNetworkAvailable(context)) {
                Log.i(TAG, "没有网络连接.");
                networkAvailable = false;
            } else {
                Log.i(TAG, "网络连接成功.");
                netType = TANetWorkUtil.getAPNType(context);
                networkAvailable = true;
            }
            notifyObserver();
        }
    }

    /**
     * 注册网络状态广播(代码注册)
     *
     * @param mContext
     */
    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(TA_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mContext.getApplicationContext()
                .registerReceiver(getReceiver(), filter);
    }

    /**
     * 检查网络状态
     *
     * @param mContext
     */
    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(TA_ANDROID_NET_CHANGE_ACTION);
        mContext.sendBroadcast(intent);
    }

    /**
     * 注销网络状态广播
     *
     * @param mContext
     */
    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (receiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(receiver);
            } catch (Exception e) {
                // TODO: handle exception
                Log.d(TAG, e.getMessage());
            }
        }

    }

    /**
     * 获取当前网络状态，true为网络连接成功，否则网络连接失败
     *
     * @return
     */
    public static Boolean isNetworkAvailable() {
        return networkAvailable;
    }

    public static TANetWorkUtil.NetType getAPNType() {
        return netType;
    }

    private void notifyObserver() {

        for (int i = 0; i < netChangeObserverArrayList.size(); i++) {
            TANetChangeObserver observer = netChangeObserverArrayList.get(i);
            if (observer != null) {
                if (isNetworkAvailable()) {
                    observer.onConnect(netType);
                } else {
                    observer.onDisConnect();
                }
            }
        }

    }

    /**
     * 注册网络连接观察者
     *
     * @param  observer observerKey
     */
    public static void registerObserver(TANetChangeObserver observer) {
        if (netChangeObserverArrayList == null) {
            netChangeObserverArrayList = new ArrayList<TANetChangeObserver>();
        }
        netChangeObserverArrayList.add(observer);
    }

    /**
     * 注销网络连接观察者
     *
     * @param observer observerKey
     */
    public static void removeRegisterObserver(TANetChangeObserver observer) {
        if (netChangeObserverArrayList != null) {
            netChangeObserverArrayList.remove(observer);
        }
    }
}
