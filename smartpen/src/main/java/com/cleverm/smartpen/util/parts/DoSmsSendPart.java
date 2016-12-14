package com.cleverm.smartpen.util.parts;

import android.util.Log;

import com.cleverm.smartpen.app.SelectTableActivity;
import com.cleverm.smartpen.net.InfoSendSMSVo;
import com.cleverm.smartpen.net.RequestNet;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.NetWorkUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.ThreadManager;

/**
 * Created by xiong,An android project Engineer,on 18/5/2016.
 * Data:18/5/2016  上午 11:35
 * Base on clever-m.com(JAVA Service)
 * Describe: 先发送短信,然后发送成功后弹出显示框
 * Version:1.0
 * Open source
 */
public class DoSmsSendPart {

    private static final String TAG="DoSmsSendPart：";

    private static class DoSmsPartHolder {
        private static final DoSmsSendPart INSTANCE = new DoSmsSendPart();
    }

    private DoSmsSendPart() {
    }

    public static final DoSmsSendPart getInstance() {
        return DoSmsPartHolder.INSTANCE;
    }

    /**
     * 统计事件不受短信影响
     * @param penCode
     * @param alreadySend
     */
    public void sendSms(int penCode,boolean alreadySend){
        int templateID = statIteration(penCode);
        long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        if(!NetWorkUtil.hasNetwork()){
            QuickUtils.toast("网络异常，请直接找服务员～");
            return;
        }
        if (deskId == Constant.DESK_ID_DEF_DEFAULT) {
            QuickUtils.toast("桌号未设置，请直接找服务员～");
            return;
        }
        InfoSendSMSVo infoSendSMSVo = new InfoSendSMSVo();
        infoSendSMSVo.setTemplateID(templateID);
        infoSendSMSVo.setTableID(deskId);
        Log.v(TAG, "SendSMS：" + "id=" + penCode + " deskId=" + deskId + " alreadySend=" + alreadySend);
        sendMessageToService(infoSendSMSVo, penCode, alreadySend);
    }

    private void sendMessageToService(final InfoSendSMSVo infoSendSMSVo, final int code, final boolean alreadySend) {
        new Thread() {
            @Override
            public void run() {
                if (alreadySend == false) {
                    InfoSendSMSVo getSMSVo = RequestNet.getData(infoSendSMSVo);
                    if (getSMSVo != null && getSMSVo.getSuccess()) {
                        QuickUtils.log("SendSMS："+ getSMSVo.getSuccess());
                        DoAnimationPart.getInstance().doAnimation(code);
                    } else {
                    }
                } else {
                    DoAnimationPart.getInstance().doAnimation(code);
                }
            }
        }.start();
    }

    /**
     * 统计数据
     * @param penCode
     * @return
     */
    private int statIteration(int penCode) {
        int templateID = 0;
        switch (penCode){
            case Constant.ORDER_DISHES1:
            case Constant.ORDER_DISHES2:
            case Constant.ORDER_DISHES3:
            case Constant.ORDER_DISHES4:
            case Constant.ORDER_DISHES5: {
                templateID = Constant.FOOD_ADD_SMS;
                doStatistic(StatisticsUtil.CALL_ADD_FOOD, StatisticsUtil.CALL_ADD_FOOD_DESC);
                break;
            }
            case Constant.ADD_WATER1:
            case Constant.ADD_WATER2:
            case Constant.ADD_WATER3:
            case Constant.ADD_WATER4:
            case Constant.ADD_WATER5: {
                templateID = Constant.WATER_ADD_SMS;
                doStatistic(StatisticsUtil.CALL_ADD_WATER, StatisticsUtil.CALL_ADD_WATER_DESC);
                break;
            }
            case Constant.TISSUE1:
            case Constant.TISSUE2:
            case Constant.TISSUE3:
            case Constant.TISSUE4:
            case Constant.TISSUE5: {
                templateID =  Constant.TISSUE_ADD_SMS;
                doStatistic(StatisticsUtil.CALL_ADD_TISSUE, StatisticsUtil.CALL_ADD_TISSUE_DESC);
                break;
            }
            case Constant.PAY1:
            case Constant.PAY2:
            case Constant.PAY3:
            case Constant.PAY4:
            case Constant.PAY5: {
                templateID = Constant.PAY_MONRY_SMS;//呼叫结账统计代码到具体界面PayActivity处理
                break;
            }
            case Constant.OTHER1:
            case Constant.OTHER2:
            case Constant.OTHER3:
            case Constant.OTHER4:
            case Constant.OTHER5: {
                templateID = Constant.OTHER_SERVICE_SMS;
                doStatistic(StatisticsUtil.CALL_OTHER_SERVIC, StatisticsUtil.CALL_OTHER_SERVIC_DESC);
                break;
            }
            case Constant.CLEAN_DESK: {
                templateID = Constant.CLEAN_SMS;
                doStatistic(StatisticsUtil.CLEAN_DESK, StatisticsUtil.CLEAN_DESK_DESC);
                break;
            }
            case Constant.FONDUE_SOUP: {
                templateID = Constant.FONDUE_SOUP_SMS;
                doStatistic(StatisticsUtil.FONDUE_SOUP_STAT, StatisticsUtil.FONDUE_SOUP_STAT_DESC);
                break;
            }
            case Constant.CHANGE_TABLEWARE: {
                templateID = Constant.CHANGE_TABLEWARE_SMS;
                doStatistic(StatisticsUtil.CHANGE_TABLEWARE_STAT, StatisticsUtil.CHANGE_TABLEWARE_STAT_DESC);
                break;
            }
            case Constant.CASH_PAY:{
                templateID = Constant.CASH_PAY_SMS;
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC + "--" + StatisticsUtil.CALL_PAY_CASH_DESC, StatisticsUtil.CALL_PAY_CASH);
                break;
            }
            case Constant.UNION_CARD_PAY:{
                templateID= Constant.UNION_CARD_PAY_SMS;
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC + "--" + StatisticsUtil.CALL_PAY_CARD_DESC, StatisticsUtil.CALL_PAY_CARD);
                break;
            }
            case Constant.WEIXIN_PAY:{
                templateID = Constant.WEIXIN_PAY_SMS;
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC + "--" + StatisticsUtil.CALL_PAY_WEIXIN_DESC, StatisticsUtil.CALL_PAY_WEIXIN);
                break;
            }
            case Constant.ALI_PAY:{
                templateID = Constant.ALI_PAY_SMS;
                doStatistic(StatisticsUtil.CALL_PAY, StatisticsUtil.CALL_PAY_DESC + "--" + StatisticsUtil.CALL_PAY_ALIPAY_DESC, StatisticsUtil.CALL_PAY_ALIPAY);
                break;
            }
        }
        return templateID;
    }


    private void doStatistic(final int key,final String describe){
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                StatisticsUtil.getInstance().insert(key, describe);
            }
        });
    }

    private void doStatistic(final int key,final String describe,final Long secondKey){
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                StatisticsUtil.getInstance().insertWithSecondEvent(key, describe, secondKey);
            }
        });
    }





}
