package com.cleverm.smartpen.util;

import android.media.MediaPlayer;
import android.widget.VideoView;

import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.log.*;

import java.io.File;

/**
 * Created by xiong,An android project Engineer,on 16/5/2016.
 * Data:16/5/2016  下午 03:17
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DemoVideoUtil {

    private static DemoVideoUtil INSTANCE = new DemoVideoUtil();

    private DemoVideoUtil(){

    }

    public static DemoVideoUtil getInstance(){
        return INSTANCE;
    }

    /**
     * 播放本地的视频,不关心是否含有播放节点信息
     */
    public void prepareLocalVideoKeptGo(final String mPath,final VideoView videoView){
        final int inode = 0;
        final String[] videoUrls = getVideoURIs(mPath);
        if (videoUrls == null || videoUrls.length <=0) {
            return;
        }
        videoView.setVideoPath(videoUrls[inode]);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                String[] videoUrls = getVideoURIs(mPath);
                videoView.setVideoPath(videoUrls[inode]);
                videoView.start();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                String[] videoUrls = getVideoURIs(mPath);
                videoView.setVideoPath(videoUrls[inode]);
                videoView.start();
                return true;
            }
        });
    }

    public void prepareOnlineVideo(final VideoInfo video,final VideoView videoView){
        if(video.getQiniuPath()==null){
            return;
        }
        videoView.setVideoPath(QuickUtils.spliceUrl(video.getVideoPath(), video.getQiniuPath()));
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoPath(QuickUtils.spliceUrl(video.getVideoPath(), video.getQiniuPath()));
                videoView.start();
            }
        });

    }

    /**
     * 获取本地视频数组
     * 注意对该目录下的无效目录(或者隐藏目录)要进行删除
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
        for(File contentFile : files){
            if(contentFile.isDirectory()){
                com.cleverm.smartpen.log.FileUtil.deleteDir(contentFile);
            }
        }
        files=file.listFiles();
        String[] videoUrls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            videoUrls[i] = files[i].getAbsolutePath();
        }
        return videoUrls;
    }
}
