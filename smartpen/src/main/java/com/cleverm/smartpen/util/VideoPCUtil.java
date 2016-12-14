package com.cleverm.smartpen.util;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cleverm.smartpen.bean.VideoInfo;
import com.cleverm.smartpen.log.*;
import com.cleverm.smartpen.ui.windows.engineer.EngineerUtil;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import okhttp3.Call;
import okhttp3.OkHttpClient;


/**
 * Created by xiong,An android project Engineer,on 18/8/2016.
 * Data:18/8/2016  上午 10:54
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class VideoPCUtil {

    private Timer timer;
    private Activity context;
    private static final String TAG = "VideoPCUtil"+"与PC通讯：";
    private static final int SERVER_HOST = 8532;
    private static final int PING_TIMEOUT = 30000;
    private static final int POLLTIME = 10;//600s = 30 分钟
    public static final String IPKEY = "VideoPCUtilIPKey";
    public static final String TIMEKEY = "VideoPCUtilTimeKey";
    private static final String ENTRYKEY = "smart_pen_server_ip";
    //private static final String IP_SERVER_URL = Constant.DDP_URL + "/cleverm/api/catchBytes/get";
    private static final String IP_SERVER_URL = "http://www.myee7.com/api_test/api/v10/catchBytes/get";
    private Toast mToast;


    private static class VideoPCUtilHolder {
        private static final VideoPCUtil INSTANCE = new VideoPCUtil();
    }

    private VideoPCUtil() {
    }

    public static final VideoPCUtil getInstance() {
        return VideoPCUtilHolder.INSTANCE;
    }


    public void start(Activity context) {
        this.context = context;
        this.mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                go();
            }
        });
    }

    private void go() {
        if (NetWorkUtil.hasNetwork()) {
            QuickUtils.log(TAG+"hasNetwork");
            checkIp();
        } else {
            QuickUtils.log(TAG+"initTimeStart");
            initTimeStart();
        }
    }

    private void initTimeStart() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (NetWorkUtil.hasNetwork()) {
                    checkIp();
                }
            }
        }, 1000, POLLTIME);
    }

    private void checkIp() {
        if (timer != null) {
            timer.cancel();
        }
        String ip = RememberUtil.getString(IPKEY, null);
        if (ip == null) {
            getIpFromServer();
        } else {
            ping(ip);
        }
    }

    private void ping(String ip) {
        boolean isSuccss = isPing(ip);
        if (isSuccss) {
            handlerPCResource(ip);
        } else {
            QuickUtils.log("ping PC 失败,只能去服务端去取IP");
            getIpFromServer();
        }
    }


    private boolean isPing(String ip) {
        long start = System.currentTimeMillis();
        boolean statu = false;
        try {
            Socket soc = new Socket();
            soc.connect(new InetSocketAddress(ip, SERVER_HOST), PING_TIMEOUT);
            statu = true;
            soc.close();
        } catch (IOException ex) {
        }
        long end = System.currentTimeMillis();
        long time = (end - start) + 1;
        QuickUtils.log("Ping-PC "+ip+ " ，时间为 "+time );
        return statu;
    }


    private void getIpFromServer() {
        String orgId = QuickUtils.getOrgIdFromSp();
        OkHttpUtils.post()
                .addParams("orgID", orgId)
                .addParams("entry", ENTRYKEY)
                .url(IP_SERVER_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        QuickUtils.log("服务端取IP失败  "+e.getMessage());
                    }

                    @Override
                    public void onResponse(final String json) {
                        ThreadManager.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(json);
                                    String data = object.getString("data");
                                    JSONObject object1 = new JSONObject(data);
                                    String ip = object1.getString("ip");
                                    String timestamp = object1.getString("timestamp");
                                    boolean isSucces = isPing(ip);
                                    if (isSucces) {
                                        storeIP(ip, timestamp);
                                        handlerPCResource(ip);
                                    } else {
                                        doMetalogic();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }


    private void storeIP(String ip, String timestamp) {
        RememberUtil.putString(IPKEY, ip);
        RememberUtil.putString(TIMEKEY, timestamp);
    }


    private void handlerPCResource(String ip) {
        getJsonFromPC(ip);
    }

    private void getJsonFromPC(final String ip) {
        String url = "http://" + ip + ":8532/resources/videos/video_list.txt";
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "video_list.txt") {

                    @Override
                    public void inProgress(float v) {

                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(File file) {
                        ThreadManager.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String json = FileUtil.getFileContext(Environment.getExternalStorageDirectory().getAbsolutePath(), "video_list.txt");
                                    transform(ip, json);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 读取json
     *
     * @param json
     */
    private void transform(String ip, String json) {
        try {
            List<VideoInfo> infos = JsonUtil.parser(json, VideoInfo.class);
            BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
            for (int i = 0; i < infos.size(); i++) {
                queue.add(infos.get(i).getVideoId() + "");
            }
            String num = queue.poll();
            downloadVideoFromPC(ip, num, queue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取视频资源
     *
     * @param ip
     * @param num
     * @param queue
     */
    private void downloadVideoFromPC(final String ip, final String num, final BlockingQueue<String> queue) {
        String url = "http://" + ip + ":8532" + "/resources/videos/" + num + ".mp4";
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), num + ".mp4") {

                    @Override
                    public void inProgress(final float progress) {
                        //QuickUtils.log(TAG + "视频的编号为 "+num + " inProgress进度条" + ":" + progress);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        QuickUtils.log(TAG+ "onError:" + e.getMessage());
                        downloadVideoFromPC(ip, num, queue);
                    }

                    @Override
                    public void onResponse(final File file) {
                        ThreadManager.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                QuickUtils.log(TAG + "inProgress onResponse: " + file.getAbsolutePath());
                                CopyFileUtil.copyByNIO(AlgorithmUtil.VIDEO_FILE + File.separator + num + ".mp4", AlgorithmUtil.VIDEO_FILE_PLAY + File.separator + num + ".mp4", false);
                                String num = queue.poll();//do not use take() !
                                if (num != null) {
                                    downloadVideoFromPC(ip, num, queue);
                                }
                            }
                        });
                    }
                });


    }

    /**
     * 直接使用pad-wifi请求视频资源
     */
    private void doMetalogic() {
        QuickUtils.log("直接使用pad-wifi请求视频资源");
    }



    private void toast(final String message) {
        /*context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(message);
                mToast.show();
            }
        });*/
    }


}
