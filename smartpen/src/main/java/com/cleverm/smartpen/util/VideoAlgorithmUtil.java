package com.cleverm.smartpen.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.service.DownloaderDifferenceService;
import com.cleverm.smartpen.service.DownloaderService;
import com.cleverm.smartpen.ui.FullScreenVideoView;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by xiong,An android project Engineer,on 2016/2/24.
 * Data:2016-02-24  14:45
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class VideoAlgorithmUtil {

    private static VideoAlgorithmUtil INSTANCE = new VideoAlgorithmUtil();

    public static VideoAlgorithmUtil getInstance() {
        return INSTANCE;
    }

    private VideoAlgorithmUtil() {

    }

    public interface videoInterface {
        void onSucess(String json);

        void onFail(String error);
    }


    /**
     * 第一次从服务端直接读取并存储到本地
     *
     * @param vvAdvertisement
     */
    public void getVideoFirst(final FullScreenVideoView vvAdvertisement,final Activity activity) {

        videoAPI(new videoInterface() {
            @Override
            public void onSucess(String json) {
                try {
                    List<VideoInfo> info = JsonUtil.parser(json, VideoInfo.class);

                    //第一个视频为在线直接播放，其他所有视频为下载后再播放
                    downloadVideoByService(activity,info);

                    VideoUtil videoUtil = new VideoUtil(vvAdvertisement);
                    videoUtil.prepareOnlineVideo(info);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });

    }


    public void downloadVideoByService(Activity activity , List<VideoInfo> info){
        /*//第一个视频为在线直接播放，其他所有视频为下载后再播放
        for (int i = 0; i < info.size(); i++) {
             //downloadVideoFirstByVolley(QuickUtils.spliceUrl(info.get(i).getVideoPath()), info.get(i).getVideoId() + "");
             downloadVideoFirst(QuickUtils.spliceUrl(info.get(i).getVideoPath()), info.get(i).getVideoId() + "");
        }*/

        Intent intent = new Intent(activity, DownloaderService.class);
        intent.putExtra(DownloaderService.SERVICE_OBJECT, (Serializable) info);
        activity.startService(intent);
    }

    public void downloadDifferVideoByService(Activity activity , List<VideoInfo> info,HashMap<String,String> map){
        Intent intent = new Intent(activity, DownloaderDifferenceService.class);
        intent.putExtra(DownloaderDifferenceService.SERVICE_DIFFERENCE_LIST, (Serializable) info);

        //传递map是通过外层嵌套一个List
        Bundle bundle=new Bundle();
        ArrayList bundlelist = new ArrayList();
        bundlelist.add(map);
        bundle.putParcelableArrayList(DownloaderDifferenceService.SERVICE_CONTAINSKEY_MAP, bundlelist);
        intent.putExtras(bundle);

        activity.startService(intent);
    }



    /**
     * 第一次下载所有视频的方法
     *
     * @param path
     * @param num
     */
    public void downloadVideoFirst(final String path, final String num) {

        //存储的地址为storage/emulated/0/muye/木爷我们的视频.mp4
        OkHttpUtils//
                .get()//
                .url(path)//
                .build()//
                .connTimeOut(60000)
                .readTimeOut(60000)
                .writeTimeOut(60000)
                .execute(new FileCallBack(AlgorithmUtil.VIDEO_FILE, num + ".mp4")//
                {
                    @Override
                    public void inProgress(float progress) {
                        Log.i("FILE", "onResponse inProgress " +num+".mp4 :"+ progress);
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        Log.i("FILE", "onResponse onError :" + e.getMessage());
                        //下载视频失败去做retry机制
                        downloadVideoFirst(path,num);
                    }


                    @Override
                    public void onResponse(File file) {
                        Log.i("FILE", "onResponse onResponse:" + file.getAbsolutePath());
                        //copy操作
                        //CopyFileUtil.copyFile(AlgorithmUtil.VIDEO_FILE+File.separator+num + ".mp4",AlgorithmUtil.VIDEO_FILE_PLAY+File.separator+num+".mp4",false);
                        CopyFileUtil.copyByNIO(AlgorithmUtil.VIDEO_FILE+File.separator+num + ".mp4",AlgorithmUtil.VIDEO_FILE_PLAY+File.separator+num+".mp4",false);

                    }
                });
    }


    private void videoAPI(final videoInterface videoInterface) {
        String url = Constant.DDP_URL+"/api/api/v10/video/list";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("orgId", QuickUtils.getOrgIdFromSp())
                .addParams("type", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        videoInterface.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(String s) {
                        videoInterface.onSucess(s.toString());
                    }
                });
    }


    private HashMap<String, String> serviceFielsMap = new HashMap<String, String>();


    /**
     * @deprecated Because of BUG ~~
     * 轮询检查VideoId是否存在
     */
    public void loopFileName(final FullScreenVideoView videoView) {

        videoAPI(new videoInterface() {
            @Override
            public void onSucess(String json) {
                try {
                    //服务器上的videoId
                    List<VideoInfo> info = JsonUtil.parser(json, VideoInfo.class);
                    //本地的videoId
                    File[] files = QuickUtils.getVideoFolderFiles();


                    QuickUtils.log("Video--------" + files[0].getName());


                    for (int i = 0; i < info.size(); i++) {
                        serviceFielsMap.put(info.get(i).getVideoId() + "", info.get(i).getVideoPath());
                    }

                    QuickUtils.log("Video--------serviceFielsMap=" + serviceFielsMap.size());


                    for (int i = 0; i < files.length; i++) {
                        //拿本地的FileName
                        String fileName = files[i].getName();

                        //过滤单词101.mp4  为101
                        String num_fileName = fileName.substring(0, fileName.length() - 4);

                        QuickUtils.log("Video----num_fileName----" + num_fileName);


                        //1.如果服务端有这个key,表示这个视频本地有,我们不做任何处理
                        if (serviceFielsMap.containsKey(num_fileName)) {

                        }

                        //2.如果服务端没有这个key.但是本地是有这个key的,表示本地的这个视频就是要被删除的
                        if (!serviceFielsMap.containsKey(num_fileName)) {
                            QuickUtils.log("Video----deleteFile----");
                            QuickUtils.deleteFile(AlgorithmUtil.VIDEO_FILE + num_fileName + ".mp4");
                        }

                        //3.如果服务端有这个key,同时本地没有服务端有的key，我们就要去下载
                        for (Map.Entry<String, String> entry : serviceFielsMap.entrySet()) {

                            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

                            //如果fileName中没有该key,就直接下载
                            if (!entry.getKey().equals(num_fileName) && (!serviceFielsMap.containsKey(num_fileName))) {


                                //如果map中没有这个key才会走下面
                                //if(!serviceFielsMap.containsKey(num_fileName)){


                                //Video----downloadVideoFirst----entry.getKey()=100/num_fileName101
                                QuickUtils.log("Video----downloadVideoFirst----entry.getKey()=" + entry.getKey() + "/num_fileName" + num_fileName);

                                QuickUtils.log("Video----downloadVideoFirst----serviceUrl=" + entry.getValue());

                                String serviceUrl = entry.getValue();
                                downloadVideoFirst(serviceUrl, entry.getKey());


                                //}

                            }

                        }


                    }

                    //因为无法判断多个视频在下载时多久结束,所以我只读这个目录中的,而且是循环的读
                    VideoUtil videoUtil = new VideoUtil(videoView);
                    videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE, 0);


                } catch (Exception e) {
                    e.printStackTrace();
                    VideoUtil videoUtil = new VideoUtil(videoView);
                    videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE, 0);
                }


            }

            @Override
            public void onFail(String error) {
                VideoUtil videoUtil = new VideoUtil(videoView);
                videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE, 0);
            }
        });
    }


    private HashMap<String, String> serviceList = new HashMap<String, String>();
    private HashMap<String, String> LocalList = new HashMap<String, String>();


    /**
     * @param videoView
     */
    public void loopFileName2(final FullScreenVideoView videoView,final Activity activity) {

        videoAPI(new videoInterface() {
            @Override
            public void onSucess(String json) {
                try {
                    //服务器上的videoId
                    List<VideoInfo> infos = JsonUtil.parser(json, VideoInfo.class);

                    //本地的videoId
                    File[] files = QuickUtils.getVideoFolderFiles();


                    for (int i = 0; i < infos.size(); i++) {
                        serviceList.put(infos.get(i).getVideoId() + "", infos.get(i).getVideoPath());
                    }

                    for (int i = 0; i < files.length; i++) {
                        String fileNameNum = QuickUtils.subVideoEnd(files[i].getName());
                        LocalList.put(fileNameNum, AlgorithmUtil.VIDEO_FILE + fileNameNum + ".mp4");
                    }

                    QuickUtils.log("Video----serviceList----" + serviceList.size() + "/LocalList" + LocalList.size());


                    //Step1:服务器无,本地有(服务器没有本地的那个key)
                    for (int i = 0; i < files.length; i++) {
                        if (!serviceList.containsKey(QuickUtils.subVideoEnd(files[i].getName()))) {
                            QuickUtils.log("Video----LocalList----删除=" + AlgorithmUtil.VIDEO_FILE + File.separator + QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
                            //删除
                            QuickUtils.deleteFile(AlgorithmUtil.VIDEO_FILE +File.separator+QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
                            QuickUtils.deleteFile(AlgorithmUtil.VIDEO_FILE_PLAY +File.separator+QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
                        }
                    }

                    QuickUtils.log("copyFile："+"LocalList.size()=" + LocalList.size());

                    //Step2:若服务器，本地也有,则比对大小,然后决定是否删除.删除后重新得到新的localList集合
                    for(int i = 0; i < files.length; i++){
                        //服务端有这个key
                        if(serviceList.containsKey(QuickUtils.subVideoEnd(files[i].getName()))){
                            //然后迭代拿到服务端的这个key   和   本地的这   name
                            for(int j =0 ; j<infos.size();j++){
                                String ser_id = String.valueOf(infos.get(j).getVideoId());
                                String loc_id = QuickUtils.subVideoEnd(files[i].getName());
                                QuickUtils.log("copyFile：" + "ser_id=" + ser_id + "/loc_id=" + loc_id);
                                if(ser_id.equals(loc_id)){
                                    QuickUtils.log("copyFile：" + "ser_id2=" + ser_id + "/loc_id2=" + loc_id);
                                    Long Service_VideoSize = infos.get(j).getVideoSize();
                                    long Local_VideoSize = getVideoFileLength(AlgorithmUtil.VIDEO_FILE +File.separator+QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
                                    QuickUtils.log("copyFile：" + "Service_VideoSize=" + Service_VideoSize + "/Local_VideoSize=" + Local_VideoSize);
                                    if(Local_VideoSize!=Service_VideoSize){

                                        QuickUtils.deleteFile(AlgorithmUtil.VIDEO_FILE +File.separator+QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
                                        //更新集合
                                        LocalList.remove(QuickUtils.subVideoEnd(files[i].getName()));
                                    }
                                }
                            }
                        }
                    }

                    QuickUtils.log("copyFile：" + "LocalList.remove.size()=" + LocalList.size());

                    //Step3:服务器有,本地无(本地没有服务器的那个key)
                    downloadDifferVideoByService(activity,infos,LocalList);
                    /*for (int i = 0; i < infos.size(); i++) {
                        if (!LocalList.containsKey(infos.get(i).getVideoId() + "")) {
                            QuickUtils.log("Video----serviceList----下载路径=" + infos.get(i).getVideoPath());
                            //下载
                            downloadVideoFirst(QuickUtils.spliceUrl(infos.get(i).getVideoPath()), infos.get(i).getVideoId() + "");
                        }
                    }*/



                    //因为无法判断多个视频在下载时多久结束,所以我只读这个目录中的,而且是循环的读
                    VideoUtil videoUtil = new VideoUtil(videoView);
                    //videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE, 0);
                    videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE_PLAY, 0);


                } catch (Exception e) {
                    e.printStackTrace();
                    VideoUtil videoUtil = new VideoUtil(videoView);
                    videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE_PLAY, 0);
                }

            }

            @Override
            public void onFail(String error) {
                VideoUtil videoUtil = new VideoUtil(videoView);
                videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE_PLAY, 0);
            }
        });


    }




    private void downloadVideoFirstByVolley(String path, String num) {

        Uri downloadUri = Uri.parse(path);
        Uri destinationUri = Uri.parse(AlgorithmUtil.VIDEO_FILE + File.separator + num + ".mp4");

        final DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setRetryPolicy(new DefaultRetryPolicy(50000,3,1f))
                .setStatusListener(new DownloadStatusListenerV1() {
                    @Override
                    public void onDownloadComplete(DownloadRequest downloadRequest) {
                        QuickUtils.log("######## onDownloadComplete");
                    }

                    @Override
                    public void onDownloadFailed(DownloadRequest request, int errorCode, String errorMessage) {
                        QuickUtils.log("######## onDownloadFailed");
                    }

                    @Override
                    public void onProgress(DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
                        QuickUtils.log("######## onProgress ###### " + request.getDownloadId() + " : " + totalBytes + " : " + downloadedBytes + " : " + progress);
                    }
                });

        SmartPenApplication.getThinDownloadManager().add(downloadRequest);


    }


    public long getVideoFileLength(String sPath){
        File file = new File(sPath);
        return file.length();
    }


}
