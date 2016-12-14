package com.cleverm.smartpen.util;

import android.app.Activity;
import android.os.Environment;

import com.cleverm.smartpen.bean.DiscountInfo;
import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.ui.FullScreenVideoView;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 2016/2/21.
 * Data:2016-02-21  14:12
 * Base on clever-m.com(JAVA Service)
 * Describe: 视频和图片处理的算法
 * Version:1.0
 * Open source
 */
public class AlgorithmUtil {

    //逻辑视频目录
    private static final String OFFICIAL_BASE_FILE=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.android.cache/system/data/cache/empty/com.android.cache";
    public static final String VIDEO_FILE = OFFICIAL_BASE_FILE+ File.separator+"mvideo";
    public static final String VIDEO_FILE_PLAY = OFFICIAL_BASE_FILE+ File.separator+"mpaly";
    public static final String VIDEO_DEMO_FILE=OFFICIAL_BASE_FILE + File.separator+"mdemo";
    public static final String FILE_MFILE=OFFICIAL_BASE_FILE+File.separator+"mfile";
    public static final String FILE_MFILE_ONLINE_TEXT="/online.csv";
    public static final String FILE_MFILE_DEBUG_TEXT="/debug.deb";
    public static final String FILE_MORG=OFFICIAL_BASE_FILE+File.separator+"morg";
    public static final String FILE_MORG_ORG_TEXT=FILE_MORG+ File.separator+"smartpen.txt";
    public static final String FILE_MAPK=OFFICIAL_BASE_FILE+File.separator+"mapk";
    public static final String FILE_MSCREEN=OFFICIAL_BASE_FILE+File.separator+"mscreen";

    public static final String DOWNLOAD_APK_FILE=Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String OLD_ORG_PATH=Environment.getExternalStorageDirectory().getPath()+"/SystemPen/smartpen.txt";

    //private static final String BASE_FILE=Environment.getExternalStorageDirectory().getAbsolutePath()+"/muye";
    //public static final String VIDEO_FILE= Environment.getExternalStorageDirectory().getAbsolutePath() + "/muyevideo";
    //public static final String VIDEO_FILE_PLAY = Environment.getExternalStorageDirectory().getAbsolutePath()+"/muyepaly";
    //public static final String VIDEO_DEMO_FILE=BASE_FILE + File.separator+"muyedemo";

    private static AlgorithmUtil INSTANCE =new AlgorithmUtil();

    AlgorithmUtil(){

    }

    public static AlgorithmUtil getInstance(){
        return INSTANCE;
    }

    //-------------------------------------------------------------
    // 图片顺序算法
    //-------------------------------------------------------------
    HashMap<Integer,String> bussinessMapImage= new HashMap<Integer,String>();
    ArrayList<String> bussinessImage=new ArrayList<String>();
    HashMap<Integer,String> muyeMapImage= new HashMap<Integer,String>();
    ArrayList<String> muyeImage=new ArrayList<String>();

    /**
     * @deprecated
     * 图片顺序算法：
     * 算法核心：
     * 1.本店活动和商圈活动两个内容间隔播放
     * 2.数量限制，本店活动最多10个，商圈活动最多5个，即轮播的最大数量为15个
     *      例：素材库上有4个本店活动,2个商圈活动，则播放顺序为：本店、商圈、本店、商圈、本店、本店
     * 3.第一个播放本店活动，本店活动播放的优先顺序在后台可手动配置，默认以上次顺序排列。轮播时间设定为3秒
     * @param list
     * @return 播放顺序符合算法的图片集合
     */
    public List<DiscountInfo> getImageSequence(List<DiscountInfo> list){


        if(list==null || list.size()<=0){
            return null;
        }


        //1.根据type分集合(0表示 是本店商户的，1表示图片是商圈的)
        for(int i =0;i<list.size();i++){

            int type = list.get(i).getType();

            if(0==type){//本店商家
                //bussinessList.add(list.get(i));
                bussinessMapImage.put(list.get(i).getOrderSeq(), list.get(i).getPictruePath());
            }else{//商圈
                muyeMapImage.put(list.get(i).getOrderSeq(),list.get(i).getPictruePath());
            }

        }


        //2.根据orderSeq进行集合内排序，orderSql的起始值为1
        if(bussinessMapImage.size()>0){
            for(int j =1;j<bussinessMapImage.size()+1;j++){
                String path = bussinessMapImage.get(j);
                bussinessImage.add(path);
            }
        }

        if(muyeMapImage.size()>0){
            for(int j =1 ; j <muyeMapImage.size()+1;j++){
                String path = muyeMapImage.get(j);
                muyeImage.add(path);
            }
        }


        //TODO 3.根据规则从不同的集合中取值出来:先拿商家的再拿商圈的.以此类推放入一个新集合



        return null;

    }



    private int index=0;
    List<DiscountInfo> bussinessList= new ArrayList<DiscountInfo>();
    List<DiscountInfo> muyeList = new ArrayList<DiscountInfo>();
    List<DiscountInfo> sequenceList = new ArrayList<DiscountInfo>();

