package com.cleverm.smartpen.util;

import android.app.Activity;

import com.cleverm.smartpen.app.VideoActivity;
import com.cleverm.smartpen.ui.FullScreenVideoView;

/**
 * Created by xiong,An android project Engineer,on 9/7/2016.
 * Data:9/7/2016  下午 03:53
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class VideoManager {

    private volatile boolean mAlreadyDataUpdate =false;


    private static class VideoManagerHolder {
        private static final VideoManager INSTANCE = new VideoManager();
    }
    private VideoManager (){}
    public static final VideoManager getInstance() {
        return VideoManagerHolder.INSTANCE;
    }


    public void initVideoEngine(FullScreenVideoView mAdvertFsvv,Activity activity) {
        if (QuickUtils.isHasVideoFolder() && QuickUtils.isVideoFolderHaveFiel2()) {
            if (QuickUtils.isHasVideoFolder(AlgorithmUtil.VIDEO_FILE_PLAY) && QuickUtils.isVideoFolderHaveFiel2(AlgorithmUtil.VIDEO_FILE_PLAY)) {
                VideoUtil videoUtil = new VideoUtil(mAdvertFsvv);
                videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE_PLAY, 0);
            } else {
                goUpdateVideo(mAdvertFsvv,activity);
            }
        } else {
            goUpdateVideo(mAdvertFsvv,activity);
        }
    }

    public void goUpdateVideo(FullScreenVideoView mAdvertFsvv,Activity activity){
        if(!mAlreadyDataUpdate){
            initData(mAdvertFsvv,activity);
        }
    }

    private void initData(FullScreenVideoView mAdvertFsvv,Activity activity) {
        QuickUtils.log("onBootRestartEvent:doUpdateVideo");
        mAlreadyDataUpdate = true;
        AlgorithmUtil.getInstance().startVideoPlayAlgorithm(mAdvertFsvv, activity);
    }



}
