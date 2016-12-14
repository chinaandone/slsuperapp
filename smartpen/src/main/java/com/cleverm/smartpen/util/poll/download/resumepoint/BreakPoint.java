package com.cleverm.smartpen.util.poll.download.resumepoint;

import java.io.File;

/**
 * Created by xiong,An android project Engineer,on 8/9/2016.
 * Data:8/9/2016  上午 11:36
 * Base on clever-m.com(JAVA Service)
 * Describe: 断点续传
 * Version:1.0
 * Open source
 */
public class BreakPoint implements ProgressResponseBody.ProgressListener {




    /**
     * 下载
     * @param file
     * @param breakPoints
     * @param downloadPath
     */
    public void startDownload(File file,int breakPoints,String downloadPath){
        ResumeDownload downloader = new ResumeDownload(downloadPath, file, BreakPoint.this);
        downloader.download(breakPoints);
    }


    @Override
    public void onPreExecute(long contentLength) {

    }

    @Override
    public void update(long totalBytes, long contentLength, boolean done) {
        //this.totalBytes = totalBytes + breakPoints;
    }
}
