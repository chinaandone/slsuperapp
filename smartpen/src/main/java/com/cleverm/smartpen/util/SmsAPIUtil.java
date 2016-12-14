package com.cleverm.smartpen.util;

import com.cleverm.smartpen.app.SelectTableActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by xiong,An android project Engineer,on 2/8/2016.
 * Data:2/8/2016  上午 10:17
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
@Deprecated
public class SmsAPIUtil {

    private static final String TAG = "SmsAPIUtil：";

    private static final String url=Constant.DDP_URL+"/cleverm/sockjs/sendSMS";


    private static SmsAPIUtil INSTANCE = new SmsAPIUtil();

    public static SmsAPIUtil getInstance() {
        return INSTANCE;
    }

    private SmsAPIUtil() {
    }


    public void sendSms(String templateID){
        long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        if (deskId == Constant.DESK_ID_DEF_DEFAULT) {
            return;
        }
        OkHttpUtils.post()
                .url(url)
                .addParams("tableID",deskId+"")
                .addParams("templateID",templateID)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String s) {
            }
        });
    }






}
