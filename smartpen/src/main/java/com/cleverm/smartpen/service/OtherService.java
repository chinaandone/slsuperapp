package com.cleverm.smartpen.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.CopyFileUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.ServiceUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONException;

import java.io.File;

/**
 * Created by xiong,An android project Engineer,on 3/5/2016.
 * Data:3/5/2016  下午 07:56
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class OtherService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startDownloader();
        return Service.START_REDELIVER_INTENT;
    }



    private void startDownloader() {
        downloadDemoUrl();
    }

    private void downloadDemoUrl() {
        ServiceUtil.getInstance().getDemoVideoData(Constant.DEMO_VIDEO_ID, Constant.DEMO_VIDEO_TYPE, new ServiceUtil.JsonInterface() {
            @Override
            public void onSucced(String json) {
                try {
                    VideoInfo videoInfo = ServiceUtil.getInstance().parserSingleData(json, VideoInfo.class);
                    if(videoInfo!=null){
                        downloadDemoVideo(QuickUtils.spliceUrl(videoInfo.getVideoPath(),videoInfo.getQiniuPath()), Constant.DEMO_VIDEO_ID);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(String error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void downloadDemoVideo(final String path,final String num) {
        OkHttpUtils//
                .get()//
                .url(path)//
                .build()//
                .connTimeOut(60000)
                .readTimeOut(60000)
                .writeTimeOut(60000)
                .execute(new FileCallBack(AlgorithmUtil.VIDEO_FILE, num + ".mp4"){
                    @Override
                    public void inProgress(float progress) {
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(File file) {
                        CopyFileUtil.copyByNIO(AlgorithmUtil.VIDEO_FILE + File.separator + num + ".mp4", AlgorithmUtil.VIDEO_DEMO_FILE + File.separator + num + ".mp4", false);
                    }
                });
    }

}

