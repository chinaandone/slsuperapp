package com.cleverm.smartpen.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;
/**
 * Created by xiong,An android project Engineer,on 2016/2/15.
 * Data:2016-02-15  15:08
 * Base on clever-m.com(JAVA Service)
 * Describe: 设置VideoView全屏
 * Version:1.0
 * Open source
 */
public class FullScreenVideoView extends VideoView {

    private int mVideoWidth;
    private int mVideoHeight;

    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //下面的代码是让视频的播放的长宽是根据你设置的参数来决定
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
