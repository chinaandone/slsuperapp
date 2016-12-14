package com.cleverm.smartpen.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bleframe.library.log.BleLog;
import com.cleverm.smartpen.R;
import com.cleverm.smartpen.Version.VersionManager;
import com.cleverm.smartpen.bean.event.DefaultEvent;
import com.cleverm.smartpen.bean.event.OnBootRestartEvent;
import com.cleverm.smartpen.bean.event.OnChangeAnimNoticeEvent;
import com.cleverm.smartpen.bean.event.OnDestoryActivityEvent;
import com.cleverm.smartpen.bean.event.OnMessageCallEvent;
import com.cleverm.smartpen.bean.event.OnRobotShowEvent;
import com.cleverm.smartpen.bean.event.OnVideoPlayMemoryEvent;
import com.cleverm.smartpen.bean.event.OnVideoUpdateEvent;
import com.cleverm.smartpen.database.DatabaseHelper;
import com.cleverm.smartpen.modle.TableType;
import com.cleverm.smartpen.service.ScreenLockListenService;
import com.cleverm.smartpen.service.penService;
import com.cleverm.smartpen.ui.FullScreenVideoView;
import com.cleverm.smartpen.ui.MyText;
import com.cleverm.smartpen.util.AlgorithmUtil;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.DownloadUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.SchedulingUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.UpdateTableUtil;
import com.cleverm.smartpen.util.VideoUtil;
import com.cleverm.smartpen.util.excle.CreateExcel;
import com.cleverm.smartpen.util.parts.DoAnimationPart;
import com.cleverm.smartpen.util.parts.DoSmsSendPart;
import com.cleverm.smartpen.util.service.PenUtil;
import com.cleverm.smartpen.util.service.ScreenLockUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


/**
 * Created by xiong,An android project Engineer,on 2016/2/15.
 * Data:2016-02-16  11:25
 * Base on clever-m.com(JAVA Service)
 * Describe: 广告视频播放,该界面将作为基Activity存在,所有的其他点点笔的操作Activity都会覆盖于其上,该Activity将一直存在于栈内.
 * Version:1.0
 * Open source
 */
public class VideoActivity extends BaseActivity implements penService.MessageListener {

    public static final String TAG = VideoActivity.class.getSimpleName()+"：";

    FullScreenVideoView mAdvertFsvv;
    private RelativeLayout mrlNotice;
    private ImageView mivNoticeImage;
    private TextView mrlNoticeText;
    private MyText mtv;
    private UpdateTableHandlerSuccess mUpdateTableHandlerSuccess;
    public static final String SUCCESSACTION = "UpdateTableHandlerSuccess";
    public final static int updateSpeed = 3;
    public static float width;
    public static final int GET_PENSERVICE = 3;
    public static final int GET_STUB = 4;

    public static final String VIDEO_ACTIVITY_KEY = "video_activity_key";
    public static final String VIDEO_ACTIVITY_ISSEND = "video_activity_isSend";

    private volatile boolean mAlreadyDataUpdate =false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_PENSERVICE: {
                    initPenServiceListener();
                    break;
                }
                case GET_STUB: {
                    initStubListener();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fix bug double message
        if (!isTaskRoot()) {
            finish();
            return;
        }
        QuickUtils.log(TAG + "onCreate");
        setContentView(R.layout.activity_video);
        initView();
        initBroadcastReceiver();
        onCreateEventBus();
        initVideoEngine();
        RememberUtil.putBoolean(Constant.BROADCAST_RESATRT_EVENT, false);
        mHandler.sendEmptyMessage(GET_PENSERVICE);
        //initIntent();
    }




    private void initBroadcastReceiver() {
        mUpdateTableHandlerSuccess = new UpdateTableHandlerSuccess();
        registerReceiver(mUpdateTableHandlerSuccess, new IntentFilter(SUCCESSACTION));
    }

    @Override
    protected int onGetEventId() {
        return StatisticsUtil.BACK_VIDEO;
    }

    @Override
    protected String onGetDesc() {
        return StatisticsUtil.BACK_VIDEO_DESC;
    }

