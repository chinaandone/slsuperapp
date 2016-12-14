package com.cleverm.smartpen.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bleframe.library.log.BleLog;
import com.cleverm.apollo.communication.protocal.parser.data.RawParser;
import com.cleverm.smartpen.app.BaseActivity;
import com.cleverm.smartpen.app.DemoVideoActivity;
import com.cleverm.smartpen.app.DiscountActivity;
import com.cleverm.smartpen.app.DriverActivity;
import com.cleverm.smartpen.app.EvaluateActivity;
import com.cleverm.smartpen.app.FutureActivity;
import com.cleverm.smartpen.app.GameActivity;
import com.cleverm.smartpen.app.LocalDiscountActivity;
import com.cleverm.smartpen.app.LuckyDrawActivity;
import com.cleverm.smartpen.app.PayActivity;
import com.cleverm.smartpen.app.ScrollDiscountActivity;
import com.cleverm.smartpen.app.SelectTableActivity;
import com.cleverm.smartpen.app.SimpleAppActivity;
import com.cleverm.smartpen.app.VideoActivity;
import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.bean.TemplateIDState;
import com.cleverm.smartpen.bean.event.OnDestoryActivityEvent;
import com.cleverm.smartpen.bean.event.OnMessageCallEvent;
import com.cleverm.smartpen.bean.event.OnRobotShowEvent;
import com.cleverm.smartpen.service.penService;
import com.cleverm.smartpen.util.service.ApolloUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by xiong,An android project Engineer,on 2016/2/19.
 * Data:2016-02-19  13:41
 * Base on clever-m.com(JAVA Service)
 * Describe: Activity跳转帮助类
 * Version:1.0
 * Open source
 */
public class IntentUtil {

