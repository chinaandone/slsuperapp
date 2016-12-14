package com.cleverm.smartpen.pushtable;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public class ConnectManager implements WebSocketListener, NetworkMonitor.NetworkChanged {
    private static final String TAG = "ConnectManager";
    private Handler socketHandler;
    private Handler mainHandler;
    private WebSocketClient webSocket;
    private Context context;
    private boolean socketConnected = false;
    private ConnectListener connectListener;
    private String url;
    private static final int HEARTBEAT_INTERVAL = 60000;

    interface HEARTBEAT_STATE {
        public static final int UNSUPPORT = -1;
        public static final int FAIL = 0;
        public static final int SUCCESS = 1;
    };

    private Runnable connectTask = new Runnable() {
        @Override
        public void run() {
            doConnect();
            socketHandler.removeCallbacks(this);
            socketHandler.postDelayed(this, 2000L);
        }
    };

    private Runnable heartbeatTask = new Runnable() {
        @Override
        public void run() {
            switch (connectListener.onHeartbeat()) {
            case HEARTBEAT_STATE.UNSUPPORT:
                Log.v(TAG, "heartbeat unsupport");
                socketHandler.removeCallbacks(this);
                break;
            case HEARTBEAT_STATE.FAIL:
                Log.v(TAG, "heartbeat fail");
                socketHandler.removeCallbacks(this);
                if (webSocket != null){
                    webSocket.close();
                }
                postConnectTask();
                break;
            case HEARTBEAT_STATE.SUCCESS:
                Log.v(TAG, "heartbeat success");
                socketHandler.removeCallbacks(this);
                socketHandler.postDelayed(this, HEARTBEAT_INTERVAL);
                break;
            default:
                Log.v(TAG, "heartbeat invalid value");
                break;
            }
        }
    };

    private Runnable stateChangeTask = new Runnable() {
        @Override
        public void run() {
            if (connectListener == null) {
                return;
            }
            connectListener.onStateChange(socketConnected);
        }
    };

    public ConnectManager(Context context, Config config, String url, ConnectListener stompListener) {
        HandlerThread mHandlerThread = new HandlerThread("websocket-thread");
        mHandlerThread.start();
        this.context = context;
        this.connectListener = stompListener;
        this.url = url;
        mainHandler = new Handler(Looper.getMainLooper());
        socketHandler = new Handler(mHandlerThread.getLooper());
        NetworkMonitor.addMonitor(context, this);
        if (config.getMode() == Config.Mode.LOCAL) {
            webSocket = WebSocketFactory.local(url, this);
        } else {
            webSocket = WebSocketFactory.remote(context, url, this);
        }
        socketHandler.postDelayed(connectTask, 1000l);
    }

    private void doConnect() {
        if (webSocket != null && webSocket.isDisconnected() && NetworkMonitor.isConnected(context)) {
            Log.i(TAG, "ConnectManager doConnect");
            webSocket.connect();
        }
    }

    @Override
    public void onNetworkConnect() {
        socketHandler.removeCallbacks(connectTask);
        socketHandler.postDelayed(connectTask, 1000l);
    }

    @Override
    public void onNetworkDisconnect() {
        socketConnected = false;
        socketHandler.removeCallbacks(connectTask);
        socketHandler.removeCallbacks(heartbeatTask);
    }

    private void postConnectTask() {
        socketHandler.removeCallbacks(connectTask);
        socketHandler.post(connectTask);
    }

    private void postHeartbeatTask() {
        socketHandler.removeCallbacks(heartbeatTask);
        socketHandler.postDelayed(heartbeatTask, HEARTBEAT_INTERVAL);
    }

    private void postStateChangeTask() {
        socketHandler.removeCallbacks(stateChangeTask);
        socketHandler.post(stateChangeTask);
    }

    @Override
    public void onConnect() {
        if (webSocket == null) {
            Log.v(TAG, "onConnect webSocket is null");
            return;
        }
        Log.v(TAG, "Successful connection to " + url);
        socketConnected = true;
        postStateChangeTask();
        postHeartbeatTask();
    }

    @Override
    public void onMessage(String payload) {
        try {
            final Message inMessage = Message.parse(payload);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    connectListener.onMessage(inMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onDisconnect(String reason, Exception e) {
        if (webSocket == null) {
            Log.v(TAG, "onDisconnect webSocket is null");
            return;
        }

        Log.e(TAG, "ConnectManager onDisconnect " + reason, e);
        if (socketConnected) {
            socketConnected = false;
            postStateChangeTask();
        }
        postConnectTask();
    }

    @Override
    public void onHeartbeat() {
        System.currentTimeMillis();
    }

    public boolean sendMessage(Message message) {
        if (!socketConnected) {
            return false;
        }
        String rawString = Message.toRawString(message);
        if (null != webSocket) {
            return webSocket.send(rawString);
        }
        return false;
    }

    public void close() {
        NetworkMonitor.removeMonitor(this);
        socketHandler.removeCallbacksAndMessages(null);
        mainHandler.removeCallbacksAndMessages(null);
        if (null != webSocket) {
            webSocket.close();
            webSocket = null;
        }
    }

    public boolean connected() {
        return socketConnected;
    }

    public static interface ConnectListener {

        void onStateChange(boolean connected);

        void onMessage(Message message);

        int onHeartbeat();
    }
}
