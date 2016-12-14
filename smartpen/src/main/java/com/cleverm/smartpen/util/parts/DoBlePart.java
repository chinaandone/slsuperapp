package com.cleverm.smartpen.util.parts;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bleframe.library.BleManager;
import com.bleframe.library.bundle.OnChangedBundle;
import com.bleframe.library.bundle.OnLeScanBundle;
import com.bleframe.library.bundle.OnWriteReadBundle;
import com.bleframe.library.bundle.onReadRemoteRssiBundle;
import com.bleframe.library.callback.BlatandAPICallback;
import com.bleframe.library.callback.SimpleBlatandAPICallback;
import com.bleframe.library.config.BleConfig;
import com.bleframe.library.exception.anr.ANRError;
import com.bleframe.library.exception.anr.ANRWatchDog;
import com.bleframe.library.log.BleLog;
import com.bleframe.library.profile.SmartPenProfile;
import com.bleframe.library.util.BleUtils;
import com.bleframe.library.util.TimerPlanUtil;
import com.cleverm.smartpen.R;
import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.ui.windows.FixSplashScreenView;
import com.cleverm.smartpen.ui.windows.MyWindowManager;
import com.cleverm.smartpen.util.ThreadManager;
import com.cleverm.smartpen.util.service.PenUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by xiong,An android project Engineer,on 2016/4/21.
 * Data:2016-04-21  11:17
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DoBlePart {

    private static final String TAG = "BleOperation";

    private static final String sOidText = "oid.txt";
    private static final String sPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/muyeoid";
    private static final String sDefaultOid = "001";
    private static final String sPadMode="X10H";

    private static boolean sSubTwenty=true;
    private static boolean sSubTen=true;
    private static final boolean sFunctionOpen=false;

    private static Context mContext;


    private static final String MainService="0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String WriteChar="0000fff5-0000-1000-8000-00805f9b34fb";

    /**
     * 初始化蓝牙笔
     *
     * @param context
     */
    private static void initWirelessPen(Context context) {
        mContext = context;
        String name = getOidName();
        BleConfig config = new BleConfig.Builder(context).
                setDefaultValue(false).
                setMatchType(BleConfig.MatchType.NAME).
                setBluetoothName(name).
                setMaxScanTime(15000).
                setAutoConnect().
                setAutoScan().
                setAutoNotify(). 
                setProfile(new SmartPenProfile()).
                build();
        BleManager.getInstance().init(config);
        BleManager.getInstance().onCreate(context, wirelessCallback);
        initWirelessPenTimer();
        //initBatteryTimer();
        initHeartBeat();
    }


    private static void initHeartBeat() {
        TimerPlanUtil timer =new TimerPlanUtil.Builder()
                .listener(new TimerPlanUtil.OnTickListener() {
                    @Override
                    public void onTick(long l) {
                        ThreadManager.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                byte[] val = new byte[1];
                                val[0] = 0121;
                                BleManager.getInstance().send(MainService, WriteChar, null, val, BleConfig.WriteReadAttribute.CHARACTERISTIC);
                            }
                        });
                    }
                }).looper(Looper.getMainLooper())
                .timerIntervalInSeconds(2)
                .build();
        timer.start();
    }

    /**
     * 30秒扫描一次电量,并更新电量状态
     * not open temporary
     */
    private static void initBatteryTimer() {
        if(!sFunctionOpen){
            return;
        }
        TimerPlanUtil timer = new TimerPlanUtil.Builder()
                .listener(new TimerPlanUtil.OnTickListener() {
                    @Override
                    public void onTick(long timestampInMilliseconds) {
                        BleManager.getInstance().read(SmartPenProfile.UUID_BATTERY_SERVICE, SmartPenProfile.UUID_BATTERY_WRITENOTIFY_CHARACTERISTIC, null, BleConfig.WriteReadAttribute.CHARACTERISTIC);
                    }
                })
                .looper(Looper.getMainLooper())
                .timerIntervalInSeconds(30)
                .build();
        timer.start();
    }

    /**
     * 8秒扫描Timer,防止部分型号pad会扫描断掉的硬件bug
     */
    private static void initWirelessPenTimer() {
        TimerPlanUtil timer = new TimerPlanUtil.Builder()
                .listener(new TimerPlanUtil.OnTickListener() {
                    @Override
                    public void onTick(long timestampInMilliseconds) {
                        BleManager.getInstance().startTimerScan();
                    }
                })
                .looper(Looper.getMainLooper())
                .timerIntervalInSeconds(8)
                .build();
        timer.start();
    }

    /**
     * 开启蓝牙笔连接
     * @param context
     */
    public static void openBLEPen(final Context context){
        if(DoBlePart.padNotShield() && !SmartPenApplication.getSimpleVersionFlag()){
             MyWindowManager.createSmallWindow();
             DoBlePart.initWirelessPen(context);
             //startANRSolution();
        }
        if(!DoBlePart.padNotShield()){
            new FixSplashScreenView().add();
        }
    }

    public static void closedBLEPen(){
        if(DoBlePart.padNotShield()&&!SmartPenApplication.getSimpleVersionFlag()){
            MyWindowManager.removeSmallWindow();
            BleManager.getInstance().openCloseGatt();
        }
    }



    /**
     * ANR捕获并处理:如果发生ANR,那么就直接重启pad蓝牙硬件模块
     */
    private static void startANRSolution() {
        new ANRWatchDog().setANRListener(new ANRWatchDog.ANRListener() {
            @Override
            public void onAppNotResponding(ANRError anrError) {
                Log.e("ANR-onAppNotResponding","--ANR----"+anrError.toString());
                BleManager.getInstance().openOrCloseBluetooth(false);
            }
        }).start();
    }


    /**
     * 是否屏蔽该pad型号
     * @return
     */
    public static boolean padNotShield(){
        String padMode = getPadMode();
        if(padMode.contains(sPadMode)){
            return false;
        }
        return true;
    }

    /**
     * pad型号
     * @return
     */
    private static String getPadMode(){
        Build build = new Build();
        String model = build.MODEL;
        Log.e("Pad-Mode",model);
        return model;
    }

    /**
     * 从muye-oid中读取值
     * 如果没有值,默认为Pen_001
     *
     * @return
     */
    private static String getOidName() {
        String oidName;
        try {
            oidName = BleUtils.rmSpecialSymbol("Pen_" + String.valueOf(getFileContext(sPath, sOidText, sDefaultOid).trim()));
        } catch (IOException e) {
            e.printStackTrace();
            oidName = sDefaultOid;
        }
        return oidName;
    }

    /**
     * 读Txt文件
     *
     * @param directory  目录
     * @param txtName    txt文件名
     * @param defalutOid 默认oid
     * @return txt的文本内容(可能含有特殊符号, 需要处理)
     * @throws IOException
     */
    private static String getFileContext(String directory, String txtName, String defalutOid) throws IOException {
        File file = new File(directory, txtName);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        if (file.length() <= 0) {
            return defalutOid;
        }
        if (!file.exists() || file.isDirectory()) throw new FileNotFoundException();
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result = result + "\n" + s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;


    }

    /**
     * BLE回调
     */
    public static BlatandAPICallback wirelessCallback = new SimpleBlatandAPICallback() {
        @Override
        public void onNotifyValue(OnChangedBundle onChangedBundle) {
            //Toast.makeText(SmartPenApplication.getApplication(), hexToString(onChangedBundle.getValue())+"", Toast.LENGTH_SHORT).show();
            BleLog.e(TAG + "onNotifyValue=" + onChangedBundle.getValue());
            if(onChangedBundle.getValue().equals("5102030405")||onChangedBundle.getValue().equals("0102030405")){
                return;
            }
            if(hexToString(onChangedBundle.getValue())!=-1){
                PenUtil.getInstance().getPenServcie().onScan(hexToString(onChangedBundle.getValue()));
            }
        }

        @Override
        public void onStartScan() {
            super.onStartScan();
            BleLog.e(TAG + "onStartScan");
        }

        @Override
        public boolean onDeviceFound(OnLeScanBundle info) {
            BleLog.e(TAG + "发现设备");
            return super.onDeviceFound(info);
        }

        @Override
        public void onScanTimeOut() {
            BleLog.e(TAG + "搜索超时");
            super.onScanTimeOut();
        }

        @Override
        public boolean onServiceDiscover(BluetoothGatt gatt) {
            BleLog.e(TAG + "蓝牙服务发现");
            MyWindowManager.removeSmallWindow();
            return super.onServiceDiscover(gatt);
        }

        @Override
        public void onWriteValue(OnWriteReadBundle info) {
            BleLog.e(TAG + "   onWriteValue:"+info.getWriteReadValue());
            super.onWriteValue(info);
        }

        @Override
        public void onReadValue(OnWriteReadBundle info) {
            lowPowerWarn(info);
            super.onReadValue(info);
        }

        @Override
        public void onConnectOff() {
            BleLog.e(TAG + "断开连接");
            MyWindowManager.createSmallWindow();
            super.onConnectOff();
        }

        @Override
        public void onConnectOn() {
            BleLog.e(TAG + "连接中");
            super.onConnectOn();
        }

        @Override
        public void onReadRemoteRssi(onReadRemoteRssiBundle info) {
            super.onReadRemoteRssi(info);
            BleLog.e(TAG + "onReadRemoteRssi" + info.getRssi());
        }


    };



    /**
     * 读取到的电量Service的值<20和<10时就会发短信报警,但报警该行为每个临界点只触发一次.
     * notice: temporary not open
     * @param info
     */
    private static void lowPowerWarn(OnWriteReadBundle info) {
        String hexString = BleUtils.bytesToHexString(info.getWriteReadValue());
        if(hexString!=null){
            int parseInt = Integer.parseInt(hexString,16);
            if(sSubTwenty&&parseInt<20){
                Toast.makeText(mContext,"笔电量低于20%,请充电...",Toast.LENGTH_LONG).show();
                sSubTwenty=false;
            }
            if(sSubTen&&parseInt<10){
                Toast.makeText(mContext,"笔电量低于10%,即将自动关闭!",Toast.LENGTH_LONG).show();
                sSubTen=false;
            }
        }
    }

    /**
     * 16进制转10进制
     *
     * @return
     */
    public static Integer hexToString(String hexadecimal) {
        Integer integer=-1;
        try {
            integer = Integer.valueOf(hexadecimal, 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return integer;
    }


}
