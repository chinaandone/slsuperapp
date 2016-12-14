package com.cleverm.smartpen.util.online;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.cleverm.smartpen.broadcast.AlarmReceive;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.ThreadManager;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by xiong,An android project Engineer,on 22/8/2016.
 * Data:22/8/2016  上午 11:10
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class InformationOnline {

    private static final String TAG = InformationOnline.class.getSimpleName() + "：";
    public static final String ALARM_ACTION = "com.cleverm.smartpen.clock";
    public static final String ALARM_INTENT = "com.cleverm.smartpen.clock.intent";
    public static  int ALARM_ID = 0; //PendingIntent.getBroadcast的requestCode一定要是唯一的才可以避免闹钟定时被覆盖
    public static final String INFROMATION_URL = Constant.DDP_URL + "/api/api/v10/uploadProductStatusLog/save";
    private Activity mContext;
    private Time mTime;

    public InformationOnline() {

    }

    public enum Time {
        REBOOT, TWELVE_AM, SEVEN_PM
    }

    public void start(Activity context, Time time) {
        this.mContext = context;
        this.mTime = time;
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                go();
            }
        });
    }

    private void go() {
        sendTimeTask();
    }

    private void sendTimeTask() {

        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceive.class);
        intent.setAction(ALARM_ACTION);
        intent.putExtra(ALARM_INTENT, switchUploadTypeId());
        PendingIntent sender = PendingIntent.getBroadcast(mContext, ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long systemTime = System.currentTimeMillis();
        long selectTime = switchTime(systemTime);

        QuickUtils.log(TAG+" selectTime ===== " + selectTime + ", systemTime ==== " + systemTime);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.set(AlarmManager.RTC_WAKEUP, selectTime, sender);
        } else {
            manager.setExact(AlarmManager.RTC_WAKEUP, selectTime, sender);
        }

    }

    /**
     * 说明:1.开机; 2.12点状态; 3.19点状态; 4.关机;  1000.用户说的话;  1001.语音Log;  10000.离线;   10001.上线;
     *
     * @return
     */
    private int switchUploadTypeId() {
        int type = 1;
        switch (mTime) {
            case REBOOT:
                type = 1;
                ALARM_ID=1;
                break;

            case TWELVE_AM:
                type = 2;
                ALARM_ID=2;
                break;

            case SEVEN_PM:
                type = 3;
                ALARM_ID=3;
                break;

            default:
                type = 1;
                ALARM_ID=1;
                break;
        }
        return type;

    }

    private long switchTime(long systemTime) {
        long selectTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        switch (mTime) {
            case REBOOT:
                selectTime = 0;
                QuickUtils.log("InformationOnline   REBOOT=" + selectTime);
                break;

            case TWELVE_AM:
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                selectTime = calendar.getTimeInMillis();
                if (systemTime > selectTime) {// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
                    QuickUtils.log("设置的时间小于当前时间");
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    selectTime = calendar.getTimeInMillis();
                }
                QuickUtils.log("InformationOnline   TWELVE_AM=" + selectTime);
                break;

            case SEVEN_PM:
                calendar.set(Calendar.HOUR_OF_DAY, 19);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                selectTime = calendar.getTimeInMillis();
                if (systemTime > selectTime) {// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
                    QuickUtils.log("设置的时间小于当前时间");
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    selectTime = calendar.getTimeInMillis();
                }
                QuickUtils.log("InformationOnline   SIX_PM=" + selectTime);
                break;

            default:
                selectTime = 0;
                break;
        }

        return selectTime;
    }


}
