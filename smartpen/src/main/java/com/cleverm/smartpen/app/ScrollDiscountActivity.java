package com.cleverm.smartpen.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bleframe.library.log.BleLog;
import com.cleverm.smartpen.R;
import com.cleverm.smartpen.bean.DiscountInfo;
import com.cleverm.smartpen.ui.infinite.InfiniteIndicator;
import com.cleverm.smartpen.ui.infinite.page.OnPageClickListener;
import com.cleverm.smartpen.ui.infinite.page.Page;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.ServiceUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.ThreadManager;
import com.cleverm.smartpen.util.UILoader;
import com.cleverm.smartpen.util.parts.DoBlePart;
import com.cleverm.smartpen.util.parts.DoDiskLruPart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 23/5/2016.
 * Data:23/5/2016  上午 09:34
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class ScrollDiscountActivity extends BaseActivity implements View.OnClickListener, OnPageClickListener, ViewPager.OnPageChangeListener {

    public static final String TAG="ScrollDiscountActivity：";

    public static final  String AllKey = "LocalKey";
    private String mAllData = "1";

    private Activity mContext;
    private InfiniteIndicator mScrollAdvertIil;
    private ImageView leftIv;
    private ImageView rightIv;
    private ImageView closeIv;
    private TextView numTv;

    public static final int GOBack = 200;
    public static final int TIME = 60000;

    private ArrayList<Page> pageViews;
    private ArrayList<String> images;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestFullScreen();
        setContentView(R.layout.activity_scroll_discount);
        mContext = this;
        initIntent();
        initView();
        initDate();
        initClick();
    }

    private void requestFullScreen() {
        if(DoBlePart.padNotShield()){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    private void initIntent() {


    }

    private void initView() {
        mScrollAdvertIil = (InfiniteIndicator) findViewById(R.id.iil_scroll_advert);
        leftIv = (ImageView) findViewById(R.id.iv_left);
        rightIv = (ImageView) findViewById(R.id.iv_right);
        closeIv = (ImageView) findViewById(R.id.iv_close);
        numTv = (TextView) findViewById(R.id.tv_num);
    }

    private void initDate() {
        String value= DoDiskLruPart.getInstance().get(AllKey);
        BleLog.e(TAG + value);
        if(value != null && value!= null){
            handlerJosn(value);
        }else{
            getDiscountDataFromService(mAllData);
        }
    }

    private void handlerJosn(String json) {
        try {
            AlgorithmUtil.getInstance().clearImageSequence();
            List<DiscountInfo> discountInfos = ServiceUtil.getInstance().parserDiscountData(json);
            if (discountInfos.size() <= 0) {
                return;
            }
            List<DiscountInfo> listImageSequence = AlgorithmUtil.getInstance().getSimpleImageSequence(discountInfos);
            images = QuickUtils.getDiscountImage(listImageSequence);
            initScrollAdvert(listImageSequence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initScrollAdvert(List<DiscountInfo> infos) {
        pageViews = new ArrayList<Page>();
        for(int i =0; i< infos.size() ; i++){
            pageViews.add(new Page(i+"", images.get(i), infos.get(i),this));
        }
        if (images.size() == 1) {
            leftIv.setVisibility(View.INVISIBLE);
            rightIv.setVisibility(View.INVISIBLE);
        }
        mScrollAdvertIil.setImageLoader(new UILoader());
        mScrollAdvertIil.addPages(pageViews);
        mScrollAdvertIil.setPosition(InfiniteIndicator.IndicatorPosition.Center_Bottom);
        mScrollAdvertIil.setOnPageChangeListener(this);
    }

    private void getDiscountDataFromService(String type) {
        ServiceUtil.getInstance().getDiscountData(QuickUtils.getOrgIdFromSp(), type, new ServiceUtil.JsonInterface() {
            @Override
            public void onSucced(final String json) {
                if (json != null && !json.equals("")) {
                    handlerJosn(json);
                    BleLog.e(TAG + json);
                    ThreadManager.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            //存入
                            DoDiskLruPart.getInstance().put(BaseDiscountActivity.AllKey, json);
                        }
                    });
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private void initClick() {
        leftIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
        closeIv.setOnClickListener(this);
    }


    @Override
    protected int onGetEventId() {
        return StatisticsUtil.SERVICE_DISCOUNT;
    }

    @Override
    protected String onGetDesc() {
        return StatisticsUtil.SERVICE_DISCOUNT_DESC;
    }

    protected void onBack() {
        AlgorithmUtil.getInstance().clearImageSequence();
        IntentUtil.goBackToVideoActivity(ScrollDiscountActivity.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                if(judgeImagelistSize()){
                    mScrollAdvertIil.goLeft();
                }
                break;

            case R.id.iv_right:
                if(judgeImagelistSize()){
                    mScrollAdvertIil.goRight();
                }
                break;

            case R.id.iv_close:
                onBack();
                break;
        }
    }

    @Override
    public void onPageClick(int position, Page page) {
        DiscountInfo info = (DiscountInfo)page.info;
        if (true) {
            StatisticsUtil.getInstance().insertWithSecondEvent(StatisticsUtil.SECOND_DISCOUNT_ACTIVITY, StatisticsUtil.SECOND_DISCOUNT_ACTIVITY_DESC, StatisticsUtil.getInstance().str2Long(info.getRollMainId()));
        } else {
            StatisticsUtil.getInstance().insertWithSecondEvent(StatisticsUtil.SECOND_LOACL_DISCOUNT_ACTIVITY, StatisticsUtil.SECOND_LOACL_DISCOUNT_ACTIVITY_DESC, StatisticsUtil.getInstance().str2Long(info.getRollMainId()));
        }
        Intent intent = new Intent(mContext, DiscountDetailActivity.class);
        intent.putExtra(DiscountDetailActivity.INTENT_NAME,info);
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        if(judgeImagelistSize()){
            numTv.setText((mScrollAdvertIil.getReallyCount(i) + 1) + "/" + images.size());
        }
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
        mScrollAdvertIil.stop();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScrollAdvertIil.start(1000);
        unLockScreen();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
    }

    public boolean judgeImagelistSize() {
        if (images != null) {
            if (images.size() > 0) {
                return true;
            }
        }
        return false;
    }

    private void unLockScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
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
}
