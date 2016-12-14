package com.cleverm.smartpen.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.bean.LuckyPrizeIdInfo;
import com.cleverm.smartpen.bean.LuckyPrizeInfo;
import com.cleverm.smartpen.bean.event.OnLuckyEvent;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.LuckyDrawUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.WeakHandler;
import com.cleverm.smartpen.util.common.EasyCommonInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiong,An android project Engineer,on 1/9/2016.
 * Data:1/9/2016  下午 04:57
 * Base on clever-m.com(JAVA Service)
 * Describe: 5分钟后回退
 * Version:1.0
 * Open source
 */
public class LuckyDetailActivity extends BaseActivity implements View.OnClickListener {

    public static String INTENT_KEY="oldInfo";

    private LinearLayout mLuckyDetailPhoneLl;
    private LinearLayout mLuckyDetailCouponLl;

    ImageView mLuckyDetailBack;
    ImageView mLuckyDetailPhoneImage;
    ImageView mLuckyDetailcouponImage;
    EditText mLuckyDetailPhoneEd;
    Button mLuckyDetailPhoneBtn;

    LuckyPrizeIdInfo oldInfo;

    private boolean isGotPrize=false;
    private boolean isInputEd=false;

    @Override
    protected int onGetEventId() {
        return 0;
    }

    @Override
    protected String onGetDesc() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mLuckyDetailPhoneLl = (LinearLayout) findViewById(R.id.ll_lucky_detail_phone);
        mLuckyDetailCouponLl = (LinearLayout) findViewById(R.id.ll_lucky_detail_coupon);
        mLuckyDetailBack = (ImageView) findViewById(R.id.iv_lucky_detail_back);
        mLuckyDetailPhoneImage = (ImageView) findViewById(R.id.iv_lucky_detail_phone_image);
        mLuckyDetailcouponImage = (ImageView) findViewById(R.id.iv_lucky_detail_coupon_image);
        mLuckyDetailPhoneEd = (EditText) findViewById(R.id.ll_lucky_detail_phone_ed);
        mLuckyDetailPhoneBtn = (Button) findViewById(R.id.ll_lucky_detail_phone_btn);
    }



    private void initData() {
        Intent intent = getIntent();
        oldInfo = (LuckyPrizeIdInfo) intent.getSerializableExtra(INTENT_KEY);
        if(oldInfo.getType()==0){
            mLuckyDetailPhoneLl.setVisibility(View.VISIBLE);
            mLuckyDetailCouponLl.setVisibility(View.GONE);
            QuickUtils.displayImage(LuckyDrawUtil.URL_SPLITE_IMAGE+oldInfo.getBigPic(),mLuckyDetailPhoneImage);
        }else{
            mLuckyDetailPhoneLl.setVisibility(View.GONE);
            mLuckyDetailCouponLl.setVisibility(View.VISIBLE);
            QuickUtils.displayImage(LuckyDrawUtil.URL_SPLITE_IMAGE + oldInfo.getBigPic(), mLuckyDetailcouponImage);
        }
    }



    private void initListener() {
        mLuckyDetailBack.setOnClickListener(this);
        mLuckyDetailPhoneBtn.setOnClickListener(this);
        mLuckyDetailPhoneEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    mLuckyDetailPhoneEd.setText(sb.toString());
                    mLuckyDetailPhoneEd.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isInputEd){
                    QuickUtils.doStatistic(StatisticsUtil.APP_PRIZE_INPUTPHONE,StatisticsUtil.APP_PRIZE_INPUTPHONE_DESC);
                    isInputEd=true;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_lucky_detail_back:
                if(!isGotPrize){
                    LuckyDrawUtil.getInstance().cancelPrize(String.valueOf(oldInfo.getPrizeGetId()));
                }
                finish();
                break;

            case R.id.ll_lucky_detail_phone_btn:
                isGotPrize=true;
                mLuckyDetailPhoneBtn.setEnabled(false);
                QuickUtils.doStatistic(StatisticsUtil.APP_PRIZE_RECEIVEGOODS,StatisticsUtil.APP_PRIZE_RECEIVEGOODS_DESC);
                if(checkPhone()){
                    LuckyDrawUtil.getInstance().inputphone(mLuckyDetailPhoneEd.getText().toString().trim().replaceAll(" ",""),String.valueOf(oldInfo.getPrizeGetId()));
                }
                break;
        }
    }

    private boolean checkPhone() {
        String trim = mLuckyDetailPhoneEd.getText().toString().trim().replaceAll(" ","");
        if(trim.equals("")||trim==null){
            QuickUtils.toast("请输入手机号码...");
            return false;
        }
        if(!EasyCommonInfo.getInstance().PHONE().isMobilePhone(trim)){
            QuickUtils.toast("手机号码格式不匹配...");
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLuckyEvent(OnLuckyEvent event) {
        switch (event.getType()) {
            case gotPrizeSuccess:
                mLuckyDetailPhoneBtn.setEnabled(false);
                //QuickUtils.toast((String) event.getObj());
                findViewById(R.id.iv_lucky_detail_success).setVisibility(View.VISIBLE);
                doSuccessFinish();
                break;
            case gotPrizeFail:
                mLuckyDetailPhoneBtn.setEnabled(true);
                QuickUtils.toast((String) event.getObj());
                break;
        }

    }

    private void doSuccessFinish() {
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },10000);
    }


    /****
     * ***
     * ***回退操作***
     * ***
     */
    public static final int GOBack = 200;
    public static final int TIME = 300000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOBack: {
                    onBack();
                    break;
                }
            }
        }
    };

    private void onBack() {
        finish();
    }


    /**
     * 需要与onPause()中mHandler.removeCallbacksAndMessages(null);
     */
    @Override
    public void onUserInteraction() {
        mHandler.removeMessages(GOBack);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
        super.onUserInteraction();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
    }

}
