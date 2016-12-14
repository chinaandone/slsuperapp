package com.cleverm.smartpen.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.Version.VersionManager;
import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.bean.DiscountInfo;
import com.cleverm.smartpen.bean.event.OnBootRestartEvent;
import com.cleverm.smartpen.bean.event.OnChangeAnimNoticeEvent;
import com.cleverm.smartpen.bean.event.OnDestoryActivityEvent;
import com.cleverm.smartpen.bean.event.OnOutOfChargingEvent;
import com.cleverm.smartpen.bean.event.OnPayEvent;
import com.cleverm.smartpen.bean.event.OnToastEvent;
import com.cleverm.smartpen.bean.event.OnVideoBackEvent;
import com.cleverm.smartpen.database.DatabaseHelper;
import com.cleverm.smartpen.modle.TableType;
import com.cleverm.smartpen.net.InfoSendSMSVo;
import com.cleverm.smartpen.net.RequestNet;
import com.cleverm.smartpen.pushtable.NetworkMonitor;
import com.cleverm.smartpen.ui.FullScreenVideoView;
import com.cleverm.smartpen.ui.LongPressView;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.DownloadUtil;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.LuckyDrawUtil;
import com.cleverm.smartpen.util.NetWorkUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.SchedulingUtil;
import com.cleverm.smartpen.util.ServiceUtil;
import com.cleverm.smartpen.util.SimpleStatisticsLogic;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.ThreadManager;
import com.cleverm.smartpen.util.UpdateTableUtil;
import com.cleverm.smartpen.util.VideoManager;
import com.cleverm.smartpen.util.VideoPCUtil;
import com.cleverm.smartpen.util.WeakHandler;
import com.cleverm.smartpen.util.excle.CreateExcel;
import com.cleverm.smartpen.util.online.InformationOnline;
import com.cleverm.smartpen.util.parts.DoBlePart;
import com.cleverm.smartpen.util.parts.DoDiskLruPart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import static com.cleverm.smartpen.app.SimpleAppActivity.MessageCode.ALI_PAY;
import static com.cleverm.smartpen.app.SimpleAppActivity.MessageCode.CASH_PAY;
import static com.cleverm.smartpen.app.SimpleAppActivity.MessageCode.FOOD;
import static com.cleverm.smartpen.app.SimpleAppActivity.MessageCode.OTHER;
import static com.cleverm.smartpen.app.SimpleAppActivity.MessageCode.UNION_CARD_PAY;
import static com.cleverm.smartpen.app.SimpleAppActivity.MessageCode.WATER;
import static com.cleverm.smartpen.app.SimpleAppActivity.MessageCode.WEIXIN_PAY;

/**
 * Created by xiong,An android project Engineer,on 29/6/2016.
 * Data:29/6/2016  下午 03:22
 * Base on clever-m.com(JAVA Service)
 * Describe: 无笔版本Demo类
 * need to do :
 * 短信发送规则
 * <p/>
 * Version:1.0
 * Open source
 */
public class SimpleAppActivity extends BaseActivity implements View.OnClickListener, NetworkMonitor.NetworkChanged {

    private Context mContext;
    Dispatch mDispatch;
    private static final String TAG = SimpleAppActivity.class.getSimpleName()+"：";

    FullScreenVideoView mVideoFsvv;
    ImageView mCallFood;
    ImageView mCallWater;
    ImageView mCallPay;
    ImageView mCallOther;
    ImageView mGoDiscountIv;
    TextView mDeskTv;

    RelativeLayout mNoticeRl;
    ImageView mNoticeImageIv;
    TextView mNoticeTextRl;

    ImageView mGoShopIv;
    ImageView mGoPlay;

    ImageView mGoBenefitIv;
    ImageView mGoVideoIv;
    ImageView mGoGameIv;
    LongPressView mGoShadeSettingIv;

    RelativeLayout mToastRl;
    TextView mToastTv;

    LinearLayout mLevitatePay;
    ImageView mIvOutOfCharging;
    RelativeLayout mRlOutOfCharging;
    AnimationDrawable mOutOfChargingDrawable;

    ImageView mAdAnim;
    private ImageView mGoMoney;


    public enum MessageCode {
        FOOD, WATER, PAY, OTHER, CASH_PAY, UNION_CARD_PAY, WEIXIN_PAY, ALI_PAY
    }


