package com.cleverm.smartpen.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.ui.FullScreenVideoView;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.CopyFileUtil;
import com.cleverm.smartpen.util.DemoVideoUtil;
import com.cleverm.smartpen.util.FileUtil;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.ServiceUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

/**
 * Created by xiong,An android project Engineer,on 2016/3/1.
 * Data:2016-05-03  14:54
 * Base on clever-m.com(JAVA Service)
 * Describe: demo视频引导
 * Version:1.0
 * Open source
 */
public class DemoVideoActivity extends BaseActivity {
    public static final String TAG = DemoVideoActivity.class.getSimpleName();

    //关闭按钮
    private ImageView mClose;
    private FullScreenVideoView vvAdvertisement;

    public static final int GOBack = 200;
    public static final int TIME = Constant.DELAY_BACK;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOBack: {
                    IntentUtil.goBackToVideoActivity(DemoVideoActivity.this);
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_video);
        initView();
        initEmptyMessage();
    }


    private void initEmptyMessage() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
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
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initView() {
        mClose = (ImageView) findViewById(R.id.demo_close);
        vvAdvertisement = (FullScreenVideoView) findViewById(R.id.vvAdvertisement);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessage(GOBack);
            }
        });
    }


    private void initData() {
        if (FileUtil.checkFileExist(new File(AlgorithmUtil.VIDEO_DEMO_FILE + File.separator + Constant.DEMO_VIDEO_ID + ".mp4"))) {
            DemoVideoUtil.getInstance().prepareLocalVideoKeptGo(AlgorithmUtil.VIDEO_DEMO_FILE, vvAdvertisement);
        } else {
            getDataFromService();
        }
    }

    private void getDataFromService() {
        ServiceUtil.getInstance().getDemoVideoData(Constant.DEMO_VIDEO_ID, Constant.DEMO_VIDEO_TYPE, new ServiceUtil.JsonInterface() {
            @Override
            public void onSucced(String json) {
                try {
                    VideoInfo videoInfo = ServiceUtil.getInstance().parserSingleData(json, VideoInfo.class);
                    if (videoInfo != null) {
                        downloadDemoVideo(QuickUtils.spliceUrl(videoInfo.getVideoPath(),videoInfo.getQiniuPath()), Constant.DEMO_VIDEO_ID,videoInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onUserInteraction() {
        mHandler.removeMessages(GOBack);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
        super.onUserInteraction();
    }

    public void downloadDemoVideo(final String path,final String num,final VideoInfo videoInfo) {
        OkHttpUtils//
                .get()//
                .url(path)//
                .build()//
                .connTimeOut(60000)
                .readTimeOut(60000)
                .writeTimeOut(60000)
                .execute(new FileCallBack(AlgorithmUtil.VIDEO_FILE, num + ".mp4") {
                    @Override
                    public void inProgress(float progress) {
                        Log.e("inProgress",""+progress);
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(File file) {
                        CopyFileUtil.copyByNIO(AlgorithmUtil.VIDEO_FILE + File.separator + num + ".mp4", AlgorithmUtil.VIDEO_DEMO_FILE + File.separator + num + ".mp4", false);
                        if(!QuickUtils.isActivityFinish(DemoVideoActivity.this)){
                            DemoVideoUtil.getInstance().prepareOnlineVideo(videoInfo,vvAdvertisement);
                        }
                    }
                });
    }
}