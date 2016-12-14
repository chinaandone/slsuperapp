package com.cleverm.smartpen.util;

import android.content.Context;
import android.util.Log;

import com.cleverm.smartpen.service.penService;

/**
 * Created by xiong,An android project Engineer,on 13/4/2016.
 * Data:13/4/2016  下午 04:23
 * Base on clever-m.com(JAVA Service)
 * Describe: 无线与有线共同适配一套Scan代码
 * Version:1.0
 * Open source
 */
public class ScanUtil {

    private static final String TAG = "ScanUtil：";


    private static ScanUtil INSTANCE = new ScanUtil();

    public static ScanUtil getInstance() {
        return INSTANCE;
    }

    private ScanUtil() {
    }

    /**
     * 状态值匹配
     * @param id
     */
    public void onScan(Context context,int id,penService.MessageListener messageListener) {
        Log.v(TAG, "onScan() id=" + id);
        if (id == 0) {
            return;
        }
        switch (id) {
            //5个服务
            case Constant.ORDER_DISHES1:
            case Constant.ADD_WATER1:
            case Constant.TISSUE1:
            case Constant.OTHER1:
            case Constant.ORDER_DISHES2:
            case Constant.ADD_WATER2:
            case Constant.TISSUE2:
            case Constant.OTHER2:
            case Constant.ORDER_DISHES3:
            case Constant.ADD_WATER3:
            case Constant.TISSUE3:
            case Constant.OTHER3:
            case Constant.ORDER_DISHES4:
            case Constant.ADD_WATER4:
            case Constant.TISSUE4:
            case Constant.OTHER4:
            case Constant.ORDER_DISHES5:
            case Constant.ADD_WATER5:
            case Constant.TISSUE5:
            case Constant.OTHER5:
            case Constant.CLEAN_DESK:
            case Constant.FONDUE_SOUP:
            case Constant.CHANGE_TABLEWARE:{
                IntentUtil.goCallService(context,id,messageListener);
                break;
            }
            //呼叫服务员
            case Constant.PAY1:
            case Constant.PAY2:
            case Constant.PAY3:
            case Constant.PAY4:
            case Constant.PAY5: {
                IntentUtil.goToPayActivity(context);
                break;
            }
            case Constant.DEMO1:
            case Constant.DEMO2:
            case Constant.DEMO3:
            case Constant.DEMO4:
            case Constant.DEMO5: {
                IntentUtil.goToDemoActivity(context);
                break;
            }
            case Constant.EVALUATE1:
            case Constant.EVALUATE2:
            case Constant.EVALUATE3:
            case Constant.EVALUATE4:
            case Constant.EVALUATE5: {
                IntentUtil.goToEvaluateActivity(context);
                break;
            }
            case Constant.E_JIA1:
            case Constant.E_JIA2:
            case Constant.E_JIA3:
            case Constant.E_JIA4:
            case Constant.E_JIA5: {
                IntentUtil.goToDriverActivity(context);
                break;
            }
            case Constant.SET1:
            case Constant.SET2:
            case Constant.SET3:
            case Constant.SET4:
            case Constant.SET5: {
                IntentUtil.goToSelectTableActivity(context);
                break;
            }
            case Constant.YOU_HUI1:
            case Constant.YOU_HUI2:
            case Constant.YOU_HUI3:
            case Constant.YOU_HUI4:
            case Constant.YOU_HUI5: {
                IntentUtil.goToDiscountActivity(context);
                break;
            }

            case Constant.RECOMMEND1:
            case Constant.RECOMMEND2:
            case Constant.RECOMMEND3:
            case Constant.RECOMMEND4:
            case Constant.RECOMMEND5: {
                IntentUtil.goToLocalDiscountActivity(context);
                break;
            }
            case Constant.MO_JI1:
            case Constant.MO_JI2:
            case Constant.MO_JI3:
            case Constant.MO_JI4:
            case Constant.MO_JI5: {
                //今日天气
                IntentUtil.goToLauncherApp(context,Constant.MO_JI_PACKAGE_NAME, StatisticsUtil.APP_WEATHER, StatisticsUtil.APP_WEATHER_DESC);
                break;
            }
            case Constant.TOU_TIAO1:
            case Constant.TOU_TIAO2:
            case Constant.TOU_TIAO3:
            case Constant.TOU_TIAO4:
            case Constant.TOU_TIAO5: {
                //今日头条
                IntentUtil.goToLauncherApp(context,Constant.TOU_TIAO_PACKAGE_NAME, StatisticsUtil.APP_NEWS, StatisticsUtil.APP_NEWS_DESC);
                break;
            }
            case Constant.BAI_DU1:
            case Constant.BAI_DU2:
            case Constant.BAI_DU3:
            case Constant.BAI_DU4:
            case Constant.BAI_DU5: {
                //周边玩乐
                IntentUtil.goToLauncherApp(context,Constant.BAI_DU_PACKAGE_NAME, StatisticsUtil.APP_AROUNDPLAY, StatisticsUtil.APP_AROUNDPLAY_DESC);
                break;
            }
            case Constant.ONE_SHOP1:
            case Constant.ONE_SHOP2:
            case Constant.ONE_SHOP3:
            case Constant.ONE_SHOP4:
            case Constant.ONE_SHOP5: {
                //在线商场
                IntentUtil.goToLauncherApp(context,Constant.ONE_SHOP_PACKAGE_NAME, StatisticsUtil.APP_ONLINEBUY, StatisticsUtil.APP_ONLINEBUY_DESC);
                break;
            }
            case Constant.DA_ZONG1:
            case Constant.DA_ZONG2:
            case Constant.DA_ZONG3:
            case Constant.DA_ZONG4:
            case Constant.DA_ZONG5: {
                //周边优惠
                IntentUtil.goToH5Round(context);
                break;
            }
            case Constant.ZHI_ZHU1:
            case Constant.ZHI_ZHU2:
            case Constant.ZHI_ZHU3:
            case Constant.ZHI_ZHU4:
            case Constant.ZHI_ZHU5: {
                //电子杂志
                IntentUtil.goToLauncherApp(context,Constant.ZHIZ_ZHU_PACKAGE_NAME, StatisticsUtil.APP_MAGAZINE, StatisticsUtil.APP_MAGAZINE_DESC);
                break;
            }
            case Constant.AMUSEMENTFRAGMENT1:
            case Constant.AMUSEMENTFRAGMENT2:
            case Constant.AMUSEMENTFRAGMENT3:
            case Constant.AMUSEMENTFRAGMENT4:
            case Constant.AMUSEMENTFRAGMENT5: {
                //视频娱乐
                IntentUtil.goToLauncherApp(context,Constant.VIDEO_ENTERTAINMENT, StatisticsUtil.APP_VIDEO, StatisticsUtil.APP_VIDEO_DESC);
                break;
            }
            case Constant.WEB1:
            case Constant.WEB2:
            case Constant.WEB3:
            case Constant.WEB4:
            case Constant.WEB5: {
                //手游试玩
                IntentUtil.goToH5Game(context);
                break;
            }
            //自主买单
            case Constant.TWO_DIMENSION_CODE1:
            case Constant.TWO_DIMENSION_CODE2:
            case Constant.TWO_DIMENSION_CODE3:
            case Constant.TWO_DIMENSION_CODE4:
            case Constant.TWO_DIMENSION_CODE5: {
                IntentUtil.goToBuyMyself(context);
                break;
            }

                //打赏
            case Constant.AWARD:{
                IntentUtil.goToFutureActivity(context, StatisticsUtil.OTHER_GIVE_MONEY, StatisticsUtil.OTHER_GIVE_MONEY_DESC);
                break;
            }
                //未知功能
            case Constant.ROBOT_SHOW:{
                IntentUtil.goToRobotShow(context);
                break;
            }
            //金融
            case Constant.FINANCIAL:
            case Constant.UN_KNOW2:
            case Constant.UN_KNOW3:
            case Constant.UN_KNOW4:{
                IntentUtil.goToUnknow(context);
                break;
            }
        }
    }


}
