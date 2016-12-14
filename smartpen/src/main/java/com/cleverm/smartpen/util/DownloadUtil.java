package com.cleverm.smartpen.util;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cleverm.smartpen.app.BaseDiscountActivity;
import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.service.DownloadPicassoService;
import com.cleverm.smartpen.ui.FullScreenVideoView;
import com.cleverm.smartpen.util.cache.FileRememberUtil;
import com.cleverm.smartpen.util.parts.DoDiskLruPart;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import okhttp3.Call;


/**
 * Created by xiong,An android project Engineer,on 2016/2/19.
 * Data:2016-02-19  13:41
 * Base on clever-m.com(JAVA Service)
 * Describe: Download广告视频帮助类
 * Version:1.0
 * Open source
 */
public class DownloadUtil {

    //优惠专区
    public static final String  DISOUNT_JSON="DISOUNT_JSON";
    public static final String  Dir_DISOUNT_JSON="Dir_DISOUNT_JSON";
    //本店推荐
    public static final String  DISOUNT_HEADOFFICE_JSON="DISOUNT_HEADOFFICE_JSON";
    public static final String  Dir_DISOUNT_HEADOFFICE_JSON="Dir_DISOUNT_HEADOFFICE_JSON";


    public static void getVideoFlag(final ServiceUtil.JsonInterface jsonInterface){
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
                        jsonInterface.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        jsonInterface.onSucced(response.toString());

                    }
                });
    }







    public static void preVideoFileFromService(final FullScreenVideoView vvAdvertisement) {
        String url = Constant.DDP_URL+"/api/api/v10/video/list";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("orgId", QuickUtils.getOrgIdFromSp())
                .addParams("type", "1")
                .build()
                .execute(new StringCallback() {


                    @Override
                    public void onError(okhttp3.Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            List<VideoInfo> info = JsonUtil.parser(response, VideoInfo.class);

                            //第一个视频为在线直接播放，其他所有视频为下载后再播放

                            for (int i = 0; i < info.size(); i++) {
                                DownloadUtil.downloadFile(QuickUtils.spliceUrl(info.get(i).getVideoPath(),info.get(i).getQiniuPath()), (i + 1) + "");
                            }

                            VideoUtil videoUtil = new VideoUtil(vvAdvertisement);
                            videoUtil.prepareOnlineVideo(info);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                });
    }


    /**
     * 优惠专区的Json数据
     *   type ： 0表示只显示商户，即请求本店推荐数据，1表示显示商户和商业，即请求优惠专区的数据
     * @param orgId 餐厅Id
     */
    public static void cacheDiscountJson(String orgId) {
        String url = Constant.DDP_URL+"/api/api/v10/roll/main/list";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("orgId", orgId)
                .addParams("type", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        //存储起来,准备特惠专区界面使用
                        //RememberUtil.putStringSync(DISOUNT_JSON, response);
                        if (FileRememberUtil.has(Dir_DISOUNT_JSON)) {
                            FileRememberUtil.put(response, Dir_DISOUNT_JSON, DISOUNT_JSON);
                        }
                        DoDiskLruPart.getInstance().put(BaseDiscountActivity.AllKey,response);
                        //FileCacheUtil.get(CleverM.getApplication()).put(DISOUNT_JSON, response);
                        Intent intent = new Intent(SmartPenApplication.getApplication(), DownloadPicassoService.class);
                        intent.putExtra(DownloadPicassoService.PICASSO_JSON, response);
                        SmartPenApplication.getApplication().startService(intent);

                    }
                });

        OkHttpUtils
                .get()
                .url(url)
                .addParams("orgId", orgId)
                .addParams("type", "0")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        //QuickUtils.toast(response);
                        //存储起来,准备本店推荐界面使用
                        //RememberUtil.putStringSync(DISOUNT_HEADOFFICE_JSON, response);
                        if(FileRememberUtil.has(Dir_DISOUNT_HEADOFFICE_JSON)){
                            FileRememberUtil.put(response,Dir_DISOUNT_HEADOFFICE_JSON,DISOUNT_HEADOFFICE_JSON);
                        }
                        //FileCacheUtil.get(CleverM.getApplication()).put(DISOUNT_HEADOFFICE_JSON, response);
                        DoDiskLruPart.getInstance().put(BaseDiscountActivity.OnlyKey,response);
                    }
                });



    }

    /**
     * online down Video File
     * 1.出现异常的情况：删除视频 ，然后 重新下载  , 下载两次
     *
     * 2.两次后依然异常，会记录问题url，然后定时再次去进行下载
     *
     * @param path
     * @param num
     */
    public static void downloadFile(String path, final String num) {



        //存储的地址为storage/emulated/0/muye/木爷我们的视频.mp4
        OkHttpUtils//
                .get()//
                .url(path)//
                .build()//
                .execute(new FileCallBack(AlgorithmUtil.VIDEO_FILE, num + ".mp4")//
                {
                    @Override
                    public void inProgress(float progress) {
                        //Log.i("FILE", "onResponse :" + progress);
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        Log.i("FILE", "onResponse onError:" + e.getMessage());
                        //发生下载异常后
                        try {
                            QuickUtils.deleteFile(AlgorithmUtil.VIDEO_FILE +File.separator+ num + ".mp4");

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }


                    @Override
                    public void onResponse(File file) {
                        Log.i("FILE", "onResponse onResponse:" + file.getAbsolutePath());
                    }
                });
    }




}
