package com.cleverm.smartpen.application;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.PowerManager;
import android.support.multidex.MultiDexApplication;

import com.cleverm.smartpen.broadcast.PowerReceiver;
import com.cleverm.smartpen.log.CrashHandler;
import com.cleverm.smartpen.log.FileUtil;
import com.cleverm.smartpen.service.CommunicationService;
import com.cleverm.smartpen.statistic.dao.StatsDao;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.cache.FileRememberUtil;
import com.cleverm.smartpen.util.common.EasyCommonInfo;
import com.cleverm.smartpen.util.event.BroadcastEvent;
import com.cleverm.smartpen.util.excle.CreateExcel;
import com.cleverm.smartpen.util.parts.DoBlePart;
import com.cleverm.smartpen.util.service.ApolloUtil;
import com.cleverm.smartpen.util.service.PenUtil;
import com.cleverm.smartpen.util.service.ScreenLockUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.thin.downloadmanager.ThinDownloadManager;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * Created by 95 on 2016/1/13.
 */
public class SmartPenApplication extends MultiDexApplication {
    public static final String TAG=SmartPenApplication.class.getSimpleName();

    private static SmartPenApplication application;
    private static ThinDownloadManager thinDownloadManager;
    private static SQLiteDatabase db;
    private static StatsDao statsDao;

    public static final String PATH= Environment.getExternalStorageDirectory().getPath()+"/logFile/log";
    private static final String PREFS_NAME = "com.Clever.myapp";

    public static CH34xUARTDriver usbdriver;

    /**
     * 是否是无笔版本
     */
    private static  boolean isSimpleVersion=true;

    /**
     * 是否废弃功能
     */
    private boolean negativeDiscard = false;

    /**
     * 是否debug模式
     */
    public static boolean isDebug=false;


    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        initSystemListener();
        initFileAndDatabase();
        initImageManager();
    }


    private void initSystemListener() {
        DoBlePart.openBLEPen(this);
        wakeUpAndUnlock(this);
        ScreenLockUtil.getInstance().bindService(this);
        PowerReceiver.getInstance().register(this);
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    private void initFileAndDatabase() {
        RememberUtil.init(getApplicationContext(), PREFS_NAME);
        FileRememberUtil.init(getApplicationContext());
        initDownloader();
        initEvent();
        initDataBase();
    }


    private void initImageManager() {
        initImageLoader();
    }


    /**
     * 废弃功能代码,关闭
     * use on onCreate()
     */
    @Deprecated
    private void discard() {
        if(negativeDiscard){
            //abandon  long communication
            Intent intent = new Intent(this, CommunicationService.class);
            intent.setAction(Constant.ACTION_CONNECT_SOCKET);
            startService(intent);
            //abandon pen service
            PenUtil.getInstance().bindService(this);
            //abandon apollo dance
            ApolloUtil.getInstance().bindService(this);
            //abandon crash log
            CrashHandler.getInstance().init(this, PATH);
        }
    }


    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .cacheInMemory(false)
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片的缩放方式:图像将被二次采样的整数倍
                .displayer(new FadeInBitmapDisplayer(300))// 正常显示一张图片,不是圆角RoundedBitmapDisplayer和渐变FadeInBitmapDisplayer
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2)// 线程池内加载数量
                .denyCacheImageMultipleSizesInMemory()
                .discCacheSize(50 * 1024 * 1024)// 硬盘缓存的最大大小50MB
                .discCacheFileCount(150)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
                        // 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options).build();//LIFO队列

        ImageLoader.getInstance().init(config);
    }

    private void initDataBase() {
        FileUtil.createMoreFile(
                AlgorithmUtil.VIDEO_FILE, AlgorithmUtil.VIDEO_FILE_PLAY,
                AlgorithmUtil.VIDEO_DEMO_FILE, AlgorithmUtil.FILE_MAPK,
                AlgorithmUtil.FILE_MFILE, AlgorithmUtil.FILE_MORG,
                AlgorithmUtil.FILE_MSCREEN, CreateExcel.EXPORT_PATH
        );
        EasyCommonInfo.getInstance().FILE().copyByNIO(new File(AlgorithmUtil.OLD_ORG_PATH),new File(AlgorithmUtil.FILE_MORG_ORG_TEXT),false);
        StatisticsUtil.DaoReturnValue daoReturnValue = StatisticsUtil.getInstance().onInit(this);
        db=daoReturnValue.getDb();
        statsDao = daoReturnValue.getStatsDao();
    }

    private void initEvent() {
        BroadcastEvent.init(this);
    }

    private void initDownloader() {
        this.thinDownloadManager = new ThinDownloadManager(4);
    }

    public static StatsDao getStatsDao() {
        return statsDao;
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    public static ThinDownloadManager getThinDownloadManager() {
        return thinDownloadManager;
    }

    public static SmartPenApplication getApplication() {
        return application;
    }

    public static boolean getSimpleVersionFlag(){
        return isSimpleVersion;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        onExit();
    }



    public static void wakeUpAndUnlock(Context context){
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK  | PowerManager.ACQUIRE_CAUSES_WAKEUP,"bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    /**
     * 废弃功能
     * use on onExit()
     */
    @Deprecated
    private void closeNet() {
        Intent intent = new Intent(this, CommunicationService.class);
        intent.setAction(Constant.ACTION_CONNECT_SOCKET);
        stopService(intent);
        PenUtil.getInstance().unbindServcie(this);
        ApolloUtil.getInstance().unbindService(this);
    }

    public void onExit(){
        DoBlePart.closedBLEPen();
        ScreenLockUtil.getInstance().unbindServcie(this);
        PowerReceiver.getInstance().unregister(this);
        System.exit(0);
    }

}
