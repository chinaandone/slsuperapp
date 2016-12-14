package com.cleverm.smartpen.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.StatisticsUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2016/2/3.
 */
public class DemoActivity extends BaseActivity {
    public static final String TAG = DemoActivity.class.getSimpleName();
    //关闭按钮
    private ImageView mClose;

    //Demo
    private ViewPager mViewPager;
    private int[] drawableRes = new int[]{R.mipmap.guide1, R.mipmap.guide2};
    private DemoPagerAdapter mDemoPagerAdapter;

    public static final int GOBack = 200;
    public static final int TIME = Constant.DELAY_BACK;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOBack: {
                    Log.v(TAG, "come hand====");
                    IntentUtil.goBackToVideoActivity(DemoActivity.this);
                    break;
                }
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        init();
        System.out.println("AAA=="+getDeviceInfo(this));
    }

    @Override
    protected int onGetEventId() {
        return StatisticsUtil.SERVICE_DEMO;
    }

    @Override
    protected String onGetDesc() {
        return StatisticsUtil.SERVICE_DEMO_DESC;
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "OthersFragment onPause()=====");
        mHandler.removeCallbacksAndMessages(null);
    }

    private void init() {
        mClose = (ImageView) findViewById(R.id.demo_close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessage(GOBack);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mDemoPagerAdapter == null) {
            List<View> list = new ArrayList<View>();
            View pagerView1 = View.inflate(this, R.layout.viewpageritem, null);
            ImageView mImageView1 = (ImageView) pagerView1.findViewById(R.id.viewpagerItemImage);
            mImageView1.setBackgroundResource(drawableRes[0]);
            list.add(pagerView1);
            View pagerView2 = View.inflate(this, R.layout.viewpageritem, null);
            ImageView mImageView2 = (ImageView) pagerView2.findViewById(R.id.viewpagerItemImage);
            mImageView2.setBackgroundResource(drawableRes[1]);
            list.add(pagerView2);
            mDemoPagerAdapter = new DemoPagerAdapter(this, list);
        }
        mViewPager.setAdapter(mDemoPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, TIME);
            }

            @Override
            public void onPageSelected(int i) {
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, TIME);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, TIME);
            }
        });

    }


    class DemoPagerAdapter extends PagerAdapter {
        private Context mContext;
        private List<View> list;


        public DemoPagerAdapter(Context mContext, List<View> list) {
            this.mContext = mContext;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view==o;
        }
    }


    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}