    /**
     * 从VideoActivity向其他的界面做跳转
     *
     * @param activity 原Activity.this
     * @param clazz    要跳转的界面
     */
    public static void startPenddingActivity(Activity activity, Class clazz) {
        Intent intent = new Intent(activity, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }

    public static void goPayServcie(Context context, int id) {
        TemplateIDState templateIDState = ChoiceTemplateIDState(id);
        if (!Constant.NEW_FLAG.equals(VideoActivity.class.getSimpleName())) {
            Intent intent = IntentUtil.mainActivityIntent(context);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            context.startActivity(intent);
            BleLog.e("OnMessageCallEvent---goPayServcie");
            EventBus.getDefault().postSticky(new OnMessageCallEvent(templateIDState.getId(), templateIDState.isSend()));
        }
    }




    public static void goCallService(Context service, int id, penService.MessageListener messageListener) {
        TemplateIDState templateIDState = ChoiceTemplateIDState(id);
        //不在VideoActivity界面调用
        if (!Constant.NEW_FLAG.equals(VideoActivity.class.getSimpleName())) {
            Intent intent =  IntentUtil.mainActivityIntent(service);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            //intent.putExtra(penService.VIDEO_ACTIVITY_KEY, 1);
            //intent.putExtra(penService.VIDEO_ACTIVITY_ISSEND, true);
            service.startActivity(intent);
            QuickUtils.log("----OnMessageCallEvent----");
            EventBus.getDefault().postSticky(new OnMessageCallEvent(templateIDState.getId(), templateIDState.isSend()));
        } else {
            if (NetWorkUtil.hasNetwork()) {
                if (messageListener != null) {
                    messageListener.receiveData(templateIDState.getId(), templateIDState.isSend());
                }
            } else {
                QuickUtils.toast("网络异常，请直接找服务员～");
            }
        }
    }

    /**
     * ************************************************
     * 30s内短信发送一次
     * ************************************************
     */
    private static TemplateIDState ChoiceTemplateIDState(int id) {
        TemplateIDState templateIDState = new TemplateIDState();
        int templateID = 0;
        switch (id) {
            case Constant.ORDER_DISHES1:
            case Constant.ORDER_DISHES2:
            case Constant.ORDER_DISHES3:
            case Constant.ORDER_DISHES4:
            case Constant.ORDER_DISHES5: {
                templateID = Constant.FOOD_ADD_SMS;
                break;
            }
            case Constant.ADD_WATER1:
            case Constant.ADD_WATER2:
            case Constant.ADD_WATER3:
            case Constant.ADD_WATER4:
            case Constant.ADD_WATER5: {
                templateID = Constant.WATER_ADD_SMS;
                break;
            }
            case Constant.PAY1:
            case Constant.PAY2:
            case Constant.PAY3:
            case Constant.PAY4:
            case Constant.PAY5: {
                templateID = Constant.PAY_MONRY_SMS;
                break;
            }
            case Constant.TISSUE1:
            case Constant.TISSUE2:
            case Constant.TISSUE3:
            case Constant.TISSUE4:
            case Constant.TISSUE5: {
                templateID = Constant.TISSUE_ADD_SMS;
                break;
            }
            case Constant.OTHER1:
            case Constant.OTHER2:
            case Constant.OTHER3:
            case Constant.OTHER4:
            case Constant.OTHER5: {
                templateID = Constant.OTHER_SERVICE_SMS;
                break;
            }
            case Constant.CLEAN_DESK: {
                templateID = Constant.CLEAN_SMS;
                break;
            }
            case Constant.FONDUE_SOUP: {
                templateID = Constant.FONDUE_SOUP_SMS;
                break;
            }
            case Constant.CHANGE_TABLEWARE: {
                templateID = Constant.CHANGE_TABLEWARE_SMS;
                break;
            }
            case Constant.CASH_PAY: {
                templateID = Constant.CASH_PAY_SMS;
                break;
            }
            case Constant.UNION_CARD_PAY: {
                templateID = Constant.UNION_CARD_PAY_SMS;
                break;
            }

            case Constant.WEIXIN_PAY:{
                templateID=Constant.WEIXIN_PAY_SMS;
                break;
            }

            case Constant.ALI_PAY:{
                templateID=Constant.ALI_PAY_SMS;
                break;
            }

            default: {
                break;
            }
        }
        boolean isSend = getTemplateIDState(templateID);
        /**
         * isSend-true表示30秒内已经发送过了
         */
        if (isSend) {
            templateIDState.setId(id);
            templateIDState.setIsSend(true);
        } else {
            setTemplateIDState(templateID);
            templateIDState.setId(id);
            templateIDState.setIsSend(false);
        }
        return templateIDState;

    }

    private static HashMap<Integer, Boolean> mHashMap = new HashMap<Integer, Boolean>();

    public static void setTemplateIDState(int TemplateID) {
        mHashMap.put(TemplateID, true);
        mSMSHand.sendEmptyMessageDelayed(TemplateID, Constant.TEMPLATEID_DELAY);
    }


    public static boolean getTemplateIDState(int TemplateID) {
        QuickUtils.log("goCallService-mHashMap=" + mHashMap);
        Boolean flag = mHashMap.get(TemplateID);
        QuickUtils.log("goCallService-flag=" + flag);
        if (flag == null) {
            return false;
        } else {
            return flag;
        }
    }

    private static Handler mSMSHand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.FOOD_ADD_SMS: {
                    mHashMap.put(Constant.FOOD_ADD_SMS, false);
                    break;
                }
                case Constant.WATER_ADD_SMS: {
                    mHashMap.put(Constant.WATER_ADD_SMS, false);
                    break;
                }
                case Constant.TISSUE_ADD_SMS: {
                    mHashMap.put(Constant.TISSUE_ADD_SMS, false);
                    break;
                }
                case Constant.PAY_MONRY_SMS: {
                    mHashMap.put(Constant.PAY_MONRY_SMS, false);
                    break;
                }
                case Constant.OTHER_SERVICE_SMS: {
                    mHashMap.put(Constant.OTHER_SERVICE_SMS, false);
                    break;
                }
                case Constant.CLEAN_SMS: {
                    mHashMap.put(Constant.CLEAN_SMS, false);
                    break;
                }
                case Constant.FONDUE_SOUP_SMS: {
                    mHashMap.put(Constant.FONDUE_SOUP_SMS, false);
                    break;
                }
                case Constant.CHANGE_TABLEWARE_SMS: {
                    mHashMap.put(Constant.CHANGE_TABLEWARE_SMS, false);
                    break;
                }
                case Constant.CASH_PAY_SMS: {
                    mHashMap.put(Constant.CASH_PAY_SMS, false);
                    break;
                }
                case Constant.UNION_CARD_PAY_SMS: {
                    mHashMap.put(Constant.UNION_CARD_PAY_SMS, false);
                    break;
                }
                case Constant.WEIXIN_PAY_SMS:{
                    mHashMap.put(Constant.WEIXIN_PAY_SMS,false);
                    break;
                }
                case Constant.ALI_PAY_SMS:{
                    mHashMap.put(Constant.ALI_PAY_SMS,false);
                    break;
                }

            }
        }
    };
    /**
     * ************************************************
     * 30s内短信发送一次
     * ************************************************
     */
    /**
     * 呼叫结账
     *
     * @param service
     */
    public static void goToPayActivity(Context service) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(PayActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, PayActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
    }

    /**
     * 敬请期待
     *
     * @param service
     * @param eventId
     * @param eventStr
     */
    public static void goToFutureActivity(Context service, int eventId, String eventStr) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(FutureActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, FutureActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            intent.putExtra(StatisticsUtil.FUTURE_INTNET_EVENTID, eventId);
            intent.putExtra(StatisticsUtil.FUTRUE_INTENT_EVENTDESC, eventStr);
            service.startActivity(intent);
        }
    }

    /**
     * Demo
     *
     * @param service
     */
    public static void goToDemoActivity(Context service) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(DemoVideoActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, DemoVideoActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
    }

    public static void goToEvaluateActivity(Context service) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(EvaluateActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, EvaluateActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
    }

    /**
     * 代驾服务
     *
     * @param service
     */
    public static void goToDriverActivity(Context service) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(DriverActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, DriverActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
    }

    /**
     * 设置桌号
     *
     * @param service
     */
    public static void goToSelectTableActivity(Context service) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(SelectTableActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, SelectTableActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
    }

    /**
     * 优惠专区
     *
     * @param service
     */
    public static void goToDiscountActivity(Context service) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(DiscountActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, ScrollDiscountActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
    }

    public static void goToLuckyActivity(Context service){
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(LuckyDrawActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, LuckyDrawActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
    }



    /**
     * 本店推荐
     *
     * @param service
     */
    public static void goToLocalDiscountActivity(Context service) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(LocalDiscountActivity.class.getSimpleName())) {
            Intent intent = new Intent(service, LocalDiscountActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
    }

    /**
     * 启动APP
     */
    public static void goToLauncherApp(Context context, String packName, int statId, String statDesc) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        LauncherApp(context, packName, statId, statDesc);
        Constant.NEW_FLAG = packName;
    }

    /**
     * 手游试玩
     *
     * @param context
     */
    public static void goToH5Game(Context context) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(Constant.URL)) {
            QuickUtils.log("ActivityTag=goToH5Game");
            Intent intent = new Intent(context, GameActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            intent.putExtra(penService.GAME_URL, Constant.URL);
            context.startActivity(intent);
            Constant.NEW_FLAG = Constant.URL;
        }
    }

    public static void goToH5(Context context,String url){
        if (!Constant.NEW_FLAG.equals(url)) {
            Intent intent = new Intent(context, GameActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            intent.putExtra(penService.GAME_URL,url);
            context.startActivity(intent);
            Constant.NEW_FLAG = url;
        }
    }



    public static void goToH5Round(Context context) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(Constant.NEARBY_DISCOUNT_URL)) {
            QuickUtils.log("ActivityTag=goToH5Round");
            Intent intent = new Intent(context, GameActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            intent.putExtra(penService.GAME_URL, Constant.NEARBY_DISCOUNT_URL);
            context.startActivity(intent);
            Constant.NEW_FLAG = Constant.NEARBY_DISCOUNT_URL;
        }
    }

    public static void goToBuyMyself(Context context) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(FutureActivity.class.getSimpleName())) {
            Intent intent = new Intent(context, FutureActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            intent.putExtra(StatisticsUtil.FUTURE_INTNET_EVENTID, StatisticsUtil.SERVICE_BUY_MYSELF);
            intent.putExtra(StatisticsUtil.FUTRUE_INTENT_EVENTDESC, StatisticsUtil.SERVICE_BUY_MYSELF_DESC);
            context.startActivity(intent);
        }
    }

    public static void goToUnknow(Context context) {
        QuickUtils.log("ActivityTag=" + Constant.NEW_FLAG);
        if (!Constant.NEW_FLAG.equals(FutureActivity.class.getSimpleName())) {
            Intent intent = new Intent(context, FutureActivity.class);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            intent.putExtra(StatisticsUtil.FUTURE_INTNET_EVENTID, StatisticsUtil.UN_KNOW);
            intent.putExtra(StatisticsUtil.FUTRUE_INTENT_EVENTDESC, StatisticsUtil.UN_KNOW_DESC);
            context.startActivity(intent);
        }
    }

    private static void LauncherApp(Context context, String packageName, int eventId, String eventDesc) {
        try {
            //弹跳APP的统计代码
            StatisticsUtil.getInstance().insert(eventId, eventDesc);
            String topActivityAppName = getTopActivityAppName(context);
            if (isSameNewFlag(packageName)) {
                return;
            }
            if (topActivityAppName.equals(packageName)) {
                return;
            }
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                            Intent.FLAG_ACTIVITY_NEW_TASK |intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.v("LauncherApp", "no this APK packageName=" + packageName);
            e.printStackTrace();
            if (!Constant.NEW_FLAG.equals(FutureActivity.class.getSimpleName())) {
                Intent intent = new Intent(context, FutureActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(StatisticsUtil.FUTURE_INTNET_EVENTID, eventId);
                intent.putExtra(StatisticsUtil.FUTRUE_INTENT_EVENTDESC, eventDesc);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 这个函数在台电平板中得到的总是点点笔package
     *
     * @return
     */
    private static String getTopActivityAppName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String packageName = info.topActivity.getPackageName(); //包名
        return packageName;
    }


    private static boolean isSameNewFlag(String packageName) {
        if (Constant.NEW_FLAG == packageName) {
            return true;
        }
        return false;
    }

    private static void intentFlag(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public static void intentFlagNotClear(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 保持栈内永远只有VideoActivity和当前顶Activity
     */
    public static void clearRedundantActivity() {
        for (final Activity activityTask : BaseActivity.mActivityGroup) {
            String mainActivityFlag;
            if(SmartPenApplication.getSimpleVersionFlag()){
                mainActivityFlag=SimpleAppActivity.class.getSimpleName();
            }else{
                mainActivityFlag=VideoActivity.class.getSimpleName();
            }
            if (!QuickUtils.getRunningActivityName(activityTask).equals(mainActivityFlag)) {
                if (activityTask != null) {
                    //为了兼容有线笔，必须在使用事件机制.不可直接finish()
                    EventBus.getDefault().postSticky(new OnDestoryActivityEvent(activityTask));
                    //activityTask.finish();
                    BaseActivity.mActivityGroup.remove(activityTask);
                }
            }
            //会出现内部装载2个VideoActivity的情况,剔除ArrayList中的相同元素
            for (int i = 0; i < BaseActivity.mActivityGroup.size() - 1; i++) {
                for (int j = BaseActivity.mActivityGroup.size() - 1; j > i; j--) {
                    if (QuickUtils.getRunningActivityName(BaseActivity.mActivityGroup.get(j)).equals(QuickUtils.getRunningActivityName(BaseActivity.mActivityGroup.get(i)))) {
                        QuickUtils.log("activityTask=BaseActivity.mActivityGroup.remove(j)");
                        BaseActivity.mActivityGroup.remove(j);
                    }
                }
            }
        }
    }

    public static void goBackToVideoActivity(Activity activity) {
        Intent intent = IntentUtil.mainActivityIntent(activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
        activity.finish();
        Constant.NEW_FLAG = VideoActivity.class.getSimpleName();
    }


    public static void clearMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, "com.qiyi.video.pad");
        } catch (Exception e) {
        }

    }


    public static void goToRobotShow(Context service) {
        if (!Constant.NEW_FLAG.equals(VideoActivity.class.getSimpleName())) {
            Intent intent = IntentUtil.mainActivityIntent(service);
            intentFlagNotClear(intent);
            clearRedundantActivity();
            service.startActivity(intent);
        }
        QuickUtils.toast("请稍等，马上为您呼叫Cooky,等待回应！");
        if (ApolloUtil.getInstance().getApolloServiceConnection() != null && ApolloUtil.getInstance().getApolloServiceConnection().isAlive()) {
            byte[] summonRobotCMD = new byte[6];
            summonRobotCMD[0] = (byte) 0x55;
            summonRobotCMD[1] = (byte) 0x06;
            summonRobotCMD[2] = (byte) 0x00;
            summonRobotCMD[3] = (byte) 0xC8;
            for (int i = 0; i < 4; i++) {
                summonRobotCMD[4] += summonRobotCMD[i];
            }
            summonRobotCMD[5] = (byte) 0xAA;
            int returnvalue=ApolloUtil.getInstance().getApolloServiceConnection().send(new RawParser(summonRobotCMD));
            EventBus.getDefault().postSticky(new OnRobotShowEvent(Constant.ROBOT_SHOW, returnvalue));
            QuickUtils.log("ServiceConnection=" + returnvalue);
        }
    }


    public static Intent mainActivityIntent(Context context){
        Intent intent;
        if(SmartPenApplication.getApplication().getSimpleVersionFlag()){
             intent = new Intent(context, SimpleAppActivity.class);
        }else{
             intent = new Intent(context, VideoActivity.class);
        }
        return intent;
    }



}
