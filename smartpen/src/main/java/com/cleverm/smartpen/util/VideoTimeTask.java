package com.cleverm.smartpen.util;

import android.app.Activity;
import android.util.Log;

import com.cleverm.smartpen.ui.FullScreenVideoView;

import java.util.TimerTask;

/**
 * Created by xiong,An android project Engineer,on 2016/3/21.
 * Data:2016-03-21  14:01
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
@Deprecated
public class VideoTimeTask extends TimerTask {

    private FullScreenVideoView vvAdvertisement;
    private Activity activity;

    public VideoTimeTask(FullScreenVideoView vvAdvertisement,Activity activity){
        this.vvAdvertisement=vvAdvertisement;
        this.activity=activity;
    }

    @Override
    public void run() {
        Log.e("VideoTimeTask","run");
        AlgorithmUtil.getInstance().startVideoPlayAlgorithm(vvAdvertisement,activity);
    }
}
