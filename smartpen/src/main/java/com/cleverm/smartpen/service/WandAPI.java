package com.cleverm.smartpen.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.cleverm.smartpen.util.event.util.BroadcastCx;
import com.cleverm.smartpen.util.event.util.BroadcastUtil;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jimmy on 2015/9/11.
 * <p/>
 * Api of wharton alerting network database.
 */
public class WandAPI {

    @SuppressWarnings("unused")
    private static final String TAG = WandAPI.class.getSimpleName();

    private static final String ACTION_USB_PERMISSION = "com.cleverm.smartpen" +
            ".WAND_PERMISSION";
    private static final String[] DEVICES = {"1a86:7523", "1a86:5523",
            "04f2:0111"};
    private static final int CONTROL_TRANSFER_REQUEST_TYPE = 192;
    private static final int CONTROL_TRANSFER_REQUEST = 149;
    private static final int CONTROL_TRANSFER_VALUE = 9496;
    private static final int CONTROL_TRANSFER_INDEX = 0;
    private static final int CONTROL_TRANSFER_LENGTH = 2;
    private static final int CONTROL_TRANSFER_DEFAULT_TIMEOUT = 500;
    private final WandBroadcastReceiver mWandBroadcastReceiver = new WandBroadcastReceiver();
    private Context mContext;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private UsbManager mUsbManager;
    private UsbInterface mUsbInterface;
    private UsbDeviceConnection mUsbDeviceConn;
    private OnScanListener mOnScanListener;

    final int find = 1;
    final int stopfind = 2;
    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case find: {
                    connect();
                    break;
                }
                case stopfind: {
                    hand.removeMessages(find);
                    break;
                }
            }
        }
    };
    private Runnable mScanRunnable = new Runnable() {
        private byte[] buffer = new byte[8];

        @Override
        public void run() {
            mHandler.removeCallbacks(mScanRunnable);
            if (mUsbDeviceConn != null) {
                final int ret = mUsbDeviceConn.controlTransfer
                        (CONTROL_TRANSFER_REQUEST_TYPE,
                                CONTROL_TRANSFER_REQUEST, CONTROL_TRANSFER_VALUE,
                                CONTROL_TRANSFER_INDEX, buffer,
                                CONTROL_TRANSFER_LENGTH, CONTROL_TRANSFER_DEFAULT_TIMEOUT);
                if (ret >= 0) {
                    if (mOnScanListener != null) {
                        mOnScanListener.onScan(bytesToInt(buffer));
                    }
                }
            }
            mHandler.postDelayed(mScanRunnable, 500l);
        }
    };

    public WandAPI(Context context, OnScanListener listener) {
        mContext = context;
        mOnScanListener = listener;
    }

    public void onCreate() {
        mHandlerThread = new HandlerThread(TAG + "_HandlerThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mWandBroadcastReceiver.register(mContext);
        connect();
    }

    public void onDestroy() {
        mWandBroadcastReceiver.unregister(mContext);
        disconnect();
        mHandler.removeCallbacksAndMessages(null);
        mHandlerThread.quit();
        mOnScanListener = null;
    }

    private void connect() {
        Log.d(TAG, "connect.");
        mUsbManager = (UsbManager) mContext.getSystemService(Context
                .USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        if (deviceList.isEmpty()) {
            Log.v(TAG, "deviceList.isEmpty()");
            //提示未找到匹配的扫描笔
            //Toast.makeText(mContext, R.string.no_matched_wand, Toast.LENGTH_SHORT).show();
            hand.sendEmptyMessageDelayed(find, 1000000);
            return;
        }

        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            Log.v(TAG, "deviceList.is Not Empty()");
            hand.sendEmptyMessage(stopfind);
//            Toast.makeText(mContext, "find pen", Toast
//                    .LENGTH_LONG).show();
            final UsbDevice device = deviceIterator.next();
            Log.d(TAG, "device vendorId = " + device.getVendorId() + ", " +
                    "product id = " + device.getProductId());
            for (String dev : DEVICES) {

                Log.v(TAG, "dev=" + dev);

                if (String.format("%04x:%04x", device.getVendorId(), device.getProductId()).equalsIgnoreCase(dev)) {

                    Log.v(TAG, "device.getVendorId()=" + device.getVendorId() + "device.getProductId()=" + device.getProductId());

                    if (mUsbManager.hasPermission(device)) {
                        setDevices(device);
                    } else {
                        mUsbManager.requestPermission(device, PendingIntent
                                .getBroadcast(mContext,
                                        0, new Intent(ACTION_USB_PERMISSION), 0));
                    }
                    //添加代码：连接回调
                    if (mOnConnectListener != null) {
                        mOnConnectListener.onConnect();
                    }
                }
            }
        }
    }

    private void disconnect() {
        Log.d(TAG, "disconnect");
        if (mUsbDeviceConn != null) {
            if (mUsbInterface != null) {
                mUsbDeviceConn.releaseInterface(mUsbInterface);
                mUsbInterface = null;
            }
            mUsbDeviceConn.close();
            mUsbDeviceConn = null;
            //添加代码：断连回掉
            if (mOnConnectListener != null) {
                mOnConnectListener.onDisconnect();
                BroadcastUtil.post(BroadcastCx.DEF_EVENT_ID.Cx_0x0001, null);
            }
        }
    }

    private void setDevices(UsbDevice device) {
        Log.d(TAG, "set devices.");
        disconnect();

        mUsbInterface = getUsbInterface(device);
        if (mUsbInterface == null) {
            return;
        }

        mUsbDeviceConn = mUsbManager.openDevice(device);
        if (mUsbDeviceConn == null || !mUsbDeviceConn.claimInterface
                (mUsbInterface, true)) {
            return;
        }
        mHandler.postDelayed(mScanRunnable, 300);
    }

    private UsbInterface getUsbInterface(UsbDevice device) {
        Log.d(TAG, "get usb interface.");
        for (int i = 0; i < device.getInterfaceCount(); ++i) {
            final UsbInterface intf = device.getInterface(i);
            if (intf.getInterfaceClass() == UsbConstants.USB_CLASS_VENDOR_SPEC
                    && intf.getInterfaceSubclass() == 1
                    && intf.getInterfaceProtocol() == 2) {
                return intf;
            }
        }
        return null;
    }

    private int bytesToInt(byte[] bytes) {
        int addr = bytes[0] & 0xFF;
        addr |= ((bytes[1] << 8) & 0xFF00);
//        addr |= ((bytes[2] << 16) & 0xFF0000);
//        addr |= ((bytes[3] << 24) & 0xFF000000);
        return addr;
    }

    public interface OnScanListener {
        void onScan(final int id);
    }

    public interface onConnectListener {
        void onConnect();

        void onDisconnect();
    }

    private onConnectListener mOnConnectListener;

    public void setOnConnectListener(onConnectListener mOnConnectListener) {
        this.mOnConnectListener = mOnConnectListener;
    }

    class WandBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "WandBroadcastReceiver::action = " + action);
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
//                Toast.makeText(mContext, "attached pen", Toast.LENGTH_SHORT).show();
                connect();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                disconnect();
            } else if (ACTION_USB_PERMISSION.equals(action)) {
                UsbDevice device = intent.getParcelableExtra("device");
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    setDevices(device);
                }
            }
        }

        public void register(Context context) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            filter.addAction(ACTION_USB_PERMISSION);
            context.registerReceiver(this, filter);
        }

        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }
    }

}
