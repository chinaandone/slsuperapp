package com.cleverm.smartpen.util;

/**
 * Created by xiong,An android project Engineer,on 2016/3/20.
 * Data:2016-03-20  21:32
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
@Deprecated
public class NewVideoAlgorithmUtil {

    private NewVideoAlgorithmUtil INSTANCE = new NewVideoAlgorithmUtil();


    /**
     * 业务逻辑如下：
     * 使用双目录来处理,muyevideo目录业务逻辑处理  muyeplay用于播放。只有在video目录下载完成的才会转到play目录
     * play目录用于播放
     *
     * 1.第一次使用，video和paly目录均是无视频的，所以需要在线播放且后台下载到Video目录。然后下载成功的就转到paly目录
     * 2.第二次使用进行比对。服务端有 本地无 下载  、服务端无 本地有 删除   、 服务端有 本地也有的  比对大小 判断是否要下载
     * 3.这些操作都级联到paly目录  进行copy操作
     */
    public static void startFirstTime(){
        //@Deprecated
    }



}
