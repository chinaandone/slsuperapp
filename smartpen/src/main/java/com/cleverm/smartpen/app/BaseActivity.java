package com.cleverm.smartpen.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.service.penService;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Terry on 2016/2/3.
 */
public abstract class BaseActivity extends Activity {

    public static final CopyOnWriteArrayList<Activity> mActivityGroup=new CopyOnWriteArrayList<Activity>();

    protected StatisticsUtil.TimeValue timeValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        mActivityGroup.add(this);
        hideKeyBord();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initSonIntent();

    }

    /**
     * 子类复写,用于传递Intent的情况
     */
    protected void initSonIntent() {

    }

    protected abstract int onGetEventId();

    protected abstract String onGetDesc();

    private void hideKeyBord(){
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        window.setAttributes(params);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityGroup.remove(this);
    }

    /**
     * 必须为服务端提供回退到视频界面的统计数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(!SmartPenApplication.getSimpleVersionFlag()){
            timeValue = StatisticsUtil.getInstance().onCreate(onGetEventId(),onGetDesc());
        }
        MobclickAgent.onResume(this);

        MobclickAgent.onEvent(this, QuickUtils.getRunningActivityName(this));

        Constant.NEW_FLAG=QuickUtils.getRunningActivityName(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!SmartPenApplication.getSimpleVersionFlag()) {
            StatisticsUtil.getInstance().onDestory(timeValue);
        }
        MobclickAgent.onPause(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}