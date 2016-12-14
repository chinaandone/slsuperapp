package com.cleverm.smartpen.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.cleverm.smartpen.app.VideoActivity;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;


/**
 * Created by dessie on 2016/1/7.
 */
public class ScreenLockListenService extends Service {

    private ActivityManager am;
    private PowerManager pm;
    private PowerManager.WakeLock mWakelock;
    private Window mWindow;
    private int taskId;

    private ScreenLockReceiver mReceiver = new ScreenLockReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        return new ScreenLockListenServiceStub();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "lock");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        if (mWakelock.isHeld())
            mWakelock.release();
        mWakelock = null;
        super.onDestroy();
    }

    public class ScreenLockListenServiceStub extends Binder {

        public void setWindow(Window window) {
            mWindow = window;
        }

        public void setTaskId(int id) {
            taskId = id;
        }

    }

    class ScreenLockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.d("Screen", "off");
                ComponentName name = am.getRunningTasks(1).get(0).topActivity;
                String packageName = name.getPackageName();
                Log.d("packagename", packageName);
                am.moveTaskToFront(taskId, ActivityManager.MOVE_TASK_WITH_HOME);
                Intent in = IntentUtil.mainActivityIntent(ScreenLockListenService.this);
                IntentUtil.intentFlagNotClear(in);
                IntentUtil.clearRedundantActivity();
                startActivity(in);
                if (mWindow != null) {
                    mWindow.getAttributes().flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
                }
                if (!Constant.DIAN_CAI_PACKAGE_NAME.equals(packageName)) {
                    am.moveTaskToFront(taskId, ActivityManager.MOVE_TASK_WITH_HOME);
                }
                mWakelock.acquire();
                mWakelock.release();
            }
        }
    }
}