    /**
     * 分发器
     */
    public class Dispatch {

        private DispatchHandler mDispatchHandler;

        private static final int MESSAGE_ANIM_START = 1;

        private static final int MESSAGE_ANIM_STOP = 2;

        private static final int DELAY_TIME = 3000;

        public Dispatch() {
            mDispatchHandler = new DispatchHandler(Looper.getMainLooper(), this);
        }


        public void dispatchAnimStart(OnChangeAnimNoticeEvent info) {
            mDispatchHandler.postAnimStart(info);
        }

        public void dispatchAnimStop() {
            mDispatchHandler.postAnimStop();
        }

        public class DispatchHandler extends Handler {

            private WeakReference<Dispatch> mDispatchRef;

            public DispatchHandler(Looper looper, Dispatch dispatch) {
                super(looper);
                this.mDispatchRef = new WeakReference<Dispatch>(dispatch);
            }

            @Override
            public void handleMessage(Message msg) {
                Dispatch dispatch = mDispatchRef.get();
                switch (msg.what) {
                    case MESSAGE_ANIM_START:
                        dispatch.startAnim((OnChangeAnimNoticeEvent) msg.obj);
                        break;
                    case MESSAGE_ANIM_STOP:
                        dispatch.stopAnim();
                        break;

                }
            }

            void postAnimStart(OnChangeAnimNoticeEvent info) {
                removeCallbacksAndMessages(null);
                obtainMessage(MESSAGE_ANIM_START, info).sendToTarget();
            }

            void postAnimStop() {
                mDispatchHandler.sendEmptyMessageDelayed(MESSAGE_ANIM_STOP, DELAY_TIME);
            }

        }


        private void startAnim(OnChangeAnimNoticeEvent event) {
            doAnim(new OnChangeAnimNoticeEvent(event.getImageResoure(), event.getText(), OnChangeAnimNoticeEvent.Status.SHOW));
        }

        private void stopAnim() {
            doAnim(new OnChangeAnimNoticeEvent(0, null, OnChangeAnimNoticeEvent.Status.HIDE));
        }


        private void doStartAnimation(final MessageCode code) {
            String text = null;
            int imageResource = -1;
            switch (code) {
                case FOOD: {
                    imageResource = R.mipmap.icon_simple_food;
                    text = SmartPenApplication.getApplication().getString(R.string.add_greens);
                    break;
                }
                case WATER: {
                    imageResource = R.mipmap.icon_simple_water;
                    text = SmartPenApplication.getApplication().getString(R.string.add_water);
                    break;
                }
                case UNION_CARD_PAY:{
                    imageResource = R.mipmap.icon_simple_pay_union_card;
                    text = SmartPenApplication.getApplication().getString(R.string.pay);
                    break;
                }

                case CASH_PAY:{
                    imageResource = R.mipmap.icon_simple_pay_cash;
                    text = SmartPenApplication.getApplication().getString(R.string.pay);
                    break;
                }
                case WEIXIN_PAY:{
                    imageResource = R.mipmap.icon_simple_pay_weixin;
                    text = SmartPenApplication.getApplication().getString(R.string.pay);
                    break;
                }
                case ALI_PAY:{
                    imageResource = R.mipmap.icon_simple_pay_ali;
                    text = SmartPenApplication.getApplication().getString(R.string.pay);
                    break;
                }
                case PAY: {
                    imageResource = R.mipmap.icon_simple_pay_normal;
                    text = SmartPenApplication.getApplication().getString(R.string.pay);
                    break;
                }

                case OTHER: {
                    imageResource = R.mipmap.icon_simple_other_normal;
                    text = SmartPenApplication.getApplication().getString(R.string.other);
                    break;
                }


            }

            mDispatch.dispatchAnimStart(new OnChangeAnimNoticeEvent(imageResource, text, OnChangeAnimNoticeEvent.Status.SHOW));

            mDispatch.dispatchAnimStop();
        }

        private void doAnim(OnChangeAnimNoticeEvent event) {
            onChangeAnimNoticeEvent(event);
        }


    }

