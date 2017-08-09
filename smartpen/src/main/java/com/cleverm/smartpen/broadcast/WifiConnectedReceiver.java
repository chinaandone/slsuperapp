package com.cleverm.smartpen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cleverm.smartpen.bean.event.OnNetWorkEvent;
import com.cleverm.smartpen.util.StatisticsUtil;

import org.greenrobot.eventbus.EventBus;

import static com.cleverm.smartpen.util.QuickUtils.doStatistic;

public class WifiConnectedReceiver extends BroadcastReceiver {
    public WifiConnectedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetInfo != null) {
                // 判断是wifi连接
                if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.d(WifiConnectedReceiver.class.getCanonicalName(), "wifi连接:" + activeNetInfo.getState());
                    // 连接成功
                    Log.d(WifiConnectedReceiver.class.getCanonicalName(),"wifi status:"+activeNetInfo.getState());
                    if (NetworkInfo.State.CONNECTED == activeNetInfo.getState()) {
                        EventBus.getDefault().postSticky(new OnNetWorkEvent("",1));
                        return;
                    }
                    if(!activeNetInfo.isAvailable()){
                        doStatistic(StatisticsUtil.WIFI_UNAVAILABLE, StatisticsUtil.WIFI_UNAVAILABLE_DES);
                        return;
                    }
//          else if (activeNetInfo.getType() == 1) {
//          if (NetworkInfo.State.DISCONNECTING == activeNetInfo.getState()) {
//              // 未连接成功
//              uploadService(context, intent, true);
//          }
//          }
                }
            }
            //网络无效,wifi连接关闭,记录统计
            doStatistic(StatisticsUtil.WIFI_CLOSED, StatisticsUtil.WIFI_CLOSED_DES);
        }
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
