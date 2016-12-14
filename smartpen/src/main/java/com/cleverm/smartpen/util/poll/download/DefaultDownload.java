package com.cleverm.smartpen.util.poll.download;

import android.util.Log;

import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.CopyFileUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.ThreadManager;
import com.cleverm.smartpen.util.poll.download.resumepoint.BreakPoint;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by xiong,An android project Engineer,on 7/9/2016.
 * Data:7/9/2016  下午 03:58
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DefaultDownload<T> implements Download<T> {

    //队列
    private Queue mQueue;
    //断点
    private BreakPoint mBreakPoint;


    public DefaultDownload(Queue queue,BreakPoint breakPoint){
        this.mQueue=queue;
        this.mBreakPoint=breakPoint;
    }

    @Override
    public void download(T t1) {
        List<VideoInfo> infos = (ArrayList<VideoInfo>) t1;
        BlockingQueue<VideoInfo> queue = mQueue.init();
        for (int i = 0; i < infos.size(); i++) {
            mQueue.put(queue, infos.get(i));
        }
        VideoInfo info = queue.poll();
        executDownload(info, queue);
    }

    private void executDownload(final VideoInfo info, final BlockingQueue<VideoInfo> queue) {
        final String url  =  QuickUtils.spliceUrl(info.getVideoPath(), info.getQiniuPath());
        final String num  =  info.getVideoId() + "";
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .connTimeOut(60000)
                .readTimeOut(60000)
                .writeTimeOut(60000)
                .execute(new FileCallBack(AlgorithmUtil.VIDEO_FILE, num + ".mp4")//
                {
                    @Override
                    public void inProgress(float progress) {
                        Log.i("FILE", "onResponse inProgress " + num + ".mp4 :" + progress);
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        Log.i("FILE", "onResponse onError :" + e.getMessage());
                        executDownload(info, queue);//下载视频失败去做retry机制
                    }


                    @Override
                    public void onResponse(File file) {
                        Log.i("FILE", "onResponse onResponse:" + file.getAbsolutePath());
                        ThreadManager.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                CopyFileUtil.copyByNIO(AlgorithmUtil.VIDEO_FILE + File.separator + num + ".mp4", AlgorithmUtil.VIDEO_FILE_PLAY + File.separator + num + ".mp4", false);
                                VideoInfo info = queue.poll();
                                if (info != null) {
                                    executDownload(info, queue);
                                }
                            }
                        });
                    }
                });



    }


}
