package com.cleverm.smartpen.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.pushtable.UpdateTableHandler;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.ThreadManager;
import com.cleverm.smartpen.util.excle.CreateExcel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by xiong,An android project Engineer,on 21/7/2016.
 * Data:21/7/2016  上午 09:34
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class TestExcle extends Activity {


    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_excle);
        final EditText editText = (EditText) findViewById(R.id.ed_edit);
        Button btSend = (Button) findViewById(R.id.bt_send);
        tvText = (TextView) findViewById(R.id.tv_text);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String content = editText.getText().toString();

                ThreadManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvText.setText("开始请求");
                            }
                        });


                        final long start = System.currentTimeMillis();
                        CreateExcel createExcel = new CreateExcel();
                        String execlName = createExcel.init();
                        createExcel.writeExcel(StatisticsUtil.getInstance().getDBForExcel(), execlName);
                        final long end = System.currentTimeMillis();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvText.setText("export excel time" + (end - start));
                            }
                        });

                        QuickUtils.log("export excel time" + (end - start));
                        updateExcleToService("http://"+content, StatisticsUtil.getInstance().getStatsFile());
                    }
                });


            }
        });

        findViewById(R.id.btn_send_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://172.16.0.244:8532/sms";
                OkHttpUtils.post()
                        .addParams("tableID", "1334")
                        .addParams("templateID", "20")
                        .url(url)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                QuickUtils.toast(e.getMessage());
                            }

                            @Override
                            public void onResponse(String s) {
                                QuickUtils.toast(s);
                            }
                        });
            }
        });

        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }




    public void updateExcleToService(String url, File file) {
        QuickUtils.log("updateExcleToService");


        final String UPLOAD_FILE_URL=url+"/behavior";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvText.setText("上传中,上传地址为:" + UPLOAD_FILE_URL);
            }
        });




        OkHttpUtils.post()
                .addParams("orgId", getOrgStrId())
                .addParams("tableId", getDeskId() + "")
                .addParams("path", "statistic")
                .addFile("resFile", file.getName(), file)//
                .url(UPLOAD_FILE_URL)
                //.url(StatisticsUtil.UPLOAD_FILE_URL)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e) {
                        QuickUtils.log("upload-file-onError=" + e.getMessage());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvText.setText("onError"+e.getMessage());
                            }
                        });


                    }

                    @Override
                    public void onResponse(final String result) {
                        //{"desc":"","data":"","code":"1000","err":""}
                        QuickUtils.log("upload-file-onResponse=" + result.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvText.setText(result);
                            }
                        });
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    private Long getDeskId(){
        long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        QuickUtils.log("deskId=" + deskId);
        return deskId;
    }

    private String getOrgStrId(){
        String orgId=RememberUtil.getString(UpdateTableHandler.ORGID, "8888");
        return orgId;
    }


}
