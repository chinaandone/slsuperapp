package com.cleverm.smartpen.pushtable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.cleverm.smartpen.util.Constant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Call;


public class HotfixHandler extends NoticeHandler<HotfixSetVo, Void> {
    public static final String TAG = HotfixVo.class.getSimpleName();
    private String mNewVerName=Constant.APP_NAME;
    public static final String TARGET = "/sdcard/aaa.zip";
    private Context mContext;

    public HotfixHandler(Context context) {
        super("CONTENT_GENERIC");
        mContext = context;
        Log.v(TAG,"HotfixHandler Response<Void> onSuccess");
    }

    @Override
    public Response<Void> onSuccess(Headers headers, HotfixSetVo in) {
        //
        Log.v(TAG,"Response<Void> onSuccess");
        if(in == null){
            Log.d(TAG, "downloadUrl=null");
            return null;
        }
        Log.d(TAG, "downloadUrl="+in.getPublisher());
        Set<HotfixVo> set = in.getHotfixSet();
        Iterator<HotfixVo> iterator = set.iterator();
        while(iterator.hasNext()){
            HotfixVo hotfixVo = iterator.next();
            String url = hotfixVo.getUrl();
            if(url !=null){
                downFile(url);
                Log.d(TAG, "downloadUrl="+url);
                break;
            }
        }
        return null;
    }

    private  void downFile(String url) {
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getPath(),mNewVerName) {
                    @Override
                    public void inProgress(float v) {

                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(File file) {
                        down();
                    }
                });
    }

    @Override
    public void onError(int code, String message) {
        // TODO Auto-generated method stub
        
    }


    /**
     *
     */
    private  void down() {
        new Handler().post(new Runnable() {
            public void run() {
                update();
            }
        });
    }

    /**
     *
     */
    void  update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), mNewVerName)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    class HotfixCallback extends RequestCallBack<File> {

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            Log.d(TAG, arg0.getMessage()+"==="+arg1);
        }

        @Override
        public void onSuccess(ResponseInfo<File> arg0) {
            
        }
        
    }

}
