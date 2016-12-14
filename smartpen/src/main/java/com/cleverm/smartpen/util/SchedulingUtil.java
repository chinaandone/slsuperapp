package com.cleverm.smartpen.util;

import android.content.Context;
import android.content.Intent;

import com.cleverm.smartpen.service.OtherService;

/**
 * Created by xiong,An android project Engineer,on 4/5/2016.
 * Data:4/5/2016  上午 09:14
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class SchedulingUtil {


    public static void doOnDemo(Context context){
        Intent intent = new Intent(context, OtherService.class);
        context.startService(intent);
    }



}
