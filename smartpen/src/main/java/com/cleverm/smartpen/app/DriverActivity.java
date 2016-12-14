package com.cleverm.smartpen.app;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.github.yoojia.zxing.qrcode.Encoder;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by 95 on 2016/2/3.
 */
public class DriverActivity extends BaseActivity {

    public static final String TAG = DriverActivity.class.getSimpleName();
    private ImageView mClose;
    public static final int GOBack = 200;
    public static final int TIME = 60000;
    public static final String ORGID ="OrgID";
    public static final String CLIENTID ="clientId";
    public static final String SELECTEDTABLEID="SelectedTableId";
    private String morgId;
    private String mclientId;
    private String mtableId;
    private String mCodeText = Constant.DDP_URL+"/api/api/v10/forward/driveCar?orgId="+morgId+"&clientId="+mclientId+"&tableId="+mtableId;
    private ImageView mdriveCode;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOBack: {
                    Log.v(TAG, "come hand====");
                    IntentUtil.goBackToVideoActivity(DriverActivity.this);
                    break;
                }
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.driver_activity);
        init();
    }

    @Override
    protected int onGetEventId() {
        return StatisticsUtil.SERVICE_DAIJIA;
    }

    @Override
    protected String onGetDesc() {
        return StatisticsUtil.SERVICE_DAIJIA_DESC;
    }


    private void init() {
        mClose= (ImageView) findViewById(R.id.e_driver_close);
        mdriveCode=(ImageView) findViewById(R.id.drive_code);
        morgId= RememberUtil.getString(ORGID,"");
        mclientId= RememberUtil.getString(CLIENTID,"");
        mtableId= RememberUtil.getString(SELECTEDTABLEID,"");
        creatTwoDimensionCode(mCodeText);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessage(GOBack);
            }
        });
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "OthersFragment onPause()=====");
        mHandler.removeCallbacksAndMessages(null);
        MobclickAgent.onEvent(this, "E_JIA");
    }

    private void creatTwoDimensionCode(String code){
        final int dimension = 268;
        Encoder mEncoder = new Encoder.Builder()
                .setBackgroundColor(0xFFFFFF)
                .setCodeColor(0xFF000000)
                .setOutputBitmapPadding(0)
                .setOutputBitmapWidth(dimension)
                .setOutputBitmapHeight(dimension)
                .build();
        Bitmap Bitmap=mEncoder.encode(code);
        Log.v(TAG, "code=" + code);
        Drawable drawable =new BitmapDrawable(Bitmap);
        mdriveCode.setBackground(drawable);
    }

    @Override
    public void onUserInteraction() {
        mHandler.removeMessages(GOBack);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
        super.onUserInteraction();
    }


}