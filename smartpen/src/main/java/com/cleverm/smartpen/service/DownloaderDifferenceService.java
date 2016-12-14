package com.cleverm.smartpen.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.SerializableMap;
import com.cleverm.smartpen.util.VideoAlgorithmUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiong,An android project Engineer,on 2016/2/29.
 * Data:2016-02-29  15:40
 * Base on clever-m.com(JAVA Service)
 * Describe: 下载服务
 * Version:1.0
 * Open source
 */
public class DownloaderDifferenceService extends Service {

    List<VideoInfo> info;
    private Map<String, String> LocalList;

    public final static String SERVICE_DIFFERENCE_LIST = "SERVICE_DIFFERENCE_OBJECT";
    public final static String SERVICE_CONTAINSKEY_MAP = "SERVICE_CONTAINSKEY_MAP";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
     *
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
        info = (List<VideoInfo>) intent.getSerializableExtra(SERVICE_DIFFERENCE_LIST);
        Bundle bundle = intent.getExtras();
        ArrayList<Map<String, String>> lists = (ArrayList<Map<String, String>>) bundle.getSerializable(SERVICE_CONTAINSKEY_MAP);
        LocalList = lists.get(0);
        Log.e("copyFile", "LocalList.service.size()=" + LocalList.size());
    }

    private void startDownloader() {
        for (int i = 0; i < info.size(); i++) {
            if (!LocalList.containsKey(info.get(i).getVideoId() + "")) {
                VideoAlgorithmUtil.getInstance().downloadVideoFirst(QuickUtils.spliceUrl(info.get(i).getVideoPath(),info.get(i).getQiniuPath()), info.get(i).getVideoId() + "");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
