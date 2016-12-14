package com.cleverm.smartpen.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.app.SelectTableActivity;
import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.bean.DiscountInfo;
import com.cleverm.smartpen.net.InfoSendSMSVo;
import com.cleverm.smartpen.net.RequestNet;
import com.cleverm.smartpen.pushtable.UpdateTableHandler;
import com.cleverm.smartpen.ui.windows.engineer.EngineerUtil;
import com.cleverm.smartpen.util.cache.FileRememberUtil;
import com.cleverm.smartpen.util.common.EasyCommonInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiong,An android project Engineer,on 2016/2/19.
 * Data:2016-02-19  11:43
 * Base on clever-m.com(JAVA Service)
 * Describe: 快速帮助辅助类
 * Version:1.0
 * Open source
 */
public class QuickUtils {


    private static final String image1 = "http://img3.imgtn.bdimg.com/it/u=1167659344,71834717&fm=21&gp=0.jpg";
    private static final String image2 = "http://www.winwin-hotel.com/uploadfile/2012/0324/20120324112535452.jpg";
    private static final String image3 = "http://pic27.nipic.com/20130321/12176395_170138349154_2.jpg";
    private static final String image4 = "http://hansong.zshl.com/datashow/picb/2009/p2009271112833343.jpg";


    /**
     * 隐藏 navigation 和 status
     * Hide both the navigation bar and the status bar.
     * SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
     * a general rule, you should design your app to hide the status bar whenever you
     * hide the navigation bar.
     *
     * @param activity
     */
    public static void hideNavigation(Activity activity) {
        View decorView = activity.getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static void hideTitel(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 隐藏运行Android 4.0以上系统的平板的屏幕下方的状态栏
     * onCreate调用
     */
    public static void hideHighApiBottomStatusBar() {
        try {
            String ProcID = "79";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                ProcID = "42"; // ICS
            // 需要root 权限
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "service call activity " + ProcID + " s16 com.android.systemui"}); // WAS
            proc.waitFor();
        } catch (Exception ex) {

        }
    }

