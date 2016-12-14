package com.cleverm.smartpen.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiong,An android project Engineer,on 12/7/2016.
 * Data:12/7/2016  下午 05:18
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class LongPressView extends View {

    private int mLastMotionX, mLastMotionY;
    //是否移动了
    private boolean isMoved;
    //长按的runnable
    private Runnable mLongPressRunnable;
    //移动的阈值
    private static final int TOUCH_SLOP = 20;
    //时间
    private static final int TOUCH_TIME = 3000;

    public LongPressView(Context context) {
        super(context);
        mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                performLongClick();
            }
        };
    }

    public LongPressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLongPressRunnable = new Runnable() {

            @Override
            public void run() {
                performLongClick();
            }
        };
    }


    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                isMoved = false;
                postDelayed(mLongPressRunnable, TOUCH_TIME);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoved) break;
                if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
                        || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                    //移动超过阈值，则表示移动了
                    isMoved = true;
                    removeCallbacks(mLongPressRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                //释放了
                removeCallbacks(mLongPressRunnable);
                break;
        }
        return true;
    }
}
