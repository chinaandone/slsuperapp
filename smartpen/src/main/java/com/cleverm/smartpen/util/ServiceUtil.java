package com.cleverm.smartpen.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cleverm.smartpen.bean.DiscountInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * Created by xiong,An android project Engineer,on 2016/2/21.
 * Data:2016-02-21  12:04
 * Base on clever-m.com(JAVA Service)
 * Describe: 所有网络请求和Json解析
 * Version:1.0
 * Open source
 */
public class ServiceUtil {

    private static ServiceUtil INSTANCE = new ServiceUtil();

    public static ServiceUtil getInstance() {
        return INSTANCE;
    }


    public interface JsonInterface {
        void onSucced(String json);

        void onFail(String error);
    }


    /**
     * Url地址:http://120.25.159.173:8280/api/api/v10/roll/main/list?orgId=100&type=1
     * 1代表 优惠专区
     * 2代表 本店推介
     * @param orgId
     */
    public void getDiscountData(String orgId, String type, final JsonInterface jsonInterface) {
        String url = Constant.DDP_URL+"/api/api/v10/roll/main/list";
        Log.e("getDiscountData",url+"orgId="+orgId);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("orgId", orgId)
                .addParams("type", type)
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


    /**
     * 解析优惠专区Json
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public List<DiscountInfo> parserDiscountData(String result) throws JSONException {
        JSONObject json = new JSONObject(result);
        String data = json.getString("data");
        List<DiscountInfo> list = JSON.parseArray(data, DiscountInfo.class);
        return list;
    }

    public <T> T parserSingleData(String result,Class<T> clazz) throws JSONException {
        JSONObject json = new JSONObject(result);
        String data = json.getString("data");
        T object = JSON.parseObject(data, clazz);
        return object;
    }


    public void getDemoVideoData(String vBId,String type,final JsonInterface jsonInterface){
        String url = Constant.DDP_URL+"/api/api/v10/video/get.do";
        OkHttpUtils.get()
                .url(url)
                .addParams("vBId", vBId)
                .addParams("type", type)
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

    //add by Randy for Ble Call Service
    public void getBleSetInfo(String orgID,String tableID,final JsonInterface jsonInterface){
        String url = Constant.DDP_URL+"/api/api/v10/watch/setinfo.do";
//        String url = "http://192.168.0.102:8081"+"/api/v10/watch/setinfo.do";
        OkHttpUtils.get()
                .url(url)
                .addParams("orgId", orgID)
                .addParams("tableId", tableID)
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

    //add by Randy for Ble heartBeat
    public void updateHeartBeat(String orgID,String tableID,String versioncode,String versionname,final JsonInterface jsonInterface){
        String url = Constant.DDP_URL+"/api/api/v10/heartbeat/beatinfo.do";
//        String url = "http://192.168.0.102:8081"+"/api/v10/heartbeat/beatinfo.do";
        OkHttpUtils.get()
                .url(url)
                .addParams("orgId", orgID)
                .addParams("tableId", tableID)
                .addParams("versioncode",versioncode)
                .addParams("versionname",versionname)
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


}
