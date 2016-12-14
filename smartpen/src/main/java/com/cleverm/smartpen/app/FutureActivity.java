package com.cleverm.smartpen.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.parts.DoBlePart;

/**
 * Created by 95 on 2016/2/3.
 * 敬请期待
 */
public class FutureActivity extends BaseActivity {

    public static final String TAG = FutureActivity.class.getSimpleName();
    public static final int GOBack = 200;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOBack: {
                    Log.v(TAG, "come hand====");
                    IntentUtil.goBackToVideoActivity(FutureActivity.this);
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestFullScreen();
        setContentView(R.layout.future_activity);
        initGoBack();
    }

    private void requestFullScreen() {
        if(DoBlePart.padNotShield()){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }




    @Override
    protected int onGetEventId() {
        return eventId;
    }

    @Override
    protected String onGetDesc() {
        if(desc==null){
            return StatisticsUtil.ERROR_EVENTID;
        }
        return desc;
    }

    private void initGoBack() {
        findViewById(R.id.future_close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mHandler.sendEmptyMessage(GOBack);
                    }
                }
        );
        mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
    }

    private int eventId;
    private String desc;
    protected void initSonIntent() {
        Intent intent = getIntent();
        eventId = intent.getIntExtra(StatisticsUtil.FUTURE_INTNET_EVENTID, StatisticsUtil.SERVICE_BUY_MYSELF);
        desc = intent.getStringExtra(StatisticsUtil.FUTRUE_INTENT_EVENTDESC);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "OthersFragment onPause()=====");
        mHandler.removeCallbacksAndMessages(null);
    }


}