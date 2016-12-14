package com.cleverm.smartpen.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.VideoAlgorithmUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 2016/2/29.
 * Data:2016-02-29  15:40
 * Base on clever-m.com(JAVA Service)
 * Describe: 下载服务  第一次有关的下载
 * Version:1.0
 * Open source
 */
public class DownloaderService extends Service{

    List<VideoInfo> info;

    public final static String SERVICE_OBJECT="SERVICE_OBJECT";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getIntent(intent);
        startDownloader();
        return Service.START_REDELIVER_INTENT;
    }

    private void getIntent(Intent intent) {
        info = (List<VideoInfo>) intent.getSerializableExtra(SERVICE_OBJECT);


    }

    private void startDownloader() {
        for (int i = 0; i < info.size(); i++) {
            VideoAlgorithmUtil.getInstance().downloadVideoFirst(QuickUtils.spliceUrl(info.get(i).getVideoPath(),info.get(i).getQiniuPath()), info.get(i).getVideoId() + "");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
