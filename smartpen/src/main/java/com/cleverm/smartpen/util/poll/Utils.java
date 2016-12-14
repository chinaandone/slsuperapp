package com.cleverm.smartpen.util.poll;

import java.io.File;

/**
 * Created by xiong,An android project Engineer,on 7/9/2016.
 * Data:7/9/2016  下午 05:27
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class Utils {

    /**
     * 视频长度
     * @param sPath
     * @return
     */
    public static long getVideoFileLength(String sPath){
        File file = new File(sPath);
        return file.length();
    }

    /**
     * 判断是否有存储断点
     * @param id
     * @return
     */
    public static boolean isBreakPoint(String id) {

        return true;
    }
}
