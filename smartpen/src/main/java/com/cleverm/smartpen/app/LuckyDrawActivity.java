package com.cleverm.smartpen.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.bean.LuckyPrizeIdInfo;
import com.cleverm.smartpen.bean.LuckyPrizeInfo;
import com.cleverm.smartpen.bean.event.OnLuckyEvent;
import com.cleverm.smartpen.ui.lucky.LuckyView;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.JsonUtil;
import com.cleverm.smartpen.util.LuckyDrawUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.WeakHandler;
import com.cleverm.smartpen.util.parts.DoDiskLruPart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 29/8/2016.
 * Data:29/8/2016  下午 06:56
 * Base on clever-m.com(JAVA Service)
 * Describe: 1分钟后回退
 * Version:1.0
 * Open source
 */
public class LuckyDrawActivity extends BaseActivity implements View.OnClickListener {

    ImageView mLuckyBackLL;
    LuckyView mLuckDraw;

    public static final int GOBack = 200;
    public static final int TIME = 60000;

    private HashMap<Integer, Integer> mLocalPosition = new HashMap<Integer, Integer>();
    private ArrayList<Integer> mThanksList = new ArrayList<Integer>();
    private LuckyPrizeIdInfo mOldInfo = null;

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
        setContentView(R.layout.activity_lucky_draw);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        mLuckyBackLL = (ImageView) findViewById(R.id.ll_lucky_back);
        mLuckDraw = (com.cleverm.smartpen.ui.lucky.LuckyView) findViewById(R.id.lv_luck_draw);
    }

    /**
     * 在视频界面就进行下载并存储，如果视频界面下载失败或者未完成
     * 就进入这个界面时同时全部默认显示谢谢惠顾
     * 然后开启下载，下载完成后，更换存储
     */
    private void initData() {
        if(LuckyDrawUtil.mMemoryCache==null){
            LuckyDrawUtil.getInstance().getPrizeList();
        }else{
            handlerServerPrizeList(LuckyDrawUtil.mMemoryCache);
        }
    }



    private void initListener() {
        mLuckyBackLL.setOnClickListener(this);
        mLuckDraw.setOnLuckyDrawListener(new LuckyView.OnLuckyDrawListener() {
            @Override
            public void start() {
                LuckyDrawUtil.getInstance().getPrizeId();
                QuickUtils.doStatistic(StatisticsUtil.APP_PRIZE_CLICKDRAW,StatisticsUtil.APP_PRIZE_CLICKDRAW_DESC);
            }

            @Override
            public void stop() {
                if (mOldInfo != null) {
                    if (mOldInfo.getType() != 2) {
                        final Intent intent = new Intent(LuckyDrawActivity.this, LuckyDetailActivity.class);
                        intent.putExtra(LuckyDetailActivity.INTENT_KEY, mOldInfo);
                        new WeakHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        }, 500);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_lucky_back:
                finish();
                break;

            default:
                break;
        }
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
            case ServerPrizeList:
                handlerServerPrizeList((List<LuckyPrizeInfo>) event.getObj());
                break;
            case ServerPrizeId:
                handlerServerPrizeId((LuckyPrizeIdInfo) event.getObj());
                break;
            case defaultPrizeList:
                handlerServerPrizeList((List<LuckyPrizeInfo>) event.getObj());
                break;
            case defaultPrizeId:
                showThanks();
                break;
        }
    }

    private void showThanks() {
        mOldInfo =null;
        if (mThanksList.size() > 0) {
            int end = (int) (Math.random() * mThanksList.size()) ;
            mLuckDraw.stop(mThanksList.get(end));
        }
    }


    private void handlerServerPrizeList(List<LuckyPrizeInfo> lists) {
        ArrayList<String> images = new ArrayList<String>();
        ArrayList<String> texts = new ArrayList<String>();
        for (int i = 0; i < lists.size(); i++) {
            //位置对应关系
            int id = lists.get(i).getId();
            mLocalPosition.put(id, i);
            //谢谢惠顾
            int type = lists.get(i).getType();
            if (type == 2) {
                mThanksList.add(i);
            }
            //list列表
            String smallPic = lists.get(i).getSmallPic();
            String name = lists.get(i).getName();
            images.add(smallPic);
            texts.add(name);
        }
        mLuckDraw.setPrizeImageUrl(images);
        mLuckDraw.setPrizeText(texts);
    }

    private void handlerServerPrizeId(LuckyPrizeIdInfo info) {
        mOldInfo = info;
        int end = wherePrizeStay(info.getId());//根据id来判断停留地
        QuickUtils.log("服务端奖品ID="+info.getId()+"/本地localID="+end);
        mLuckDraw.stop(end);
    }

    private int wherePrizeStay(int id) {
        return mLocalPosition.get(id);
    }


    /****
     * ***
     * ***回退操作***
     * ***
     */
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
        IntentUtil.goBackToVideoActivity(LuckyDrawActivity.this);
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