    public void onChangeAnimNoticeEvent(OnChangeAnimNoticeEvent event) {
        if (event.getType() == OnChangeAnimNoticeEvent.Status.HIDE) {
            mNoticeRl.setVisibility(View.GONE);
        } else if (event.getType() == OnChangeAnimNoticeEvent.Status.SHOW) {
            mNoticeRl.setVisibility(View.VISIBLE);
            mNoticeTextRl.setText(event.getText());
            mNoticeImageIv.setImageResource(event.getImageResoure());
        }
    }


    public static class DoSmsSendPart {

        private static HashMap<Integer, Boolean> mHashMap = new HashMap<Integer, Boolean>();

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
                    case Constant.PAY_MONRY_SMS: {
                        mHashMap.put(Constant.PAY_MONRY_SMS, false);
                        break;
                    }
                    case Constant.OTHER_SERVICE_SMS: {
                        mHashMap.put(Constant.OTHER_SERVICE_SMS, false);
                        break;
                    }
                    case Constant.UNION_CARD_PAY_SMS: {
                        mHashMap.put(Constant.UNION_CARD_PAY_SMS, false);
                        break;
                    }
                    case Constant.CASH_PAY_SMS: {
                        mHashMap.put(Constant.CASH_PAY_SMS, false);
                        break;
                    }
                    case Constant.WEIXIN_PAY_SMS: {
                        mHashMap.put(Constant.WEIXIN_PAY_SMS, false);
                        break;
                    }
                    case Constant.ALI_PAY_SMS: {
                        mHashMap.put(Constant.ALI_PAY_SMS, false);
                        break;
                    }
                }
            }
        };

        public static void setTemplateIDState(int TemplateID,boolean isAreadSend) {
            mHashMap.put(TemplateID, true);
            if(!isAreadSend){
                mSMSHand.sendEmptyMessageDelayed(TemplateID, Constant.TEMPLATEID_DELAY);
            }
        }

        public static boolean getTemplateIDState(int TemplateID) {
            Boolean flag = mHashMap.get(TemplateID);
            if (flag == null) {
                return false;
            } else {
                return flag;
            }
        }


        private static class DoSmsPartHolder {
            private static final DoSmsSendPart INSTANCE = new DoSmsSendPart();
        }

        private DoSmsSendPart() {
        }

        public static final DoSmsSendPart getInstance() {
            return DoSmsPartHolder.INSTANCE;
        }


        public void sendSms(MessageCode penCode, boolean alreadySend, Dispatch dispatch) {
            if (penCode == null) {
                return;
            }
            int smsCode = statIteration(penCode);
            long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
            if (!NetWorkUtil.hasNetwork()) {
                QuickUtils.log("网络异常，请直接找服务员!");
                EventBus.getDefault().post(new OnToastEvent("网络异常，请直接找服务员!"));
                return;
            }
            if (deskId == Constant.DESK_ID_DEF_DEFAULT) {
                QuickUtils.log("桌号未设置，请直接找服务员!");
                EventBus.getDefault().post(new OnToastEvent("桌号未设置，请直接找服务员!"));
                return;
            }
            InfoSendSMSVo infoSendSMSVo = new InfoSendSMSVo();
            infoSendSMSVo.setTemplateID(smsCode);
            infoSendSMSVo.setTableID(deskId);
            QuickUtils.log(TAG + "SendSMS：" + "id=" + penCode + " deskId=" + deskId + " alreadySend=" + alreadySend);
            sendMessageToService(infoSendSMSVo, penCode, alreadySend, dispatch);
        }


        private void sendMessageToService(final InfoSendSMSVo infoSendSMSVo, final MessageCode code, final boolean alreadySend, final Dispatch dispatch) {
                    ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (alreadySend == false) {
                    
                    InfoSendSMSVo getSMSVo = RequestNet.getData(infoSendSMSVo);
                    
                    
                    
                    if (getSMSVo != null && getSMSVo.getSuccess()) {
                        QuickUtils.log(TAG + "SendSMS-success" + getSMSVo.getSuccess());
                        dispatch.doStartAnimation(code);
                    } else {
                    }
                } else {
                    dispatch.doStartAnimation(code);
                }
            }
        });
    }


        private int statIteration(MessageCode penCode) {
            int smsCode = 0;
            switch (penCode) {
                case FOOD:
                    smsCode = Constant.FOOD_ADD_SMS;
                    break;

                case WATER:
                    smsCode = Constant.WATER_ADD_SMS;
                    break;

                case PAY:
                    smsCode = Constant.PAY_MONRY_SMS;
                    break;

                case OTHER:
                    smsCode = Constant.OTHER_SERVICE_SMS;
                    break;

                case UNION_CARD_PAY:
                    smsCode = Constant.UNION_CARD_PAY_SMS;
                    break;

                case CASH_PAY:
                    smsCode = Constant.CASH_PAY_SMS;
                    break;

                case WEIXIN_PAY:
                    smsCode = Constant.WEIXIN_PAY_SMS;
                    break;


                case ALI_PAY:
                    smsCode = Constant.ALI_PAY_SMS;
                    break;


            }
            return smsCode;
        }




    }

    private void doStatistic(final int eventId, final String eventDesc) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                StatisticsUtil.getInstance().insert(eventId, eventDesc);
            }
        });
    }

    private void doStatistic(final int eventId, final String eventDesc, final Long secondEventId) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                StatisticsUtil.getInstance().insertWithSecondEvent(eventId, eventDesc, secondEventId);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {//very important code for avoid create activity again!
            finish();
            return;
        }
        initView();
        initListener();
        initData();

