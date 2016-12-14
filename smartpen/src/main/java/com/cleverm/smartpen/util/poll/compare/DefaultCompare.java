package com.cleverm.smartpen.util.poll.compare;

import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.JsonUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.VideoAlgorithmUtil;
import com.cleverm.smartpen.util.poll.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 7/9/2016.
 * Data:7/9/2016  下午 03:58
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DefaultCompare<T1,T2,T3> implements Compare<T1,T2,T3>{

    private T1 t1;
    private T2 t2;
    private T3 t3;

    private List<VideoInfo> downloadList = new ArrayList<VideoInfo>();
    private HashMap<String, String> serviceList = new HashMap<String, String>();
    private HashMap<String, String> LocalList = new HashMap<String, String>();



    @Override
    public T1 valueLeft(String condition) throws Exception{
        List<VideoInfo> info = JsonUtil.parser(condition, VideoInfo.class);
        t1= (T1) info;
        return t1;
    }

    @Override
    public T2 valueRight(String condition) {
        File[] files = QuickUtils.getVideoFolderFiles();
        t2= (T2) files;
        return t2;
    }

    @Override
    public T3 compare(T1 t1, T2 t2) {
        List<VideoInfo> infos = (List<VideoInfo>) t1;
        File[] files = (File[]) t2;

        for (int i = 0; i < infos.size(); i++) {
            serviceList.put(infos.get(i).getVideoId() + "", infos.get(i).getVideoPath());
        }

        for (int i = 0; i < files.length; i++) {
            String fileNameNum = QuickUtils.subVideoEnd(files[i].getName());
            LocalList.put(fileNameNum, AlgorithmUtil.VIDEO_FILE + fileNameNum + ".mp4");
        }

        //Step1:服务器无,本地有(服务器没有本地的那个key)
        for (int i = 0; i < files.length; i++) {
            if (!serviceList.containsKey(QuickUtils.subVideoEnd(files[i].getName()))) {
                QuickUtils.deleteFile(AlgorithmUtil.VIDEO_FILE +File.separator+QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
                QuickUtils.deleteFile(AlgorithmUtil.VIDEO_FILE_PLAY +File.separator+QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
            }
        }
        //Step2:若服务器，本地也有,则比对大小,将大小不同的认定为需要断点续传的
        for(int i = 0; i < files.length; i++){
            //服务端有这个key
            if(serviceList.containsKey(QuickUtils.subVideoEnd(files[i].getName()))){
                //然后迭代拿到服务端的这个key   和   本地的这   name
                for(int j =0 ; j<infos.size();j++){
                    String ser_id = String.valueOf(infos.get(j).getVideoId());
                    String loc_id = QuickUtils.subVideoEnd(files[i].getName());
                    if(ser_id.equals(loc_id)){
                        Long Service_VideoSize = infos.get(j).getVideoSize();
                        long Local_VideoSize = Utils.getVideoFileLength(AlgorithmUtil.VIDEO_FILE + File.separator + QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
                        if(Local_VideoSize!=Service_VideoSize){
                            if(Utils.isBreakPoint(ser_id)){//是否本地有该断点起始位置，如果没有则删除,并更新本地list
                                QuickUtils.deleteFile(AlgorithmUtil.VIDEO_FILE +File.separator+QuickUtils.subVideoEnd(files[i].getName() + ".mp4"));
                                LocalList.remove(QuickUtils.subVideoEnd(files[i].getName()));
                            }else{
                                downloadList.add(infos.get(j));//放入下载集合
                            }
                        }
                    }
                }
            }
        }


        //Step3:服务器有,本地无(本地没有服务器的那个key)
        for (int i = 0; i < infos.size(); i++) {
            if (!LocalList.containsKey(infos.get(i).getVideoId() + "")) {
                downloadList.add(infos.get(i));
            }
        }


        return (T3) downloadList;

    }

    @Override
    public void after() {

    }
}
