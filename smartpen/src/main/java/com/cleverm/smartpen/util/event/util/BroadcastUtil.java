package com.cleverm.smartpen.util.event.util;


import android.content.Context;
import android.os.Bundle;

import com.cleverm.smartpen.util.event.BroadcastEvent;
import com.cleverm.smartpen.util.event.BroadcastEx;


/**
 * Created by xiong,An Android project Engineer.
 * Date: 2015-08-07  10:50
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class BroadcastUtil {

    /**在QuickUtils或者Application中初始化*/
    public static void init(Context context){
        BroadcastEvent.init(context);
    }

    /**在onCreate中初始化*/
    public static int register(BroadcastEx.EventsListener listener){
        return BroadcastEvent.register(listener);
    }

    /**在onDestory中初始化*/
    public static void unregister(int regId){
        BroadcastEvent.unregister(regId);
    }

    /**
     * 发送EVENT_ID
     * @param ID
     * @param bundle
     */
    public static void post(BroadcastCx.DEF_EVENT_ID ID,Bundle bundle){
        if(ID==null){
            ID=BroadcastCx.DEF_EVENT_ID.Cx_Defalut;
        }
        BroadcastEvent.send(ID.getIndex(), bundle);
    }
    
    
    public static Bundle getBundle(){
        return new Bundle();
    }
    
    
}
