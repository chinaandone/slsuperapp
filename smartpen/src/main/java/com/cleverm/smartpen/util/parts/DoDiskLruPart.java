package com.cleverm.smartpen.util.parts;

import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.util.disk.DiskLruCacheHelper;

/**
 * Created by xiong,An android project Engineer,on 19/5/2016.
 * Data:19/5/2016  下午 07:31
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DoDiskLruPart {

    private DiskLruCacheHelper helper;

    private static class DoDiskLruParttHolder {
        private static final DoDiskLruPart INSTANCE = new DoDiskLruPart();
    }

    private DoDiskLruPart() {
    }

    public static final DoDiskLruPart getInstance() {
        return DoDiskLruParttHolder.INSTANCE;
    }

    public String get(String key){
        if(helper==null){
            helper = new DiskLruCacheHelper(SmartPenApplication.getApplication());
        }
        return helper.getAsString(key);
    }


    public  void put(String key,String value){
        if(helper==null){
            helper = new DiskLruCacheHelper(SmartPenApplication.getApplication());
        }
        helper.put(key,value);
    }



}
