package com.cleverm.smartpen.util.common;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.util.QuickUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 10:09
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public enum IP {

    INSTANCE;

    /**
     * 是否有网络
     *
     * @return
     */
    public final boolean available() {
        if (Permission.INSTANCE.permission(Application.INSTANCE.application(), Manifest.permission.INTERNET)
                && Permission.INSTANCE.permission(Application.INSTANCE.application(), Manifest.permission.ACCESS_NETWORK_STATE)) {
            ConnectivityManager cm = (ConnectivityManager) Application.INSTANCE.application()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
        return false;
    }

    /**
     * WIFI是否开启
     *
     * @return
     */
    public final boolean wifiEnable() {
        boolean wifiState = false;
        WifiManager wifiManager = (WifiManager) Application.INSTANCE.application().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiState = wifiManager.isWifiEnabled();
        }
        return wifiState;
    }

    /**
     * 获取WIFI 信号
     *
     * @return
     */
    public String wifiRssi() {
        WifiManager manager = (WifiManager) SmartPenApplication.getApplication().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        if (info != null) {
            return String.valueOf(info.getRssi());
        } else {
            return String.valueOf(-1);
        }
    }


    /**
     * 获取公网IP
     *
     * @return
     */
    public final String global() {
        String result = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://ifcfg.me/ip");
            HttpResponse response;
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                if(result.length()>100){//如果返回的长度很长，直接告知为UNKNOW
                    result=null;
                }
                result =QuickUtils.replaceBlank(result);
            }
        } catch (Exception e) {

        }
        return CheckDeviceInfo.INSTANCE.checkValidData(result);
    }

    /**
     * 获取本地IP - V4
     *
     * @return
     */
    public final String v4() {
        String result = null;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = addr instanceof Inet4Address;
                        if (isIPv4) {
                            result = sAddr;
                        }
                    }
                }
            }
        } catch (SocketException e) {
        }
        return CheckDeviceInfo.INSTANCE.checkValidData(result);
    }

    /**
     * 获取本地IP - V6
     *
     * @return
     */
    public final String v6() {
        String result = null;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = addr instanceof Inet4Address;
                        if (!isIPv4) {
                            int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                            result = delim < 0 ? sAddr : sAddr.substring(0, delim);
                        }
                    }
                }
            }
        } catch (SocketException e) {
        }
        return CheckDeviceInfo.INSTANCE.checkValidData(result);
    }


    public final String mac() {
        String result = "00:00:00:00:00:00";
        if (Permission.INSTANCE.permission(Manifest.permission.ACCESS_WIFI_STATE)) {
            if (Build.VERSION.SDK_INT >= 23) {//android M
                // Hardware ID are restricted in Android 6+
                // https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id
                Enumeration<NetworkInterface> interfaces = null;
                try {
                    interfaces = NetworkInterface.getNetworkInterfaces();
                } catch (SocketException e) {

                }
                while (interfaces != null && interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();

                    byte[] addr = new byte[0];
                    try {
                        addr = networkInterface.getHardwareAddress();
                    } catch (SocketException e) {
                    }
                    if (addr == null || addr.length == 0) {
                        continue;
                    }

                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    String mac = buf.toString();
                    String wifiInterfaceName = "wlan0";
                    result = wifiInterfaceName.equals(networkInterface.getName()) ? mac : result;
                }
            } else {
                WifiManager wm = (WifiManager) Application.INSTANCE.application().getSystemService(Context.WIFI_SERVICE);
                result = wm.getConnectionInfo().getMacAddress();
            }
        }
        return CheckDeviceInfo.INSTANCE.checkValidData(result);
    }


}
