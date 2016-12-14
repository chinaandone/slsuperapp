package com.cleverm.smartpen.util.parts;


import android.os.Handler;
import android.os.Message;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.bean.event.OnChangeAnimNoticeEvent;
import com.cleverm.smartpen.util.Constant;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xiong,An android project Engineer,on 18/5/2016.
 * Data:18/5/2016  上午 10:46
 * Base on clever-m.com(JAVA Service)
 * Describe: 动画效果  + 统计报表 + 短信发送
 * Version:1.0
 * Open source
 */
public class DoAnimationPart {

    private static final int START_ANIMATION=1;
    private static final int STOP_ANIMATION=2;
    private static final int DELAY_TIME = 3000;

    private static DoAnimationPart INSTANCE = new DoAnimationPart();

    private DoAnimationPart(){

    }

    public static DoAnimationPart getInstance(){
        return INSTANCE;
    }



    private Handler mHandler = new Handler(SmartPenApplication.getApplication().getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case START_ANIMATION:
                    doStartAnimation((Integer)msg.obj);
                    break;

                case STOP_ANIMATION:
                    EventBus.getDefault().post(new OnChangeAnimNoticeEvent(-1,null, OnChangeAnimNoticeEvent.Status.HIDE));
                    break;
            }
        }
    };



    private void doStartAnimation(final int id) {
        String text = null;
        int imageResource=-1;
        switch (id) {
            case Constant.ORDER_DISHES1:
            case Constant.ORDER_DISHES2:
            case Constant.ORDER_DISHES3:
            case Constant.ORDER_DISHES4:
            case Constant.ORDER_DISHES5: {
                imageResource=R.mipmap.icon_dish;
                text = SmartPenApplication.getApplication().getString(R.string.add_greens);
                break;
            }
            case Constant.ADD_WATER1:
            case Constant.ADD_WATER2:
            case Constant.ADD_WATER3:
            case Constant.ADD_WATER4:
            case Constant.ADD_WATER5: {
                imageResource=R.mipmap.icon_water;
                text = SmartPenApplication.getApplication().getString(R.string.add_water);
                break;
            }
            case Constant.TISSUE1:
            case Constant.TISSUE2:
            case Constant.TISSUE3:
            case Constant.TISSUE4:
            case Constant.TISSUE5: {
                imageResource=R.mipmap.icon_tissue;
                text = SmartPenApplication.getApplication().getString(R.string.tissue);
                break;
            }
            case Constant.PAY1:
            case Constant.PAY2:
            case Constant.PAY3:
            case Constant.PAY4:
            case Constant.PAY5: {
                imageResource=R.mipmap.icon_pay;
                text = SmartPenApplication.getApplication().getString(R.string.pay);
                break;
            }
            case Constant.OTHER1:
            case Constant.OTHER2:
            case Constant.OTHER3:
            case Constant.OTHER4:
            case Constant.OTHER5: {
                imageResource=R.mipmap.icon_service;
                text = SmartPenApplication.getApplication().getString(R.string.other);
                break;
            }
            case Constant.CLEAN_DESK: {
                imageResource=R.mipmap.icon_clean;
                text = SmartPenApplication.getApplication().getString(R.string.clean);
                break;
            }
            case Constant.FONDUE_SOUP: {
                imageResource=R.mipmap.icon_fonduesoup;
                text = SmartPenApplication.getApplication().getString(R.string.fondue_soup);
                break;
            }
            case Constant.CHANGE_TABLEWARE: {
                imageResource=R.mipmap.icon_tableware;
                text = SmartPenApplication.getApplication().getString(R.string.change_tableware);

                break;
            }
            case Constant.CASH_PAY: {
                imageResource=R.mipmap.icon_cash_pay;
                text = SmartPenApplication.getApplication().getString(R.string.notificate_waiter_notice);

                break;
            }
            case Constant.UNION_CARD_PAY: {
                imageResource=R.mipmap.icon_union_pay;
                text = SmartPenApplication.getApplication().getString(R.string.notificate_waiter_notice);
                break;
            }
            case Constant.ROBOT_SHOW:{
                imageResource=R.mipmap.icon_robot_show;
                text=SmartPenApplication.getApplication().getString(R.string.robot_show_ok);
                break;
            }
        }
        EventBus.getDefault().post(new OnChangeAnimNoticeEvent(imageResource,text, OnChangeAnimNoticeEvent.Status.SHOW));
        mHandler.sendEmptyMessageDelayed(STOP_ANIMATION, DELAY_TIME);
    }
    


    public void doAnimation(int id){
        Message message = mHandler.obtainMessage();
        message.what=START_ANIMATION;
        message.obj=id;
        mHandler.sendMessage(message);
    }

}
