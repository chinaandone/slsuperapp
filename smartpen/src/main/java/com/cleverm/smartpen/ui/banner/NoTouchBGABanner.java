package com.cleverm.smartpen.ui.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ScrollView;

import com.cleverm.smartpen.util.QuickUtils;

/**
 * Created by xiong,An android project Engineer,on 2016/2/23.
 * Data:2016-02-23  15:08
 * Base on clever-m.com(JAVA Service)
 * Describe: 不可手动滑动BGABanner
 * Version:1.0
 * Open source
 */
public class NoTouchBGABanner extends BGABanner {


    public NoTouchBGABanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchBGABanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return true;
    }
}
