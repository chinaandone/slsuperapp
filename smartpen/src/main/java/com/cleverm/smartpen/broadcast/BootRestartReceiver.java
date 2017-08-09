package com.cleverm.smartpen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cleverm.smartpen.bean.event.OnBootRestartEvent;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.StatisticsUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by 95 on 2016/1/8.
 */
public class BootRestartReceiver extends BroadcastReceiver {

    private final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals(ACTION)){
            //统计
            StatisticsUtil.getInstance().insert(StatisticsUtil.OTHER_OPEN_TIME,StatisticsUtil.OTHER_OPEN_TIME_DESC);
            //一天只取一次数据,所以通过开机的一个boolean状态来控制
            //RememberUtil.putBooleanSync(Constant.BROADCAST_RESATRT_EVENT,true);
            Intent intent2 = IntentUtil.mainActivityIntent(context);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
            Log.d("DEBUG", "start pad on..." + System.currentTimeMillis());
            EventBus.getDefault().postSticky(new OnBootRestartEvent());

        }
    }
}