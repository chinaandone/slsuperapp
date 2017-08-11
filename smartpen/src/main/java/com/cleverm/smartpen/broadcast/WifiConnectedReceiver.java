package com.cleverm.smartpen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cleverm.smartpen.bean.event.OnNetWorkEvent;
import com.cleverm.smartpen.util.StatisticsUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.cleverm.smartpen.util.QuickUtils.doStatistic;

public class WifiConnectedReceiver extends BroadcastReceiver {

    private Handler handler;
    public WifiConnectedReceiver() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String ssid = msg.getData().getString("SSID");
                doStatistic(StatisticsUtil.WIFI_UNAVAILABLE, StatisticsUtil.WIFI_UNAVAILABLE_DES+",SSID:"+ssid);
                super.handleMessage(msg);
            }
        };

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
                    if(!activeNetInfo.isAvailable()){
                        doStatistic(StatisticsUtil.WIFI_UNAVAILABLE, StatisticsUtil.WIFI_UNAVAILABLE_DES);
                        return;
                    }
                    // 连接成功
                    Log.d(WifiConnectedReceiver.class.getCanonicalName(),"wifi status:"+activeNetInfo.getState());
                    if (NetworkInfo.State.CONNECTED == activeNetInfo.getState()) {
                        //若不能访问网络记录事件
                        new Thread(new CheckNetWorkRunnable(activeNetInfo.getExtraInfo())).start();
//                        if(isConnectNetwork()) {
                            EventBus.getDefault().postSticky(new OnNetWorkEvent("", 1));
//                        }else{
//                            doStatistic(StatisticsUtil.WIFI_UNAVAILABLE, StatisticsUtil.WIFI_UNAVAILABLE_DES+",SSID:"+activeNetInfo.getExtraInfo());
//                        }
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

    public class CheckNetWorkRunnable implements Runnable{
        private String SSID;
        public CheckNetWorkRunnable(String ssid){
            SSID = ssid;
        }
        @Override
        public void run() {
            if(!isConnectNetwork()){
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("SSID",SSID);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
//            else{
//                Message msg = new Message();
//                Bundle bundle = new Bundle();
//                bundle.putString("SSID",SSID+"OK");
//                msg.setData(bundle);
//                handler.sendMessage(msg);
//            }
        }
    }

    //检测是否能够上网，
    public boolean isConnectNetwork() {
        // HttpClient连接对象
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        // HttpGet连接对象使用get方式请求
        HttpGet myget = new HttpGet("http://www.baidu.com/");
        HttpResponse response = null;

        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
//            tools.debugger("请求开始时间", df.format(new Date()));
            response = client.execute(myget);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
//            tools.debugger("读取数据超时", "读取数据超时");
//            tools.debugger("超时时间", df.format(new Date()));
            return false;
        } catch (IOException e) {
            e.printStackTrace();
//            tools.debugger("io流出错", "io流出错");
//            tools.debugger("io流出错时间", df.format(new Date()));
            return false;
        }
        Log.d("响应值为", response.getStatusLine().getStatusCode() + "");
//        tools.debugger("响应时间", df.format(new Date()));
        // 返回值为200，即为服务器成功响应了请求,其余的，则为失败
        if (response.getStatusLine().getStatusCode() == 200) {
//            tools.debugger("请求数据成功", "请求数据成功");
            return true;
        }
        return false;
    }
}
