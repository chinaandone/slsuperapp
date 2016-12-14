package com.cleverm.smartpen.ui.windows;

import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.application.SmartPenApplication;

/**
 * Created by xiong,An android project Engineer,on 28/7/2016.
 * Data:28/7/2016  上午 11:13
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FixSplashScreenView extends LinearLayout {

    WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    public static int viewWidth;
    public static int viewHeight;

    View root;
    public FixSplashScreenView() {
        super(SmartPenApplication.getApplication());
        LayoutInflater.from(SmartPenApplication.getApplication()).inflate(R.layout.util_fix_splash_screen, this);
        root =  findViewById(R.id.util_fix_root);
        viewWidth = root.getLayoutParams().width;
        viewHeight = root.getLayoutParams().height;
    }


    public void add() {
        wm = getWindowManager();
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity=Gravity.BOTTOM|Gravity.RIGHT;
        wmParams.alpha = 0.1f;//窗口的透明度
        wmParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                //|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    //|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.width = viewWidth;
        wmParams.height = viewHeight;
        wmParams.x = (int) 3900;
        wmParams.y = (int) 3900;
        wm.addView(this, wmParams);
    }

    private  WindowManager getWindowManager() {
        if (wm == null) {
            wm = (WindowManager) SmartPenApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        }
        return wm;
    }

    public void remove(){
        WindowManager wm = getWindowManager();
        if(wm!=null){
            wm.removeView(this);
        }
    }

}
