package com.cleverm.smartpen.app;

import com.cleverm.smartpen.util.StatisticsUtil;

/**
 * Created by xiong,An android project Engineer,on 2016/3/1.
 * Data:2016-03-01  16:06
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class LocalDiscountActivity extends BaseDiscountActivity {

    @Override
    protected boolean getSonDiscountArea() {
        return false;
    }

    @Override
    protected int onGetEventId() {
        return StatisticsUtil.SERVICE_LOCAL_DISCOUNT;
    }

    @Override
    protected String onGetDesc() {
        return StatisticsUtil.SERVICE_LOCAL_DISCOUNT_DESC;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
