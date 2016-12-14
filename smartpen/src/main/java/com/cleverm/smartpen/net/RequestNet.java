package com.cleverm.smartpen.net;


import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.QuickUtils;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

/**
 * Created by 95 on 2016/1/9.
 */
public class RequestNet {
    public static final String TAG=RequestNet.class.getCanonicalName();
    public static final String PATH= Constant.NET_PATH;
//    public static AndroidHttpClient httpClient=null;
    public static InfoSendSMSVo getData(InfoSendSMSVo infoSendSMSVo){
        try {
            Log.v(TAG,"send message===");
            InfoSendSMSVo vo = requestNotice("SEND_SMSINTO", infoSendSMSVo,
                    InfoSendSMSVo.class);
            if(vo==null){
                Log.v(TAG,"InfoSendSMSVo vo=null");
                return null;
            }else {
                Log.v(TAG,"InfoSendSMSVo vo="+vo.toString());
                return vo;
            }
        } catch (Exception e) {

            System.out.println("---------- InfoSendSMSVo Exception --------");

            e.printStackTrace();
            return null;
        }
    }
    private static <Tin, Tout> Tout requestNotice(String noticeType, Object in, Class<Tout> outClass)
            throws Exception {

        Message message =
                Message.create().messageType(MessageType.NOTIFICATION).header("Notice-Type", noticeType).json(in).build();

        QuickUtils.log("requestNotice-Message"+message.toString());

        return requestByPost(message, outClass);
    }


    public static <T> T requestByPost(Message message, Class<T> clazz) {
        return requestByPost(message, 50000, clazz);
    }

    public static  <T> T requestByPost(Message message, int timeout, Class<T> clazz) {
        AndroidHttpClient httpClient=null;
        try {
            HttpPost post = new HttpPost(PATH);
            post.setEntity(new StringEntity(Message.toRawString(message), "utf-8"));
            httpClient = createHttpClient();
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseText = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                Log.v(TAG, responseText+"$$$$$$$$$");
                Response<?> response = Response.parse(responseText);
                Log.v("ttt", "body = " + response.getBody());
                return JSON.parseObject(response.getBody(), clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            httpClient.close();
        }
        return null;
    }

    private static AndroidHttpClient createHttpClient() {
        try {
            String userAgent = "Dalvik/1.6.0 (Linux; u; Android 4.2.2; sdk Build/JB_MR1.1)";
            AndroidHttpClient httpClient = AndroidHttpClient.newInstance(userAgent);
            return httpClient;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
