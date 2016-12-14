package com.cleverm.smartpen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.ThreadManager;
import com.cleverm.smartpen.util.common.EasyCommonInfo;
import com.cleverm.smartpen.util.online.InformationOnline;
import com.cleverm.smartpen.util.online.JsonStaus;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;

/**
 * Created by xiong,An android project Engineer,on 22/8/2016.
 * Data:22/8/2016  下午 04:46
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class AlarmReceive extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(InformationOnline.ALARM_ACTION)) {
            final int intExtra = intent.getIntExtra(InformationOnline.ALARM_INTENT, 1);
            QuickUtils.log("InformationOnline AlarmReceive intExtra=" + intExtra);
            ThreadManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    QuickUtils.log("InformationOnline   " + "getOrgId = " + getOrgId() + "   getClientId = " + getClientId() +
                            " getTableId = " + getTableId() + "  getBoardNo=" + getBoardNo() +
                            " getProductPower = " + getProductPower() + "  getProductGlobalIp=" + getProductGlobalIp() +
                            " getProductLocalIp = " + getProductLocalIp() + "  getProductStatusTypeId=" + getProductStatusTypeId(intExtra) +
                            " getSoftVersionCode = " + getSoftVersionCode() + "  getSoftVersionName=" + getSoftVersionName() +
                            " getChargeStatus = " + getChargeStatus() + "  getRamStatus=" + getRamStatus() +
                            " getRomStatus = " + getRomStatus() + "  getVolume=" + getVolume() +
                            " getWifiStatus = " + getWifiStatus() + "  getQiniuPath=" + getQiniuPath() +
                            " getMacAddress = " + getMacAddress() + "  getPadMode=" + getPadMode() +
                            " getOSVersion = " + getOSVersion() +
                            " getTimestamp = " + getTimestamp() + "  getDetail=" + getDetail());
                    String jsonStatus = createJson(intExtra);
                    doUploadOnline(jsonStatus);
                }
            });
        }
    }

    private void doUploadOnline(final String json) {
        OkHttpUtils.post()
                .addParams("jsonStatus", json)
                .url(InformationOnline.INFROMATION_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        QuickUtils.log("InformationOnline   onError=" + e.getMessage());
                        doRetry(json);
                    }

                    @Override
                    public void onResponse(String result) {
                        QuickUtils.log("InformationOnline   onResponse=" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                            if (code.equals("1000")) {
                                QuickUtils.log("InformationOnline 上传在线状态数据成功！");
                                count.set(0);
                                doWriteSuccess(json,true);
                            } else {
                                doRetry(json);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


    }




    private static AtomicInteger count = new AtomicInteger();//retry机制

    private void doRetry(final String json) {
        count.getAndIncrement();
        if (count.get() < 3) {//retry 3 times
            doUploadOnline(json);
        }else{//没有尝试了，也就表示失败
            doWriteSuccess(json,false);
        }
    }

    private String createJson(final int intExtra) {
        JsonStaus jsonStaus = new JsonStaus();
        jsonStaus.setOrgId(getOrgId());
        jsonStaus.setClientId(getClientId());
        jsonStaus.setTableId(getTableId());
        jsonStaus.setBoardNo(getBoardNo());
        jsonStaus.setProductPower(getProductPower());
        jsonStaus.setProductGlobalIp(getProductGlobalIp());
        jsonStaus.setProductLocalIp(getProductLocalIp());
        jsonStaus.setProductStatusTypeId(getProductStatusTypeId(intExtra));
        jsonStaus.setSoftVersionCode(getSoftVersionCode());
        jsonStaus.setSoftVersionName(getSoftVersionName());
        jsonStaus.setChargeStatus(getChargeStatus());
        jsonStaus.setRamStatus(getRamStatus());
        jsonStaus.setRomStatus(getRomStatus());
        jsonStaus.setVolume(getVolume());
        jsonStaus.setWifiStatus(getWifiStatus());
        jsonStaus.setQiniuPath(getQiniuPath());
        jsonStaus.setMacAddress(getMacAddress());
        jsonStaus.setPadMode(getPadMode());
        jsonStaus.setOSVersion(getOSVersion());
        jsonStaus.setTimestamp(getTimestamp());
        jsonStaus.setDetail(getDetail());
        return JSON.toJSONString(jsonStaus);
    }

    private String getOrgId() {
        return QuickUtils.getOrgIdFromSp();
    }

    private String getClientId() {
        return String.valueOf(QuickUtils.getClientId());
    }

    private String getTableId() {
        return String.valueOf(QuickUtils.getDeskId());
    }

    private String getBoardNo() {
        return EasyCommonInfo.getInstance().PHONE().IMEI();
    }

    /**
     * 电池电量
     *
     * @return
     */
    private String getProductPower() {
        return EasyCommonInfo.getInstance().PHONE().batteryPercentage() + "%";
    }

    /**
     * 公网IP
     *
     * @return
     */
    private String getProductGlobalIp() {
        return EasyCommonInfo.getInstance().IP().global();
    }

    /**
     * 本地IP
     *
     * @return
     */
    private String getProductLocalIp() {
        return EasyCommonInfo.getInstance().IP().v4();
    }

    /**
     * remark 备注
     *
     * @return
     */
    private String getDetail() {
        return "remark";
    }

    private String getProductStatusTypeId(int intExtra) {
        return String.valueOf(intExtra);
    }

    private String getSoftVersionCode() {
        return String.valueOf(EasyCommonInfo.getInstance().APP().versionCode());
    }

    private String getSoftVersionName() {
        return EasyCommonInfo.getInstance().APP().versionName();
    }

    /**
     * 是否充电状态
     * 0充电,1未充电
     *
     * @return
     */
    private String getChargeStatus() {
        boolean isCharging = EasyCommonInfo.getInstance().PHONE().deviceCharging();
        if (isCharging) {
            return "0";
        }
        return "1";
    }

    private String getRamStatus() {
        return EasyCommonInfo.getInstance().RAM().information();
    }

    private String getRomStatus() {
        return EasyCommonInfo.getInstance().ROM().information();
    }

    private String getVolume() {
        return EasyCommonInfo.getInstance().PHONE().audioValue();
    }

    private String getWifiStatus() {
        return EasyCommonInfo.getInstance().IP().wifiRssi();
    }

    private String getQiniuPath() {
        return "";
    }

    private String getMacAddress() {
        return EasyCommonInfo.getInstance().IP().mac();
    }

    private String getPadMode() {
        return EasyCommonInfo.getInstance().PHONE().padMode();
    }

    private String getOSVersion() {
        return EasyCommonInfo.getInstance().APP().versionOs();
    }

    private String getTimestamp() {
        return EasyCommonInfo.getInstance().PHONE().timeMillis();
    }


    private void doWriteSuccess(final String json,final boolean isSuccess) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                EasyCommonInfo.getInstance().FILE().write(new File(AlgorithmUtil.FILE_MFILE + AlgorithmUtil.FILE_MFILE_ONLINE_TEXT), wirteText(json,isSuccess));
            }
        });
    }


    /**
     * Date(timestame),EXT_TYPE,Success
     *
     * @param json
     * @return
     */
    private String wirteText(String json,boolean isSucess) {
        String read = EasyCommonInfo.getInstance().FILE().read(new File(AlgorithmUtil.FILE_MFILE + AlgorithmUtil.FILE_MFILE_ONLINE_TEXT));
        if(read==null){
            String firstLine="Date,EXT_TYPE,Boolean"+"\r\n";
            EasyCommonInfo.getInstance().FILE().write(new File(AlgorithmUtil.FILE_MFILE + AlgorithmUtil.FILE_MFILE_ONLINE_TEXT),firstLine);
        }else{
            QuickUtils.log("文件中内容="+read);
        }

        JsonStaus bean = JSON.parseObject(json, JsonStaus.class);
        String timestamp = bean.getTimestamp();
        String date= EasyCommonInfo.getInstance().TIME().convertTimestamp(timestamp);
        String type="EXT_TYPE";
        String productStatusTypeId = bean.getProductStatusTypeId();
        if (productStatusTypeId.equals("1")) {
            type = "reboot upload online status";
        } else if (productStatusTypeId.equals("2")) {
            type = "12AM upload online status";
        } else if (productStatusTypeId.equals("3")) {
            type = "7PM upload online status";
        }
        String myBoolean="Boolean";
        if(isSucess){
            myBoolean="Success";
        }else{
            myBoolean="Fail";
        }
        return date + "," + type + "," + myBoolean+"\r\n";
    }

}
