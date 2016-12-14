package com.cleverm.smartpen.util;

import com.alibaba.fastjson.JSON;
import com.bleframe.library.log.BleLog;
import com.cleverm.smartpen.bean.DiscountInfo;
import com.cleverm.smartpen.bean.LuckyPrizeIdInfo;
import com.cleverm.smartpen.bean.LuckyPrizeInfo;
import com.cleverm.smartpen.bean.event.OnLuckyEvent;
import com.cleverm.smartpen.util.parts.DoDiskLruPart;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by xiong,An android project Engineer,on 2/9/2016.
 * Data:2/9/2016  下午 07:46
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class LuckyDrawUtil {

    private static final String TAG=LuckyDrawUtil.class.getSimpleName()+"：";

    private static LuckyDrawUtil INSTANCE = new LuckyDrawUtil();

    private int CONNECT_TIME_OUT=10000;

    public static List<LuckyPrizeInfo> mMemoryCache=null;

    private static final String URL_LUCKY_LIST="http://www.myee7.com/tarot/services/public/listOfClientPrize";
    private static final String URL_LUCKY_PRIZEID="http://www.myee7.com/tarot/services/public/getRandomClientPrizeInfo";
    private static final String URL_LUCKY_RECEIVE="http://www.myee7.com/tarot/services/public/confirmPriceInfo";
    private static final String URL_CANCEL_PRIZE="http://www.myee7.com/tarot/services/public/backToPrizePool";

    public static final String URL_SPLITE_IMAGE="http://myee7.com/push/";

    private String CODE_OK="1000";
    public static final String LUCKY_LIST_KEY="LUCKYDRAWUTILKEY";

    private String GOT_PRIZE_SUCCESS_MESSAGE="稍后将发送短信到您手机上，请凭短信码到前台领取奖品";
    private String GOT_PRIZE_FAIL_UNKNOW="UNKNOW ERROR";



    public static LuckyDrawUtil getInstance() {
        return INSTANCE;
    }

    private LuckyDrawUtil() {

    }

    /**
     * 进入界面就请求
     */
    public void getPrizeList(){
        if(NetWorkUtil.hasNetwork()){
            doLuckyList();
        }else{
            deliverDefaultPirzeList();
        }
    }

    /**
     * 点击抽奖操作
     */
    public void getPrizeId(){
        if(NetWorkUtil.hasNetwork()){
            doGetPrize();
        }else{
            deliverDefaultPrizeId();
        }
    }

    public void inputphone(String phone,String prizeId){
        if(NetWorkUtil.hasNetwork()){
            doInputPhone(phone,prizeId);
        }else{
            QuickUtils.toast("需要联网操作...");
        }
    }




    public void cancelPrize(String prizeId){
        if(NetWorkUtil.hasNetwork()){
            doCancel(prizeId);
        }else{
            doTimeTry();
        }
    }

    /**
     * 定时器
     * notice:暂时不处理
     */
    private void doTimeTry() {
    }


    /**
     * 获取服务端奖品列表
     */
    private void doLuckyList() {
        String orgId = QuickUtils.getOrgIdFromSp();
        OkHttpUtils.post()
                .addParams("orgId", orgId)
                .url(URL_LUCKY_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        deliverDefaultPirzeList(); //connect time out
                    }

                    @Override
                    public void onResponse(String json) {
                        QuickUtils.log(TAG+"Lucky-Server-List："+json);
                        deliverServerPrizeList(json);
                    }
                });
    }

    /**
     * 获取奖品
     */
    private void doGetPrize() {
        String orgId = QuickUtils.getOrgIdFromSp();
        Long deskId = QuickUtils.getDeskId();
        OkHttpUtils.post()
                .addParams("orgId", orgId)
                .addParams("deskId",String.valueOf(deskId))
                .url(URL_LUCKY_PRIZEID)
                .build()
                .connTimeOut(CONNECT_TIME_OUT)
                .readTimeOut(CONNECT_TIME_OUT)
                .writeTimeOut(CONNECT_TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        deliverDefaultPrizeId();
                    }

                    @Override
                    public void onResponse(String json) {
                        QuickUtils.log(TAG+"Lucky-Server-prizeid："+json);
                        deliverServerPrizeId(json);
                    }
                });

    }

    /**
     * 输入手机号码
     */
    private void doInputPhone(String phone,String prize) {
        Long deskId = QuickUtils.getDeskId();

        OkHttpUtils.post()
                .addParams("phoneNum", phone)
                .addParams("deskId", String.valueOf(deskId))
                .addParams("prizeGetId", prize)
                .url(URL_LUCKY_RECEIVE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String result) {
                        QuickUtils.log(TAG+"Lucky-Server-phone："+result);
                        try {
                            JSONObject json = new JSONObject(result);
                            String code = json.getString("code");
                            if(code.equals(CODE_OK)){
                                EventBus.getDefault().post(new OnLuckyEvent(OnLuckyEvent.Type.gotPrizeSuccess,GOT_PRIZE_SUCCESS_MESSAGE));
                            }else{
                                String desc = json.getString("desc");
                                EventBus.getDefault().post(new OnLuckyEvent(OnLuckyEvent.Type.gotPrizeFail,desc));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            EventBus.getDefault().post(new OnLuckyEvent(OnLuckyEvent.Type.gotPrizeFail, GOT_PRIZE_FAIL_UNKNOW));
                        }
                    }
                });
    }

    private void doCancel(String prizeId){
        OkHttpUtils.post()
                .addParams("deskId",String.valueOf(QuickUtils.getDeskId()))
                .addParams("prizeGetId",prizeId)
                .url(URL_CANCEL_PRIZE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(String result) {
                        QuickUtils.log(TAG+"Lucky-Server-cancel："+result);
                    }
                });
    }





    /**
     * 展示奖品列表:默认(存储有就展示存储的，若没有则全部展示“谢谢惠顾”)
     */
    private void deliverDefaultPirzeList() {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                String json = DoDiskLruPart.getInstance().get(LUCKY_LIST_KEY);
                if (json != null) {
                    try {
                        List<LuckyPrizeInfo> list = JsonUtil.parser(json, LuckyPrizeInfo.class);
                        EventBus.getDefault().post(new OnLuckyEvent(OnLuckyEvent.Type.defaultPrizeList, list));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //doNothing
                }
            }
        });
    }

    /**
     * 展示奖品列表：服务端奖品
     */
    private void deliverServerPrizeList(final String json) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                //磁盘存储
                DoDiskLruPart.getInstance().put(LUCKY_LIST_KEY,json);
                try {
                    List<LuckyPrizeInfo> list =  JsonUtil.parser(json, LuckyPrizeInfo.class);
                    //内存存储
                    mMemoryCache=list;
                    EventBus.getDefault().post(new OnLuckyEvent(OnLuckyEvent.Type.ServerPrizeList, list));
                } catch (Exception e) {
                    e.printStackTrace();
                    deliverDefaultPirzeList();
                }
            }
        });
    }

    /**
     * 展示中奖：谢谢惠顾
     */
    private void deliverDefaultPrizeId() {
        EventBus.getDefault().post(new OnLuckyEvent(OnLuckyEvent.Type.defaultPrizeId,""));
    }

    /**
     * 展示中奖：服务端奖品
     */
    private void deliverServerPrizeId(String result){
        try {
            JSONObject json = new JSONObject(result);
            String code = json.getString("code");
            if(code.equals(CODE_OK)){
                String data = json.getString("data");
                LuckyPrizeIdInfo luckyPrizeIdInfo = JSON.parseObject(data, LuckyPrizeIdInfo.class);
                EventBus.getDefault().post(new OnLuckyEvent(OnLuckyEvent.Type.ServerPrizeId,luckyPrizeIdInfo));
                QuickUtils.doStatistic(StatisticsUtil.APP_PRIZE_SERVER_LUCKYID,StatisticsUtil.APP_PRIZE_SERVER_LUCKYID_DESC,Long.parseLong(luckyPrizeIdInfo.getPrizeGetId()+""));
            }else{
                deliverDefaultPrizeId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deliverDefaultPrizeId();
        }
    }


}
