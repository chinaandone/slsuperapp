package com.cleverm.smartpen.util;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.VideoView;


import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.bean.event.OnVideoBackEvent;
import com.cleverm.smartpen.log.FileUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

/**
 * Created by xiong on 2016/2/15.
 * 业务逻辑:将所有视频的URL地址的文件下载，然后存储到本地
 * 从本地中取出文件,判断每个文件的时常大小，根据时常和排序规则来处理视频
 * git
 */
public class VideoUtil {


    public int videoIndex = 0;
    public VideoView mVideoView;

    public VideoUtil(VideoView mVideoView) {
        this.mVideoView = mVideoView;
    }

    public VideoUtil() {

    }

    /**
     * 开启播放在线视频
     *
     * @param info
     */
    public void prepareOnlineVideo(List<VideoInfo> info) {
        final String[] videoUrls = getOnlineVideoURI(info);
        if (videoUrls == null) {
            return;
        }
        mVideoView.setVideoPath(videoUrls[videoIndex]);
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(checkHaveVideo()){
                    prepareLocalVideo(AlgorithmUtil.VIDEO_FILE_PLAY,0);
                    return;
                }
                videoIndex++;
                if (videoIndex >= videoUrls.length) {
                    videoIndex = 0;
                }
                mVideoView.setVideoPath(videoUrls[videoIndex]);
                mVideoView.start();
            }
        });
    }

    private boolean checkHaveVideo() {
        String[] videoURIs = getVideoURIs(AlgorithmUtil.VIDEO_FILE_PLAY);
        if(videoURIs!= null){
            if(videoURIs.length>0){
                return true;
            }
        }
        return false;
    }

    public void prepareOnlineVideo(final VideoInfo video) {
        if (video.getQiniuPath() == null) {
            return;
        }
        mVideoView.setVideoPath(QuickUtils.spliceUrl(video.getVideoPath(), video.getQiniuPath()));
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.setVideoPath(QuickUtils.spliceUrl(video.getVideoPath(), video.getQiniuPath()));
                mVideoView.start();
            }
        });

    }


    /**
     * 开启播放本地视频
     *
     * @param mPath
     */
    public void prepareLocalVideo(final String mPath, int currentPosition) {
        //初始化videoIndex=0;
        videoIndex = 0;
        final String[] videoUrls = getVideoURIs(mPath);

        if (videoUrls == null || videoUrls.length <= 0) {
            return;
        }

        postVideoId(videoUrls[videoIndex]);
        mVideoView.setVideoPath(videoUrls[videoIndex]);

        if(!SmartPenApplication.getSimpleVersionFlag()){
            if (RememberUtil.getInt(Constant.MEMORY_PLAY_VIDEO_URI_KEY, 0) != 0) {
                try {
                    mVideoView.setVideoPath(videoUrls[RememberUtil.getInt(Constant.MEMORY_PLAY_VIDEO_URI_KEY, 0)]);
                    videoIndex = RememberUtil.getInt(Constant.MEMORY_PLAY_VIDEO_URI_KEY, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    mVideoView.setVideoPath(videoUrls[0]);
                    RememberUtil.putInt(Constant.MEMORY_PLAY_VIDEO_URI_KEY, 0);
                    videoIndex = 0;
                }
            }
            if (currentPosition != 0) {
                mVideoView.seekTo(currentPosition);
            }
            if (RememberUtil.getInt(Constant.MEMORY_PLAY_KEY, 0) != 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Notice：seekTo是异步操作,因为seekTo在低端pad很卡顿，所以将它已去除
                        if (false) {
                            mVideoView.seekTo(RememberUtil.getInt(Constant.MEMORY_PLAY_KEY, 0));
                        }
                    }
                }, 50);
            }
        }

        mVideoView.start();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //重置一次VideoView.因为VideoView会每次都运行,所以无需重置
                String[] videoUrls = getVideoURIs(mPath);

                if (videoUrls != null) {
                    videoIndex++;
                    if (videoIndex >= videoUrls.length) {
                        videoIndex = 0;
                    }
                    postVideoId(videoUrls[videoIndex]);
                    mVideoView.setVideoPath(videoUrls[videoIndex]);
                    RememberUtil.putInt(Constant.MEMORY_PLAY_VIDEO_URI_KEY, videoIndex);
                    mVideoView.start();
                }
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //重置一次VideoView.因为VideoView会每次都运行,所以无需重置
                String[] videoUrls = getVideoURIs(mPath);
                videoIndex++;
                if (videoUrls != null) {
                    if (videoIndex >= videoUrls.length) {
                        videoIndex = 0;
                    }
                    mVideoView.setVideoPath(videoUrls[videoIndex]);
                    RememberUtil.putInt(Constant.MEMORY_PLAY_VIDEO_URI_KEY, videoIndex);
                    mVideoView.start();
                }
                return true;
            }
        });
    }



    /**
     * 获取本地视频数组
     * 注意对该目录下的无效目录(或者隐藏目录)要进行删除
     *
     * @param path
     * @return
     */
    private String[] getVideoURIs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        File[] files = file.listFiles();
        if (files.length == 0) {
            return null;
        }
        for (File contentFile : files) {
            if (contentFile.isDirectory()) {
                FileUtil.deleteDir(contentFile);
            }
        }
        files = file.listFiles();
        String[] videoUrls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            videoUrls[i] = files[i].getAbsolutePath();
        }
        return videoUrls;
    }

    /**
     * 获取在线视频数组
     *
     * @param info
     * @return
     */
    private String[] getOnlineVideoURI(List<VideoInfo> info) {
        if (info == null) {
            return null;
        }
        String[] videoUrls = new String[info.size()];
        if (info != null) {
            if (info.size() > 0) {
                for (int i = 0; i < info.size(); i++) {
                    //注意前缀为本地拼接
                    videoUrls[i] = QuickUtils.spliceUrl(info.get(i).getVideoPath(), info.get(i).getQiniuPath());
                }
            }
        }
        return videoUrls;
    }

    private void postVideoId(String url){
        int videoId = 0;
        try {
            videoId = getVideoId(url);
            EventBus.getDefault().postSticky(new OnVideoBackEvent(videoId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getVideoId(String videoUrl)throws Exception{
        int start = videoUrl.lastIndexOf("/");
        int end = videoUrl.lastIndexOf(".");
        String substring = videoUrl.substring(start+1, end);
        int i = Integer.parseInt(substring);
        return i;
    }


}