//        UpdateTableUtil.getInstance().goNewTable();
    }


    private void initView() {
        mContext = this;
        if(!DoBlePart.padNotShield()){
            setContentView(R.layout.activity_simple_app_for_teclast);
        }else{
            setContentView(R.layout.activity_simple_app_common);
        }

        mVideoFsvv = (FullScreenVideoView) findViewById(R.id.fsvv_video);
        mCallFood = (ImageView) findViewById(R.id.iv_call_food);
        mCallWater = (ImageView) findViewById(R.id.iv_call_water);
        mCallPay = (ImageView) findViewById(R.id.iv_call_pay);
        mCallOther = (ImageView) findViewById(R.id.iv_call_other);
        mDeskTv = (TextView) findViewById(R.id.tv_desk);

        mNoticeRl = (RelativeLayout) findViewById(R.id.rlNotice);
        mNoticeImageIv = (ImageView) findViewById(R.id.ivNoticeImage);
        mNoticeTextRl = (TextView) findViewById(R.id.rlNoticeText);

        mGoMoney = (ImageView) findViewById(R.id.iv_go_money);
        mGoShopIv = (ImageView) findViewById(R.id.iv_go_shop);
        mGoPlay = (ImageView) findViewById(R.id.iv_go_play);
        mGoBenefitIv = (ImageView) findViewById(R.id.iv_go_benefit);
        mGoVideoIv = (ImageView) findViewById(R.id.iv_go_video);
        mGoGameIv = (ImageView) findViewById(R.id.iv_go_game);
        mGoDiscountIv = (ImageView) findViewById(R.id.iv_go_discount);

        mGoShadeSettingIv = (LongPressView) findViewById(R.id.iv_go_shade_setting);

        mToastRl = (RelativeLayout) findViewById(R.id.rl_toast);
        mToastTv = (TextView) findViewById(R.id.tv_toast);

        mLevitatePay = (LinearLayout) findViewById(R.id.levitate_pay);

        mIvOutOfCharging = (ImageView) findViewById(R.id.iv_out_of_charging);
        mRlOutOfCharging = (RelativeLayout) findViewById(R.id.rl_out_of_charging);

        mAdAnim=(ImageView)findViewById(R.id.iv_ad_anim);

    }

    private void initListener() {
        mCallFood.setOnClickListener(this);
        mCallWater.setOnClickListener(this);
        mCallPay.setOnClickListener(this);
        mCallOther.setOnClickListener(this);
        mGoShopIv.setOnClickListener(this);
        mGoMoney.setOnClickListener(this);
        mGoPlay.setOnClickListener(this);
        mGoDiscountIv.setOnClickListener(this);
        mGoVideoIv.setOnClickListener(this);
        mGoGameIv.setOnClickListener(this);
        mGoBenefitIv.setOnClickListener(this);
        mGoShadeSettingIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                IntentUtil.goToSelectTableActivity(mContext);
                doStatistic(StatisticsUtil.SETTING, StatisticsUtil.SETTING_DESC);
                return false;
            }
        });
        findViewById(R.id.iv_levitate_pay_ali).setOnClickListener(this);
        findViewById(R.id.iv_levitate_pay_weixin).setOnClickListener(this);
        findViewById(R.id.iv_levitate_pay_unioncard).setOnClickListener(this);
        findViewById(R.id.iv_levitate_pay_cash).setOnClickListener(this);
        findViewById(R.id.iv_ad_detail).setOnClickListener(this);
        findViewById(R.id.iv_go_lucky).setOnClickListener(this);

        AnimationDrawable frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.lucky_goin_common_fram);
        findViewById(R.id.iv_go_lucky).setBackgroundDrawable(frameAnim);
        frameAnim.start();

    }


    private void initData() {
        mDispatch = new Dispatch();
        EventBus.getDefault().register(this);
        SimpleStatisticsLogic.getInstance().start(this);
        VideoManager.getInstance().initVideoEngine(mVideoFsvv, this);
        RememberUtil.putBoolean(Constant.BROADCAST_RESATRT_EVENT, false);
        checkNetState();
        doTimerClock();

    }

    private void doTimerClock() {
        //VideoPCUtil.getInstance().start(this);//PC Video
        new InformationOnline().start(this, InformationOnline.Time.REBOOT);//Clock
        new InformationOnline().start(this, InformationOnline.Time.TWELVE_AM);
        new InformationOnline().start(this, InformationOnline.Time.SEVEN_PM);
        LuckyDrawUtil.getInstance().getPrizeList();//Lucky
    }

    private void checkNetState() {
        NetworkMonitor.addMonitor(this, this);
    }


    @Override
    protected int onGetEventId() {
        return StatisticsUtil.BACK_VIDEO;
    }

    @Override
    protected String onGetDesc() {
        return StatisticsUtil.BACK_VIDEO_DESC;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_call_food:
                handlerMessage(FOOD);
                doStatistic(StatisticsUtil.CALL_ADD_FOOD, StatisticsUtil.CALL_ADD_FOOD_DESC);
                break;

            case R.id.iv_call_water:
                handlerMessage(WATER);
                doStatistic(StatisticsUtil.CALL_ADD_WATER, StatisticsUtil.CALL_ADD_WATER_DESC);
                break;
            case R.id.iv_call_pay:
                handlerPayMessage();
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC);
                break;

            case R.id.iv_call_other:
                handlerMessage(OTHER);
                doStatistic(StatisticsUtil.CALL_OTHER_SERVIC, StatisticsUtil.CALL_OTHER_SERVIC_DESC);
                break;

            case R.id.iv_go_discount://优惠专区
                if (isHaveData) {
                    IntentUtil.goToDiscountActivity(mContext);
                } else {
                    IntentUtil.goToUnknow(mContext);
                }
                hidePayLevitate();
                doStatistic(StatisticsUtil.SERVICE_DISCOUNT,StatisticsUtil.SERVICE_DISCOUNT_DESC);

                break;

            case R.id.iv_go_lucky:
                IntentUtil.goToLuckyActivity(SimpleAppActivity.this);
                doStatistic(StatisticsUtil.APP_PRIZE_GOIN, StatisticsUtil.APP_PRIZE_GOIN_DESC);
                break;

            case R.id.iv_go_shop://在线商场--统计代码在goToLauncherApp()
                IntentUtil.goToLauncherApp(mContext, Constant.ONE_SHOP_PACKAGE_NAME, StatisticsUtil.APP_ONLINEBUY, StatisticsUtil.APP_ONLINEBUY_DESC);
                hidePayLevitate();
                break;
            case R.id.iv_go_money://办卡有礼
                doStatistic(StatisticsUtil.CALL_GET_CARD, StatisticsUtil.CALL_GET_CARD_DESC);
                Intent intent = new Intent(this,moneyActivity.class);
                startActivity(intent);
                break;


            case R.id.iv_go_play://周边玩乐--统计代码在goToLauncherApp()
                IntentUtil.goToLauncherApp(mContext, Constant.BAI_DU_PACKAGE_NAME, StatisticsUtil.APP_AROUNDPLAY, StatisticsUtil.APP_AROUNDPLAY_DESC);
                hidePayLevitate();
                hidePayLevitate();
                break;


            case R.id.iv_go_benefit://周边优惠
                IntentUtil.goToH5Round(mContext);
                hidePayLevitate();
                doStatistic(StatisticsUtil.APP_AROUNDDISCOUNT,StatisticsUtil.APP_AROUNDDISCOUNT_DESC);
                break;

            case R.id.iv_go_video://视频娱乐--统计代码在goToLauncherApp()
                IntentUtil.goToLauncherApp(mContext, Constant.VIDEO_ENTERTAINMENT, StatisticsUtil.APP_VIDEO, StatisticsUtil.APP_VIDEO_DESC);
                hidePayLevitate();
                break;


            case R.id.iv_go_game://手游试玩
                //alan comment 20161114 for gameapp begin
                //                IntentUtil.goToH5Game(mContext);
                //                hidePayLevitate();
                //
                //                doStatistic(StatisticsUtil.H5_GAME,StatisticsUtil.H5_GAME_DESC);
                //alan comment 20161114 for gameapp end
                //alan add 20161114 for gameapp begin
                IntentUtil.goToLauncherApp(mContext, Constant.GAME_ENTERTAINMENT, StatisticsUtil.H5_GAME , StatisticsUtil.H5_GAME_DESC);
                hidePayLevitate();
                //alan add 20161114 for gameapp end
                break;

            case R.id.iv_levitate_pay_ali:
                hidePayLevitate();
                handlerMessage(ALI_PAY);
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC + "------" + StatisticsUtil.CALL_PAY_ALIPAY_DESC, StatisticsUtil.CALL_PAY_ALIPAY);
                break;

            case R.id.iv_levitate_pay_weixin:
                hidePayLevitate();
                handlerMessage(WEIXIN_PAY);
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC + "------" + StatisticsUtil.CALL_PAY_WEIXIN_DESC, StatisticsUtil.CALL_PAY_WEIXIN);
                break;

            case R.id.iv_levitate_pay_unioncard:
                hidePayLevitate();
                handlerMessage(UNION_CARD_PAY);
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC + "------" + StatisticsUtil.CALL_PAY_CARD_DESC, StatisticsUtil.CALL_PAY_CARD);
                break;

            case R.id.iv_levitate_pay_cash:
                hidePayLevitate();
                handlerMessage(CASH_PAY);
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC + "------" + StatisticsUtil.CALL_PAY_CASH_DESC, StatisticsUtil.CALL_PAY_CASH);
                break;

            case R.id.iv_ad_detail:
                IntentUtil.goToH5(mContext, Constant.URL_H5_WANGWANG);
                hidePayLevitate();
                doStatistic(StatisticsUtil.VIDEO_AD_DETAIL, StatisticsUtil.VIDEO_AD_DETAIL_DESC + "------" + "视频编号："+currentPlayVideo, Long.valueOf(currentPlayVideo));
                break;

        }
    }

    WeakHandler mPayHandler = new WeakHandler();
    private static boolean mLevitateShow=false;
    private Runnable payRunnable = new Runnable() {
        @Override
        public void run() {
            hidePayLevitate();
        }
    };



    private void hidePayLevitate(){
        if(mLevitatePay.getVisibility()==View.VISIBLE){
            mLevitatePay.setVisibility(View.GONE);
            mLevitateShow=false;
            mPayHandler.removeCallbacks(payRunnable);
        }
    }

    private void showPayLevitate(){
        mLevitatePay.setVisibility(View.VISIBLE);
        mLevitateShow = true;
        if (mLevitateShow) {
            mPayHandler.removeCallbacks(payRunnable);
            mPayHandler.postDelayed(payRunnable, 10000);
        }
    }

    private void handlerPayMessage() {
        if (mLevitatePay.getVisibility()==View.VISIBLE) {
            hidePayLevitate();
        }else if(mLevitatePay.getVisibility()==View.GONE||mLevitatePay.getVisibility()==View.INVISIBLE){
            showPayLevitate();
        }
    }

    private volatile boolean isHaveData = false;

    private  void checkNetData() {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                getDataFromService();
            }
        });
    }


    private synchronized void getDataFromService() {
        if (!isHaveData) {
            ServiceUtil.getInstance().getDiscountData(QuickUtils.getOrgIdFromSp(), "1", new ServiceUtil.JsonInterface() {
                @Override
                public void onSucced(final String json) {
                    if (json != null && !json.equals("")) {
                        List<DiscountInfo> discountInfos = null;
                        try {
                            discountInfos = ServiceUtil.getInstance().parserDiscountData(json);
                            if (discountInfos.size() <= 0) {
                                isHaveData = false;
                            } else {
                                isHaveData = true;
                            }
                            ThreadManager.getInstance().execute(new Runnable() {
                                @Override
                                public void run() {
                                    DoDiskLruPart.getInstance().put(BaseDiscountActivity.AllKey, json);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFail(String error) {

                }
            });
        }
    }


    /**
     * 在30秒内多次点击的不会做发送，但会显示图标
     */
    private void handlerMessage(MessageCode code) {
        boolean alreadySend = getAlreadySend(code);
        DoSmsSendPart.getInstance().sendSms(code, alreadySend, mDispatch);
    }

    private boolean getAlreadySend(MessageCode code) {
        int templateID = 0;
        switch (code) {
            case FOOD:
                templateID = Constant.FOOD_ADD_SMS;
                break;

            case WATER:
                templateID = Constant.WATER_ADD_SMS;
                break;

            case PAY:
                templateID = Constant.PAY_MONRY_SMS;
                break;

            case OTHER:
                templateID = Constant.OTHER_SERVICE_SMS;
                break;

            case UNION_CARD_PAY:
                templateID = Constant.UNION_CARD_PAY_SMS;
                break;

            case CASH_PAY:
                templateID = Constant.CASH_PAY_SMS;
                break;
            case WEIXIN_PAY:
                templateID = Constant.WEIXIN_PAY_SMS;
                break;
            case ALI_PAY:
                templateID = Constant.ALI_PAY_SMS;
                break;
        }

        boolean templateIDState = DoSmsSendPart.getInstance().getTemplateIDState(templateID);
        DoSmsSendPart.getInstance().setTemplateIDState(templateID,templateIDState);
        return templateIDState;
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBootRestartEvent(OnBootRestartEvent event) {
        QuickUtils.log("restart pad ："+"start pad on SimpleActivity");
        screenON();
        EventBus.getDefault().removeStickyEvent(event);
        new VersionManager(this).uddateVersion();
        VideoManager.getInstance().goUpdateVideo(mVideoFsvv, this);
        initCacheJson();
        //initStats();//数据统计
        if(!SmartPenApplication.getSimpleVersionFlag()){
            SchedulingUtil.doOnDemo(this);
        }
        UpdateTableUtil.getInstance().goNewTable();
    }

    private void screenON() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDestoryActivityEvent(final OnDestoryActivityEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        event.getActivity().finish();
    }

    private volatile boolean mToastShow = false;
    WeakHandler mToastHandler = new WeakHandler();
    private Runnable toastRunnable = new Runnable() {
        @Override
        public void run() {
            mToastRl.setVisibility(View.GONE);
            mToastShow = false;
            mToastHandler.removeCallbacks(toastRunnable);
            }
    };

    @Subscribe
    public void onToastEvent(final OnToastEvent event) {
        if (mToastShow) {
            mToastRl.setVisibility(View.GONE);
            mToastShow = false;
            mToastHandler.removeCallbacks(toastRunnable);
        }
        mToastRl.setVisibility(View.VISIBLE);
        mToastTv.setText(event.getMessage());
        mToastShow = true;
        if (mToastShow) {
            mToastHandler.postDelayed(toastRunnable, 3000);
        }
    }

    private static  int currentPlayVideo=-1;
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onVideoBackEvent(final OnVideoBackEvent event) {
        currentPlayVideo=event.getVideoId();
        if(event.getVideoId()==Constant.VIDEO_WANGWANG_ID){
            findViewById(R.id.iv_ad_detail).setVisibility(View.VISIBLE);
            mAdAnim.setBackgroundResource(R.drawable.frame_video_wangwang);
            AnimationDrawable mAdanimDrawable = (AnimationDrawable) mAdAnim.getBackground();
            if(mAdanimDrawable!=null){
                mAdanimDrawable.start();
            }
        }else{
            findViewById(R.id.iv_ad_detail).setVisibility(View.INVISIBLE);
            AnimationDrawable mAdanimDrawable = (AnimationDrawable) mAdAnim.getBackground();
            if(mAdanimDrawable!=null){
                mAdanimDrawable.stop();
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPayEvent(final OnPayEvent event) {
        handlerMessage(event.getCode());
    }

    WeakHandler mChargineHandler = new WeakHandler();
    private Runnable chargineRunnable = new Runnable() {
        @Override
        public void run() {
            mRlOutOfCharging.setVisibility(View.GONE);
            mChargineHandler.removeCallbacks(chargineRunnable);
            mOutOfChargingDrawable.stop();
        }
    };


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onOutOfChargingEvent(final OnOutOfChargingEvent event) {
        QuickUtils.log("onOutOfChargingEvent=" + event.isCharging());
        if(event.isCharging()){
            mRlOutOfCharging.setVisibility(View.GONE);
            mChargineHandler.removeCallbacks(chargineRunnable);
            mOutOfChargingDrawable.stop();
        }else{
            mChargineHandler.removeCallbacks(chargineRunnable);
            mRlOutOfCharging.setVisibility(View.VISIBLE);
            mIvOutOfCharging.setBackgroundResource(R.drawable.out_of_charging);
            mOutOfChargingDrawable = (AnimationDrawable) mIvOutOfCharging.getBackground();
            mOutOfChargingDrawable.start();
            mChargineHandler.postDelayed(chargineRunnable, 30000);
        }
    }


    private void initCacheJson() {
        DownloadUtil.cacheDiscountJson(QuickUtils.getOrgIdFromSp());
    }

    /**
     * 统计数据
     */
    @Deprecated
    private void initStats() {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                final long start = System.currentTimeMillis();
                CreateExcel createExcel = new CreateExcel();
                String execlName = createExcel.init();
                createExcel.writeExcel(StatisticsUtil.getInstance().getDBForExcel(), execlName);
                final long end = System.currentTimeMillis();
                QuickUtils.log("export excel time" + (end - start));
                if (!RememberUtil.getBoolean(StatisticsUtil.getInstance().getEventHappenTime(), false)) {
                    StatisticsUtil.getInstance().updateExcleToService(StatisticsUtil.UPLOAD_FILE_URL, StatisticsUtil.getInstance().getStatsFile());
                }
            }
        });
    }


    /**
     * 暂停或继续播放视频
     */
    int videoValue = 0;

    private void startPlayVideo() {
        videoValue = RememberUtil.getInt(Constant.MEMORY_PLAY_KEY, 0);
        mVideoFsvv.requestFocus();
        mVideoFsvv.start();
    }

    private void stopPlayVideo() {
        RememberUtil.putInt(Constant.MEMORY_PLAY_KEY, mVideoFsvv.getCurrentPosition());
        mVideoFsvv.pause();
    }


    private void unLockScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    private void whetherDeskInfo() {
        List<TableType> mTableTypes = DatabaseHelper.getsInstance(this).obtainAllTableTypes();
        long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        if (mTableTypes == null || mTableTypes.size() == 0 || deskId == Constant.DESK_ID_DEF_DEFAULT) {
            if (mDeskTv != null) {
                mDeskTv.setVisibility(View.VISIBLE);
            }
        } else {
            if (mDeskTv != null) {
                mDeskTv.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopPlayVideo();
    }


    @Override
    protected void onResume() {
        super.onResume();
        startPlayVideo();
        unLockScreen();
        whetherDeskInfo();
        //TODO 屏蔽关机时间后的一次视频界面
        doStatistic(StatisticsUtil.BACK_VIDEO, StatisticsUtil.BACK_VIDEO_DESC);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        NetworkMonitor.removeMonitor(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onNetworkConnect() {
        checkNetData();
    }

    @Override
    public void onNetworkDisconnect() {
        checkLocalData();
    }

    private void checkLocalData() {
        Log.d(TAG, "checkLocalData: asdfasdf");
        String json= DoDiskLruPart.getInstance().get(ScrollDiscountActivity.AllKey);
        if(json != null){
            List<DiscountInfo> discountInfos = null;
            try {
                discountInfos = ServiceUtil.getInstance().parserDiscountData(json);
                if (discountInfos.size() <= 0) {
                    isHaveData=false;
                }else{
                    isHaveData=true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }




}
