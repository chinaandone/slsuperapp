package com.cleverm.smartpen.app;

import com.cleverm.smartpen.util.StatisticsUtil;

/**
 * Created by xiong,An android project Engineer,on 2016/2/19.
 * Data:2016-02-19  11:25
 * Base on clever-m.com(JAVA Service)
 * Describe: 优惠专区 @Deprecated
 *      want to know more see ScrollDiscountActivity
 * Version:1.0
 * Open source
 */
@Deprecated
public class DiscountActivity extends BaseDiscountActivity{

    @Override
    protected boolean getSonDiscountArea() {
        return true;
    }


    protected int onGetEventId() {
        return StatisticsUtil.SERVICE_DISCOUNT;
    }


    protected String onGetDesc() {
        return StatisticsUtil.SERVICE_DISCOUNT_DESC;
    }


}