    /**
     * 恢复运行Android 4.0以上系统的平板的屏幕下方的状态栏
     * onDestory调用
     */
    public static void showHighApiBottomStatusBar() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"am", "startservice", "-n", "com.android.systemui/.SystemUIService"});
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static ArrayList<String> getTestImage() {
        ArrayList<String> images = new ArrayList<String>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image1);
        images.add(image3);
        images.add(image4);
        return images;
    }

    public static ArrayList<String> getDiscountImage(List<DiscountInfo> lists) {
        ArrayList<String> images = new ArrayList<String>();
        for (int i = 0; i < lists.size(); i++) {
            if(lists.get(i).getQiniuPath()!=null&&!lists.get(i).getQiniuPath().equals("")){
                images.add(spliceQiniuUrl(lists.get(i).getQiniuPath()));
            }else{
                images.add(spliceOriUrl(lists.get(i).getPictruePath()));
            }
        }
        return images;
    }


    public static void toast(String message) {
        Toast.makeText(SmartPenApplication.getApplication(), message, Toast.LENGTH_LONG).show();
    }

    public static void threadToast(Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast(message);
            }
        });
    }


    public static void log(String message) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        if(SmartPenApplication.isDebug){
            Log.i(tag, message);
        }
        if(RememberUtil.getBoolean(Constant.HIDDEN_DOOR_ENGINEER_KEY,false)){
            String time = EasyCommonInfo.getInstance().TIME().convertTimestamp(System.currentTimeMillis() + "");
            final String text = time+"  "+tag+log_splite+message;
            EngineerUtil.getInstance().log(text);
            ThreadManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    EasyCommonInfo.getInstance().FILE().write(new File(AlgorithmUtil.FILE_MFILE + AlgorithmUtil.FILE_MFILE_DEBUG_TEXT), text+"\r\n");
                }
            });
        }else{
            EngineerUtil.getInstance().destory();
        }

    }
    public static final String log_splite="  :  ";
    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
    private static String customTagPrefix = "";
    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }




    public static View getPageView(Activity activity, @DrawableRes int resid) {
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    public static List<View> getViews(Activity activity, int count) {
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < count; i++) {
            views.add(activity.getLayoutInflater().inflate(R.layout.image_discount, null));
        }
        return views;
    }

    /**
     * 根据startTime和endTime来确定是否同一天
     */
    public static boolean isSameDay(Long startTime, Long endTime) {
        String startDay = getDayFromData(timeStamp2Date(startTime.toString(), null));
        String startMonth = getMonthFromData(timeStamp2Date(startTime.toString(), null));

        String endDay = getDayFromData(timeStamp2Date(endTime.toString(), null));
        String endMonth = getMonthFromData(timeStamp2Date(endTime.toString(), null));

        //只有日期和月份都相同的我们才定义为一整天
        if (startDay == endDay && startMonth == endMonth) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 时间戳转具体年月日
     *
     * @param seconds
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    public static String timeStamp2DateNoSec(String days, String format) {
        if (days == null || days.isEmpty() || days.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(days)));
    }

    public static String timeStamp2DateNoSec2(String days, String format) {
        if (days == null || days.isEmpty() || days.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(days)));
    }

    public static String getDayFromData(String date) {
        String what_day = date.substring(8, 10);
        return what_day;
    }

    public static String getMonthFromData(String date) {
        String what_month = date.substring(5, 7);
        return what_month;
    }

    /**
     * 从SP中去拿餐厅号
     *
     * @return
     */
    public static String getOrgIdFromSp() {
        String orgID = RememberUtil.getString(UpdateTableHandler.ORGID, "100");
        Log.e("orgID--------",""+orgID);
        return RememberUtil.getString(UpdateTableHandler.ORGID, "100");
    }

    public static String getOrgIdFromSp(String defaultValue){
        return RememberUtil.getString(UpdateTableHandler.ORGID, defaultValue);
    }

    public static  Long getDeskId(){
        long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        return deskId;
    }

    public static Long getClientId(){
        String clientId = RememberUtil.getString(UpdateTableHandler.CLIENTID, "");
        QuickUtils.log("clientId=" + clientId);
        if(clientId.equals("")){
            return 8888L;
        }
        return Long.parseLong(clientId);
    }


    /**
     * 判断目录路径,比如storage/emulated/0/muye是否存在
     *
     * @return
     */
    public static boolean isHasVideoFolder() {
        File file = new File(AlgorithmUtil.VIDEO_FILE);
        //如果目录不存在public
        if (!file.exists() && !file.isDirectory()) {
            //先创建目录，然后返回false
            file.mkdir();
            return false;
        } else {
            return true;
        }
    }

    public static boolean isHasVideoFolder(String sPath) {
        File file = new File(sPath);
        //如果目录不存在public
        if (!file.exists() && !file.isDirectory()) {
            //先创建目录，然后返回false
            file.mkdir();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断目录下是否有文件
     *
     * @return
     */
    public static boolean isVideoFolderHaveFile() {
        File file = new File(AlgorithmUtil.VIDEO_FILE);
        File[] files = file.listFiles();

        if (files != null) {
            QuickUtils.log("files.length=" + files.length);
        }

        if ((files == null) || (files.length == 0)) {
            return false;
        } else {
            for (int i = 0; i < files.length; i++) {
                QuickUtils.log("files.name=" + files[i]);
                if (files[i].isDirectory()) {
                    //递归删除目录
                    deleteDir(files[i]);
                }
            }
            //在经过一轮删除目录中的目录后，如果该目录中还有length,那么就代表一定有文件
            if (files.length > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isVideoFolderHaveFiel2() {
        File file = new File(AlgorithmUtil.VIDEO_FILE);
        File[] files = file.listFiles();

        if (files != null) {
            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    QuickUtils.log(childFile.getAbsolutePath());
                }
            }
        }


        if ((files == null) || (files.length == 0)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isVideoFolderHaveFiel2(String sPath) {
        File file = new File(sPath);
        File[] files = file.listFiles();

        if (files != null) {
            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    QuickUtils.log(childFile.getAbsolutePath());
                }
            }
        }


        if ((files == null) || (files.length == 0)) {
            return false;
        } else {
            return true;
        }
    }

    public static File[] getVideoFolderFiles() {
        File file = new File(AlgorithmUtil.VIDEO_FILE);
        File[] files = file.listFiles();
        return files;
    }


    /**
     * 递归删除目录
     *
     * @param dir
     * @return true 删除成功
     *         false 删除失败
     */
     static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileExists(String path) {
        boolean flag = false;
        File file = new File(path);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 过滤单词101.mp4  为101
     *
     * @param fileName
     * @return
     */
    public static String subVideoEnd(String fileName) {
        return fileName.substring(0, fileName.length() - 4);
    }

    /**
     * 拼接URL前缀
     *
     * @param oriUrl
     * @param qiniuUrl
     * @return
     */
    public static String spliceUrl(String oriUrl,String qiniuUrl) {
        if(qiniuUrl!=null && !qiniuUrl.equals("")){
            return Constant.QINIU_URL+qiniuUrl;
        }
        return Constant.DDP_URL+"/push/" + oriUrl;
    }

    public static String spliceQiniuUrl(String qiniuUrl){
        return Constant.QINIU_URL+qiniuUrl;
    }

    public static String spliceOriUrl(String oriUrl){
        return Constant.DDP_URL+"/push/" + oriUrl;
    }


    /**
     * 发送短信
     */
    public static void sendSMSToService(int eventId){
        long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        if (deskId == Constant.DESK_ID_DEF_DEFAULT) {
            return;
        }
        final InfoSendSMSVo infoSendSMSVo = new InfoSendSMSVo();
        infoSendSMSVo.setTemplateID(eventId);
        infoSendSMSVo.setTableID(deskId);

        new Thread() {
            @Override
            public void run() {
                RequestNet.getData(infoSendSMSVo);
            }
        }.start();

    }

    private static ImageLoader imageLoader = ImageLoader.getInstance();
    public static void displayImage(String url, ImageView imageView){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory()
                    .cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new SimpleBitmapDisplayer()).build();
        imageLoader.displayImage(url, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });
    }

    public static void loadImage(String url){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory()
                .cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new SimpleBitmapDisplayer()).build();

        imageLoader.loadImage(url, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Log.e("onLoadingComplete", "onLoadingComplete");
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });
    }

    /**
     * 16进制转10进制
     * @return
     */
    public static  int hexToString(String hexadecimal){
        return  Integer.valueOf(hexadecimal, 16);
    }

    /**
     * 是否是空数据
     * @param json
     * @return
     */
    public static boolean isEmptyData(String json){
        try {
            List<DiscountInfo> discountInfos = ServiceUtil.getInstance().parserDiscountData(json);
            if(discountInfos.size()<=0){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean checkDiscountEmptyData(){
        if (FileRememberUtil.get(DownloadUtil.Dir_DISOUNT_JSON, DownloadUtil.DISOUNT_JSON) != null) {
            return isEmptyData(FileRememberUtil.get(DownloadUtil.Dir_DISOUNT_JSON, DownloadUtil.DISOUNT_JSON));
        }
        return true;
    }

    public static String getRunningActivityName(Activity activity) {
        String contextString = activity.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }

    public static boolean isActivityFinish(Context context){
        Activity activity = (Activity) context;
        if(!activity.isFinishing()){
            return false;
        }
        return true;
    }

    public static boolean isMainThread(){
        if(Looper.myLooper() == Looper.getMainLooper()){
            return true;
        }else{
            return false;
        }
    }

    public static String getIP() {
        WifiManager wifiManager = (WifiManager) SmartPenApplication.getApplication().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            //wifiManager.setWifiEnabled(true);
            return SmartPenApplication.getApplication().getString(R.string.version_unknown);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);

    }

    public static String getVersionName() {
        try {
            PackageInfo pi = SmartPenApplication.getApplication().getPackageManager().getPackageInfo(SmartPenApplication.getApplication().getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return SmartPenApplication.getApplication().getString(R.string.version_unknown);
        }
    }

    public static int getVersionCode(){
        try {
            PackageInfo pi=SmartPenApplication.getApplication().getPackageManager().getPackageInfo(SmartPenApplication.getApplication().getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 00000;
        }
    }

    /**
     * pad型号
     * @return
     */
    public static String getPadMode(){
        Build build = new Build();
        String model = build.MODEL;
        return model;
    }

    /**
     * 系统版本
     * @return
     */
    public static String getOSVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取mac地址
     * @return
     */
    public static String getMacAddress(){
        WifiManager manager = (WifiManager) SmartPenApplication.getApplication().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        if(info!=null){
            return info.getMacAddress();
        }else{
            return "00:00:00:00:00:00";
        }
    }

    /**
     * 获取wifi的信号强度
     * @return
     */
    public static String getWifiRSSI(){
        WifiManager manager = (WifiManager) SmartPenApplication.getApplication().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        if(info!=null){
            return String.valueOf(info.getRssi());
        }else{
            return String.valueOf(-1);
        }
    }

    /**
     * 获取媒体的音量值
     * @return
     */
    public static String getAudioVlaue(){
        AudioManager mAudioManager = (AudioManager) SmartPenApplication.getApplication().getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return String.valueOf(max);
    }

    public static String getRAM(){
        return "可用内存："+String.valueOf(getAvailMemory())+"MB"+" , "+"总内存："+String.valueOf(getTotalMemory()+"MB");
    }

    /**
     * 获取android当前可用内存大小
     * @return
     */
    private static long getAvailMemory(){
        ActivityManager am = (ActivityManager) SmartPenApplication.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem/(1024*1024);
    }

    /**
     * 获取android总内存大小
     * @return
     */
    private static long getTotalMemory(){
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try
        {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return initial_memory/(1024*1024);


    }

    /**
     * \n 回车(\u000a)
     * \t 水平制表符(\u0009)
     * \s 空格(\u0008)
     * \r 换行(\u000d)
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = null;
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    public static void doStatistic(final int eventId, final String eventDesc) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                StatisticsUtil.getInstance().insert(eventId, eventDesc);
            }
        });
    }

    public static void doStatistic(final int eventId, final String eventDesc, final Long secondEventId) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                StatisticsUtil.getInstance().insertWithSecondEvent(eventId, eventDesc, secondEventId);
            }
        });
    }


}
