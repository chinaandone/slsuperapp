package com.cleverm.smartpen.ui.windows.engineer;

import android.os.Looper;

import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.VideoPCUtil;
import com.cleverm.smartpen.util.WeakHandler;

/**
 * Created by xiong,An android project Engineer,on 19/8/2016.
 * Data:19/8/2016  下午 02:08
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class EngineerUtil {

    private EngineerModeView view;

    private static class EngineerUtilHolder {
        private static final EngineerUtil INSTANCE = new EngineerUtil();
    }

    private EngineerUtil() {
    }

    public static final EngineerUtil getInstance() {
        return EngineerUtilHolder.INSTANCE;
    }

    WeakHandler mHandler = new WeakHandler(Looper.getMainLooper());
    public void log(final String msg){
        boolean mainThread = QuickUtils.isMainThread();
        if(mainThread){
            createView(msg);
        }else{
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    createView(msg);
                }
            });
        }
    }

    private void createView(String msg){
        if(view==null){
            view=new EngineerModeView();
        }
        view.log(msg);
    }

    public void destory(){
        if(view!=null){
            view.remove();
            view=null;
        }
    }


}
