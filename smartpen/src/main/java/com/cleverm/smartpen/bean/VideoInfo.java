package com.cleverm.smartpen.bean;

import java.io.Serializable;

/**
 * Created by x on 2016/2/15.
 */
public class VideoInfo extends BaseInfoBean{

    private String videoPath;
    private String qiniuPath;

    private String type;

    private Long videoSize;

    public String getQiniuPath() {
        return qiniuPath;
    }

    public void setQiniuPath(String qiniuPath) {
        this.qiniuPath = qiniuPath;
    }

    public Long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Long videoSize) {
        this.videoSize = videoSize;
    }

    private int videoId;

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