    /**
     * 图片顺序算法：
     * @param list
     * @return 已经传插排序好的内容
     */
    public List<DiscountInfo> getSimpleImageSequence(List<DiscountInfo> list){

        if(list==null || list.size()<=0){
            return null;
        }

        //1.根据type分集合(0表示 是本店商户的，1表示图片是商圈的)
        for(int i =0;i<list.size();i++){
            int type = list.get(i).getType();
            if(0==type){//本店商家
                bussinessList.add(list.get(i));
            }else{//商圈
                muyeList.add(list.get(i));
            }
        }

        //2.根据json顺序进行集合内排序,该操作服务端已处理



        //3.根据规则从不同的集合中取值出来:先拿商家的再拿商圈的.以此类推放入一个新集合

        while(true){

            if(bussinessList.size()>index){
                sequenceList.add(bussinessList.get(index));
            }

            if(muyeList.size()>index){
                sequenceList.add(muyeList.get(index));
            }

            index++;

            if(bussinessList.size()<index&&muyeList.size()<index){
                break;
            }

        }

        return sequenceList;

    }

    public void clearImageSequence(){
        bussinessList.clear();
        muyeList.clear();
        sequenceList.clear();
        index=0;
    }



    //-------------------------------------------------------------
    // 视频处理算法:
    //-------------------------------------------------------------
    @Deprecated
    public void getSimpleVideo(final FullScreenVideoView vvAdvertisement){


        //1.去服务端拿标记位来判断是否需要更新视频
        DownloadUtil.getVideoFlag(new ServiceUtil.JsonInterface() {
            @Override
            public void onSucced(String json) {
                try {
                    List<VideoInfo> infos = JsonUtil.parser(json, VideoInfo.class);

                    boolean serviceFlagNoChange = true;


                    if (serviceFlagNoChange) {
                        //2.去本地拿是否存在视频目录及文件
                        if (QuickUtils.isHasVideoFolder()&&QuickUtils.isVideoFolderHaveFile()) {
                            //拿本地的Video：本地Video的顺序是文件名的顺序
                            VideoUtil videoUtil = new VideoUtil(vvAdvertisement);
                            videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE, 0);
                        } else {
                            //本地没有视频的话,就重新去服务器取地址并下载存储
                            DownloadUtil.preVideoFileFromService(vvAdvertisement);
                        }
                    } else {
                       //3.服务端拿视频
                        //如果更新Video,直接去读取Video,并存储所有的Video
                        DownloadUtil.preVideoFileFromService(vvAdvertisement);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //4.发生任何异常后都去本地拿


                }
            }

            @Override
            public void onFail(String error) {
                //4.发生任何异常后都去本地拿

            }
        });

    }


    /**
     * 核心点在于每天只播放服务器API上的列表的视频
     *  1.将这些视频的id值和本地存储的id比较,没有的id的视频就不会播放,然后取出要
     *  播放的那些id值;
     *  2.对于新的id的视频要进行下载后再进行播放;
     *  3.根据这些视频的type属性进行视频播放的排序;
     *  具体代码层面操作步骤：
     * 1.先判断本地是否有Video文件夹
     * 2.如果木有就表示这是第一次使用,直接走服务端请求并存储video,video的name就是：videoId这一唯一标识符+"_"+video的type类型
     * 3.如果有,那么就直接循环遍历去判断服务端这次给的videoId是否存在于了存储中.对于没有的videId就进行下载(只下载该视频)
     * 4.然后进行播放的排序操作,播放的顺序存入一个Map集合
     * 5.根据最后的排序进行播放
     *
     * 算法三部曲:
     * 1.下载和存储(finished)
     * 2.排序(waiting2.0)
     * 3.裁剪播放(waiting2.0)
     * //1.先判断服务器实现需要我们去更新
     * //2.如果不需要更新,直接检查我们的视频目录是否存在视频
     * //3.根据排序规则进行视频的依次播放
     *
     */
    public void startVideoPlayAlgorithm(FullScreenVideoView videoView,Activity activity){
        if(!QuickUtils.isHasVideoFolder(AlgorithmUtil.VIDEO_FILE_PLAY)&&!QuickUtils.isVideoFolderHaveFiel2(AlgorithmUtil.VIDEO_FILE_PLAY)){
            //删除muyevideo的文件
            File file = new File(AlgorithmUtil.VIDEO_FILE);
            QuickUtils.deleteDir(file);
        }
        //访问API,存储所有数据到DB
        if(QuickUtils.isHasVideoFolder()&&QuickUtils.isVideoFolderHaveFiel2()){
            //3.如果有,那么就直接循环遍历去判断服务端这次给的videoId是否存在于了存储中.对于没有的videoId就进行下载
            VideoAlgorithmUtil.getInstance().loopFileName2(videoView,activity);
        }else{
            //2.木有就表示这是第一次使用,直接走服务端请求并存储video
            VideoAlgorithmUtil.getInstance().getVideoFirst(videoView,activity);
        }
    }

    /**
     * 1.删除paly目录的video
     * 2.请求接口后进行数据比对，然后要播放的数据copy到新目录中
     * 3.开启播放play目录
     * @param videoView
     * @param activity
     */
    public void startVideoPlayNewAlgorithm(FullScreenVideoView videoView,Activity activity){
        //访问API,存储所有数据到DB
        if(QuickUtils.isHasVideoFolder(AlgorithmUtil.VIDEO_FILE_PLAY)&&QuickUtils.isVideoFolderHaveFiel2(AlgorithmUtil.VIDEO_FILE_PLAY)){
            QuickUtils.log("Video----nofirst----");
            //3.如果有,那么就直接循环遍历去判断服务端这次给的videoId是否存在于了存储中.对于没有的videoId就进行下载
            VideoAlgorithmUtil.getInstance().loopFileName2(videoView,activity);
        }else{
            //2.木有就表示这是第一次使用,直接走服务端请求并存储video
            QuickUtils.log("Video----first----");
            VideoAlgorithmUtil.getInstance().getVideoFirst(videoView,activity);
        }

    }

}
