package com.cleverm.smartpen.pushtable;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Levy on 2015/5/25.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static File generateUpdatePackageFile(Context context, String key) {
        File cacheDir = context.getExternalCacheDir();
        File target = new File(cacheDir, "update_" + key + ".apk");
        return target;
    }

    public static String getSerialNumber(Context context) {
//        String serial = "";
//        try {
//            Class<?> c = Class.forName("android.os.SystemProperties");
//            Method get = c.getMethod("get", String.class);
//            serial = (String) get.invoke(c, "ro.serialno");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.d(TAG, "SerialNumber : " + serial);
//        return serial;


        String serial="";

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        serial =info.getMacAddress();
        Log.d(TAG, "MacAddress:" + serial);
        return serial;
    }

    public static String getMacAddress(){
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig","HWaddr");

        //如果返回的result == null，则说明网络不可取
        if(result==null){
            return "网络出错，请检查网络";
        }

        //对该行数据进行解析
        //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
        if(result.length()>0 && result.contains("HWaddr")==true){
            Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);
            Log.i("test","Mac:"+Mac+" Mac.length: "+Mac.length());

            if(Mac.length()>1){
                Mac = Mac.replaceAll(" ", "");
                result = "";
                String[] tmp = Mac.split(":");
                for(int i = 0;i<tmp.length;++i){
                    result +=tmp[i];
                }
            }
            Log.i("test",result+" result.length: "+result.length());
        }
        return result;
    }

    public static String callCmd(String cmd,String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader (is);

            //执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine ()) != null && line.contains(filter)== false) {
                //result += line;
                Log.i("test","line: "+line);
            }

            result = line;
            Log.i("test","result: "+result);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCpuSeriesNumber() {
        String result = "";
        try {
            Process p = Runtime.getRuntime().exec("cat proc/cpuinfo");
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(isr);
            while (result != null) {
                result = input.readLine();
                if (result != null && result.startsWith("Serial")) {
                    String[] split = result.split(":");
                    result = split[1].trim();
                    break;
                }
            }
            Log.d(TAG, "CPU SN : " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getProp(String propName) {
        try {
            Process process = Runtime.getRuntime().exec("getprop " + propName);
            if (process == null) {
                return null;
            }
            return new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void Unzip(String zipFile, String targetDir) {
        final int BUFFER = 4096; // buffer
        String strEntry; // entry
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();
                    File entryFile = new File(targetDir + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {

                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSilently(Closeable o) {
        if (o == null) {
            return;
        }
        try {
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialogSafely(Dialog dialog) {
        if (dialog == null) {
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void sendLocalBroadcast(Context context, Intent intent) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.sendBroadcast(intent);
    }

    public static void registerLocalBroadcast(Context context, BroadcastReceiver receiver,
                                              IntentFilter filter) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(receiver, filter);
    }

    public static void unRegisterLocalBroadcast(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.unregisterReceiver(receiver);
    }

    public static InetAddress getLocalHostIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(
            Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int intaddr = wifiInfo.getIpAddress();
        byte[] byteaddr = new byte[]{(byte) (intaddr & 0xff), (byte) (intaddr >> 8 & 0xff), (byte) (
            intaddr >> 16 & 0xff), (byte) (intaddr >> 24 & 0xff)};
        try {
            return InetAddress.getByAddress(byteaddr);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static String getMD5(String message) {
        String text = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(message.getBytes());
            byte[] digest = md5.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            text = bigInt.toString(16);
            while (text.length() < 32) {
                text = "0" + text;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * check whether IP address is legal
     *
     * @param address
     * String IP
     * @return boolean true - legal, false - illegal
     */
    private static final String IP_REGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                                           "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                                           + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                                           "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

    public static boolean isValidIp(String ip) {
        if (ip == null || StringUtils.isEmpty(ip)) {
            return false;
        }

        return ip.matches(IP_REGEX);
    }

    public static boolean isValidPort(String port) {
        if (port == null || StringUtils.isEmpty(port)) {
            return false;
        }

        try {
            int value = Integer.parseInt(port);
            if (value < 0 || value > 65535) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void waitWithoutInterrupt(Object object) {
        try {
            object.wait();
        } catch (InterruptedException e) {
            Log.w(TAG, "unexpected interrupt: " + object);
        }
    }

    public static boolean isNonnegativeInteger(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        return s.matches("^[1-9]\\d*|0$");
    }

    public static final String readFromFile(String path) {
        String json = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String buffer = reader.readLine();
            while (buffer != null) {
                json += buffer;
                buffer = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String searchFileByName(String dirPath, final String name) {
        File dir = new File(dirPath);
        if (dir.exists()) {
            File[] list = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.toLowerCase().startsWith(name);
                }
            });
            if (list != null && list.length > 0) {
                return list[0].getAbsolutePath();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