    private void initStats() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long start = System.currentTimeMillis();
                CreateExcel createExcel = new CreateExcel();
                String execlName = createExcel.init();
                createExcel.writeExcel(StatisticsUtil.getInstance().getDBForExcel(), execlName);
                final long end = System.currentTimeMillis();
                QuickUtils.log("export excel time" + (end - start));
                if (!RememberUtil.getBoolean(StatisticsUtil.getInstance().getEventHappenTime(), false)) {
                    StatisticsUtil.getInstance().updateExcleToService(StatisticsUtil.UPLOAD_FILE_URL, StatisticsUtil.getInstance().getStatsFile());
                }

            }
        }).start();
    }


    private void initIntent() {
        Intent in = getIntent();
        if (in == null) {
            return;
        }
        int id = in.getIntExtra(VIDEO_ACTIVITY_KEY, 0);
        boolean isSend = in.getBooleanExtra(VIDEO_ACTIVITY_ISSEND, false);
        handlerCode(id, isSend);
    }

    private void initPenServiceListener() {
        penService penService = PenUtil.getInstance().getPenServcie();
        if (penService == null) {
            mHandler.sendEmptyMessageDelayed(GET_PENSERVICE, 500);
        } else {
            penService.setMessageListener(this);
        }
    }

    private void initStubListener() {
        ScreenLockListenService.ScreenLockListenServiceStub stub = ScreenLockUtil.getInstance().getStub();
        if (stub == null) {
            mHandler.sendEmptyMessageDelayed(GET_STUB, 5000);
        } else {
            stub.setTaskId(getTaskId());
            stub.setWindow(getWindow());
        }
    }

    private void initView() {
        mAdvertFsvv = (FullScreenVideoView) findViewById(R.id.vvAdvertisement);
        mrlNotice = (RelativeLayout) findViewById(R.id.rlNotice);
        mivNoticeImage = (ImageView) findViewById(R.id.ivNoticeImage);
        mrlNoticeText = (TextView) findViewById(R.id.rlNoticeText);
        /**
         *跑马灯效果，可点击控制启动与暂停
         */
        mtv = (MyText) findViewById(R.id.tv_setdesk);
        ViewTreeObserver vto = mtv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mtv.getHeight();
                width = mtv.getWidth();
                mtv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mtv.init(getWindowManager());
                mtv.startScroll();
                mtv.setTextColor(Color.WHITE);
            }
        });
    }


    private void initVideoEngine() {
        if (QuickUtils.isHasVideoFolder() && QuickUtils.isVideoFolderHaveFiel2()) {
            if (QuickUtils.isHasVideoFolder(AlgorithmUtil.VIDEO_FILE_PLAY) && QuickUtils.isVideoFolderHaveFiel2(AlgorithmUtil.VIDEO_FILE_PLAY)) {
                VideoUtil videoUtil = new VideoUtil(mAdvertFsvv);
                videoUtil.prepareLocalVideo(AlgorithmUtil.VIDEO_FILE_PLAY, 0);
            } else {
                goUpdateVideo();
            }
        } else {
            goUpdateVideo();
        }
    }

    private void initData() {
        QuickUtils.log("onBootRestartEvent:doUpdateVideo");
        mAlreadyDataUpdate = true;
        AlgorithmUtil.getInstance().startVideoPlayAlgorithm(mAdvertFsvv, VideoActivity.this);
    }

    private void goUpdateVideo(){
        if(!mAlreadyDataUpdate){
            initData();
        }
    }


    /**
     * 在处理Video前将特惠专区的json数据保存到本地的文件中，然后每天都是读取的该次数据
     * 将只取一次的数据放入Cache中
     */
    private void initCacheJson() {
        DownloadUtil.cacheDiscountJson(QuickUtils.getOrgIdFromSp());
    }


    /**
     * implements penService.MessageListener
     *
     * @param id
     */
    @Override
    public void receiveData(int id, boolean isSend) {
        handlerCode(id, isSend);
    }

    private void handlerCode(int penCode, boolean isSend) {
        BleLog.e("onMessageCallEvent--->"+isSend);
        DoSmsSendPart.getInstance().sendSms(penCode, isSend);
    }


    public class UpdateTableHandlerSuccess extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SUCCESSACTION.equals(action)) {
                mtv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 不锁屏
     */
    private void unLockScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    /**
     * 是否显示桌号设置:数据库为空或者没有选择桌号
     */
    private void whetherDeskInfo() {
        List<TableType> mTableTypes = DatabaseHelper.getsInstance(this).obtainAllTableTypes();
        long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        if (mTableTypes == null || mTableTypes.size() == 0 || deskId == Constant.DESK_ID_DEF_DEFAULT) {
            if (mtv != null) {
                mtv.setVisibility(View.VISIBLE);
            }
        } else {
            if (mtv != null) {
                mtv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 暂停或继续播放视频
     */
    int videoValue = 0;

    private void startPlayVideo() {
        videoValue = RememberUtil.getInt(Constant.MEMORY_PLAY_KEY, 0);
        mAdvertFsvv.start();
        if (false) {
            doSeekTo();
        }
    }

    private void stopPlayVideo() {
        RememberUtil.putInt(Constant.MEMORY_PLAY_KEY, mAdvertFsvv.getCurrentPosition());
        mAdvertFsvv.pause();
    }

    /**
     * 因为VideoActivity会被不断的重启,算法太耗时导致必须延迟
     */
    private void doSeekTo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdvertFsvv.seekTo(videoValue);
                mAdvertFsvv.start();
            }
        }, 250);
    }

    private volatile boolean isSend=false;



    /**
     * OnEvnet
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public synchronized void onMessageCallEvent(OnMessageCallEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        QuickUtils.log("onMessageCallEvent====" + event.toString() + "/" + isSend);
        handlerCode(event.getId(), event.getIsSend());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onRobotShowEvent(OnRobotShowEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        DoAnimationPart.getInstance().doAnimation(event.getPenCode());
    }


    @Subscribe
    public void onChangeAnimNoticeEvent(OnChangeAnimNoticeEvent event) {
        if (event.getType() == OnChangeAnimNoticeEvent.Status.HIDE) {
            mrlNotice.setVisibility(View.GONE);
        } else if (event.getType() == OnChangeAnimNoticeEvent.Status.SHOW) {
            mrlNotice.setVisibility(View.VISIBLE);
            mrlNoticeText.setText(event.getText());
            mivNoticeImage.setImageResource(event.getImageResoure());
        }
    }

    @Subscribe
    public void onVideoUpdateEvent(OnVideoUpdateEvent event) {
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onVideoPlayMemoryEvent(OnVideoPlayMemoryEvent event) {
        //doSeekTo();
    }

    @Subscribe
    public void onDefaultEvent(DefaultEvent event) {
    }


    /**只有重启后的第一次才去取数据*/
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBootRestartEvent(OnBootRestartEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        new VersionManager(this).uddateVersion();
        goUpdateVideo();
        initCacheJson();
        initStats();
        SchedulingUtil.doOnDemo(this);
        UpdateTableUtil.getInstance().goNewTable();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDestoryActivityEvent(final OnDestoryActivityEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        event.getActivity().finish();
    }

    private void onCreateEventBus() {
        boolean registered = EventBus.getDefault().isRegistered(this);
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().register(this);

    }

    private void ClearStickEvent() {
        EventBus.getDefault().removeStickyEvent(OnMessageCallEvent.class);
        EventBus.getDefault().removeStickyEvent(OnDestoryActivityEvent.class);
        EventBus.getDefault().removeStickyEvent(OnRobotShowEvent.class);
    }



    /**
     * 横竖屏数据处理逻辑
     * 在onPause中记下位置
     * 在onResume中从该位置播放
     * 记录到SharePrefrence中
     */
    @Override
    protected void onPause() {
        super.onPause();
        QuickUtils.log(TAG + "onPause");
        stopPlayVideo();
    }


    @Override
    protected void onResume() {
        super.onResume();
        QuickUtils.log(TAG + "onResume");
        unLockScreen();
        whetherDeskInfo();
        startPlayVideo();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        QuickUtils.log(TAG+"onDestroy");
        EventBus.getDefault().unregister(this);
        try {
            unregisterReceiver(mUpdateTableHandlerSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        QuickUtils.log("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        QuickUtils.log("onStop");
    }




}
