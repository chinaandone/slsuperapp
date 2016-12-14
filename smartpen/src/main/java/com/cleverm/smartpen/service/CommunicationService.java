package com.cleverm.smartpen.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.cleverm.smartpen.pushtable.ClevermClient;
import com.cleverm.smartpen.pushtable.Config;
import com.cleverm.smartpen.pushtable.ConnectionCallback;
import com.cleverm.smartpen.pushtable.HotfixHandler;
import com.cleverm.smartpen.pushtable.MessageType;
import com.cleverm.smartpen.pushtable.OrgProfileVo;
import com.cleverm.smartpen.pushtable.PushTablePreferencesHelper;
import com.cleverm.smartpen.pushtable.RequestHandler;
import com.cleverm.smartpen.pushtable.RestaurantVo;
import com.cleverm.smartpen.pushtable.UpdateTableHandler;
import com.cleverm.smartpen.pushtable.Utils;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.ThreadManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.apache.http.HttpStatus;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import de.tavendo.autobahn.WebSocketConnection;

/**
 * Created by Levy on 2015/5/25.
 */
public class CommunicationService extends Service {
    private static final String TAG = CommunicationService.class.getSimpleName();
    private static final long TIME_OUT = 60000;    // 60s
    private static final long MIN_CONNECT_SOCKET_DELAY = 5000;
    private static final int DELAY_HEART_BEAT = 30000;
    private static final int MSG_EXECUTE_SERVER_MESSAGE = 5;
    
    private static final String REQUEST_TYPE_UPLOAD_QUEUING_VO = "REQUEST_TYPE_UPLOAD_QUEUING_VO";
    private static final String REQUEST_TYPE_UPLOAD_DINING_VO = "REQUEST_TYPE_UPLOAD_DINING_VO";
    private static final String REQUEST_TYPE_UPLOAD_BONUS_VO = "REQUEST_TYPE_UPLOAD_BONUS_VO";

    private static final String WS_URL = "ws://www.myee7.com/cleverm/sockjs/notification";
    private static final String SYNCHRO_URL =
        Constant.DDP_URL+"/cleverm/sockjs/execCommand";
    private final WebSocketConnection mConnection = new WebSocketConnection();
    private final Queue<String> mTextMsgQueue = new LinkedList<String>();
    private final HashMap<String, HttpHandler> mHandlerMap = new HashMap<String, HttpHandler>();
    private final Handler mHandler = new Handler();
    private final Runnable mHeartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "HEART BEAT SENT");
            if (mConnectClient == null) {
                return;
            }
            final com.cleverm.smartpen.pushtable.Message message =
                    com.cleverm.smartpen.pushtable.Message.create().boardNumber(
                    Utils.getSerialNumber(CommunicationService.this)).messageType(
                    MessageType.PING).build();
            ThreadManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    mConnectClient.sendMessage(message);
                }
            });
            mHandler.postDelayed(this, DELAY_HEART_BEAT);
        }
    };
    private long mLastConnectDelay = 0;
    private ServiceDelegate mBinder;
    private ClevermClient mConnectClient;
    private final Runnable mReconnectRunnable = new Runnable() {
        @Override
        public void run() {
            startStomp();
        }
    };
    private HandlerThread mOperationThread;
    private Handler mExecutor;

    public interface OnFetchOrgProfileListener {
        void onComplete(RestaurantVo vo);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mOperationThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        mOperationThread.start();
        mExecutor = new Handler(mOperationThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_EXECUTE_SERVER_MESSAGE: {
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (intent == null) {
            return Service.START_STICKY;
        }
        String action = intent.getAction();
        Log.d(TAG, "action="+action);
        if (Constant.ACTION_SEND_MESSAGE.equals(action)) {
            String msg = intent.getStringExtra(Constant.KEY_MESSAGE);
            sendTextMessageInternal(msg);

        } else if (Constant.ACTION_UPDATE_PACKAGE.equals(action)) {
            fireUpdateOperation();

        } else if (Constant.ACTION_CANCEL_OPERATION.equals(action)) {
            cancelOperation();

        } else if (Constant.ACTION_CONNECT_SOCKET.equals(action)) {
            startStomp();

        } else if (Constant.ACTION_UPLOAD_LOGGING.equals(action)) {
            long token = intent.getLongExtra(Constant.KEY_TOKEN, -1);
            mExecutor.post(new UploadQueuingVoTask(token));
            mExecutor.post(new UploadDiningVoTask(token));
            
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new ServiceDelegate();
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOperationThread.quit();
    }

    private void startStomp() {
        Config config = Config.create(Config.Mode.REMOTE, Utils.getSerialNumber(this));
        config.setWebsocketUrl(WS_URL);
        config.setHttpUrl(SYNCHRO_URL);
        mConnectClient = new ClevermClient(getApplicationContext(), config);
        mConnectClient.subscribe(new UpdateTableHandler(getApplicationContext()));
        mConnectClient.subscribe(new HotfixHandler(getApplicationContext()));
        Log.v(TAG,"startStomp()");
        mConnectClient.addConnectionCallback(new ConnectionCallback() {
            @Override
            public void onStateChange(boolean connected, Config config) {
                if (connected) {
                    mConnectClient.registerClient();
                    mHandler.postDelayed(mHeartBeatRunnable, DELAY_HEART_BEAT);
                    mHandler.postDelayed(mUploadLoggingsRunnable, 5000l);
                }
            }
        });
        
        mConnectClient.registerRequestHandler(REQUEST_TYPE_UPLOAD_QUEUING_VO, new RequestHandler(){
            @Override
            public void onSuccess(String requestId) {
                Log.i(TAG, "onSuccess : " + REQUEST_TYPE_UPLOAD_QUEUING_VO + " " + requestId);
//                SQLiteDatabase db = DatabaseHelper.getInstance(CommunicationService.this).getWritableDatabase();
//                db.delete(BackupQueueColumns.TABLE_NAME, BackupQueueColumns.LAST_MODIFIED_DATE + " =? ", new String[]{requestId});
            }
        });
        
        mConnectClient.registerRequestHandler(REQUEST_TYPE_UPLOAD_DINING_VO, new RequestHandler(){

            @Override
            public void onSuccess(String requestId) {
                Log.i(TAG, "onSuccess : " + REQUEST_TYPE_UPLOAD_DINING_VO + " " + requestId);
//              SQLiteDatabase db = DatabaseHelper.getInstance(CommunicationService.this).getWritableDatabase();
//              db.delete(TableRecordColumns.TABLE_NAME, TableRecordColumns.LAST_MODIFIED_DATE + " =? ", new String[]{requestId});
            }
            
        });
        
        mConnectClient.registerRequestHandler(REQUEST_TYPE_UPLOAD_BONUS_VO, new RequestHandler(){

            @Override
            public void onSuccess(String requestId) {
                Log.i(TAG, "onSuccess : " + REQUEST_TYPE_UPLOAD_BONUS_VO + " " + requestId);
            }
            
        });
    }

    private void fireUpdateOperation() {
        String url =
            "http://p.gdown.baidu.com/d9cef1aba17830a59106fa60f6bafd61e8211f43a9c36a1c5e6f534a14affb861b90e1aab1536dab3a88251822f54d428875b3b79f0994e0faf62b2abc3e835f2988166da32edcc2f1c5a9638617499a994e969f6ee8c3e6257aa5634e4076b4c393a7a492257cbf849ea7358bf1422fdd79a60c1e155393421e9c2b079b8c31ee73c1304c55136867e7e301cff662496d4e1393783979be6e5c1095ab56c2a4c70a773ab4f8f637af011d2a523c8b8c5422628093c70cabe3ea691553b73d6a684d20c1faa9d5117df59db714ac9bc72093102d7e6c0d3e2ff9b8e512157c5c84c2df645448f308a175fdee9c248e8571d30732b3fa78b4545038092d7814f8652cf88e506cf4b8c3ceb443447bd41a0f341841b5eebc464e3a58d908866a1b";
        String squence_id = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        File target = Utils.generateUpdatePackageFile(getApplicationContext(), squence_id);

        DownloadCallBack callBack = new DownloadCallBack(squence_id);
        HttpUtils utils = new HttpUtils();
        HttpHandler handler = utils.download(url, target.getAbsolutePath(), true, false, callBack);
        mHandlerMap.put(squence_id, handler);
    }

    private void cancelOperation() {
        for (HttpHandler httpHandler : mHandlerMap.values()) {
            httpHandler.cancel();
        }
    }

    private boolean sendTextMessageInternal(String msg) {
        if (mConnection.isConnected()) {
            mConnection.sendTextMessage(msg);
            return true;
        } else {
            mTextMsgQueue.add(msg);
            return false;
        }
    }

    private void scheduleReconnect() {
        mLastConnectDelay += MIN_CONNECT_SOCKET_DELAY;
        if (mLastConnectDelay > TIME_OUT) {
            Toast.makeText(getBaseContext(),
                "Cannot connect server, please check network connection", Toast.LENGTH_SHORT)
                .show();
        } else {
            Log.i(TAG, "pending connect after : " + mLastConnectDelay + "ms");
        }
        mHandler.postDelayed(mReconnectRunnable, mLastConnectDelay);
    }

    private static int BACKUP_QUEUE_INDEX_NUMBER = 0;
    private static int BACKUP_QUEUE_INDEX_COUNT = 1;
    private static int BACKUP_QUEUE_INDEX_TYPE_ID = 2;
    private static int BACKUP_QUEUE_INDEX_QUEUE_DATE = 3;
    private static int BACKUP_QUEUE_INDEX_CALL_DATE = 4;
    private static int BACKUP_QUEUE_INDEX_CALL_TIMES = 5;
    private static int BACKUP_QUEUE_INDEX_STATE = 6;
    private static int BACKUP_QUEUE_INDEX_MATCHED_TABLE = 7;
    private static int BACKUP_QUEUE_INDEX_DINNER_DATE = 8;
    private static int BACKUP_QUEUE_INDEX_PASS_DATE = 9;
    private static int BACKUP_QUEUE_INDEX_RANDOM_CODE = 10;
    private static int BACKUP_QUEUE_INDEX_BONUS_STATE = 11;
    private static int BACKUP_QUEUE_INDEX_BONUS_ID = 12;

    private static final String[] BACKUP_QUEUE_PROJECTION = {
//        BackupQueueColumns.NUMBER,
//        BackupQueueColumns.COUNT,
//        BackupQueueColumns.TABLE_TYPE_ID,
//        BackupQueueColumns.QUEUE_DATE,
//        BackupQueueColumns.CALL_DATE,
//        BackupQueueColumns.CALL_TIMES,
//        BackupQueueColumns.STATE,
//        BackupQueueColumns.MATCHED_TABLE,
//        BackupQueueColumns.DINNER_DATE,
//        BackupQueueColumns.PASS_DATE,
//        BackupQueueColumns.RANDOM_CODE,
//        BackupQueueColumns.BONUS_STATE,
//        BackupQueueColumns.BONUS_ID,
    };

    private void uploadQueuingVo(long token) {
//        if (token == -1) {
//            return;
//        }
//
//        long clientID = PreferencesHelper.getInstance(this).getLong(Constant.KEY_CLIENT_ID, -1);
//        if (clientID == -1) {
//            return;
//        }
//
//        long orgID = PreferencesHelper.getInstance(this).getLong(Constant.KEY_RESTAURANT_ID, -1);
//        if (orgID == -1) {
//            return;
//        }
//
//        String orgName = PreferencesHelper.getInstance(this).getString(
//            Constant.KEY_RESTAURANT_NAME);
//        if (orgName == null) {
//            return;
//        }
//
//        List<RQueuingVo> queuingVoList = new ArrayList<>();
//        SQLiteDatabase db = DatabaseHelper.getInstance(this).getReadableDatabase();
//        try {
//            Cursor cursor = db.query(BackupQueueColumns.TABLE_NAME, BACKUP_QUEUE_PROJECTION,
//                BackupQueueColumns.LAST_MODIFIED_DATE + "=?",
//                new String[]{String.valueOf(token)}, null, null, "_id ASC");
//            while (cursor != null && cursor.moveToNext()) {
//                int tableSn = cursor.getInt(BACKUP_QUEUE_INDEX_MATCHED_TABLE);
//                Long tableId = TableInfoImpl.getTableIdBySn(db, tableSn);
//                long callDate = cursor.getLong(BACKUP_QUEUE_INDEX_CALL_DATE);
//                long dinnerDate = cursor.getLong(BACKUP_QUEUE_INDEX_DINNER_DATE);
//                long passDate = cursor.getLong(BACKUP_QUEUE_INDEX_PASS_DATE);
//                Date timeFallOut = null;
//                if (callDate != 0) {
//                    timeFallOut = new Date(callDate);
//                }else if (dinnerDate != 0){
//                    timeFallOut = new Date(dinnerDate);
//                }else if (passDate != 0){
//                    timeFallOut = new Date(passDate);
//                }
//
//                RQueuingVo vo = new RQueuingVo(cursor.getString(BACKUP_QUEUE_INDEX_NUMBER),
//                    cursor.getInt(BACKUP_QUEUE_INDEX_COUNT),
//                    cursor.getLong(BACKUP_QUEUE_INDEX_TYPE_ID),
//                    cursor.getLong(BACKUP_QUEUE_INDEX_QUEUE_DATE),
//                    cursor.getInt(BACKUP_QUEUE_INDEX_CALL_TIMES),
//                    new Date(cursor.getLong(BACKUP_QUEUE_INDEX_QUEUE_DATE)), timeFallOut,
//                    String.valueOf(cursor.getInt(BACKUP_QUEUE_INDEX_STATE)), tableId, null,
//                    cursor.getInt(BACKUP_QUEUE_INDEX_RANDOM_CODE),
//                    cursor.getInt(BACKUP_QUEUE_INDEX_BONUS_STATE),
//                    cursor.getInt(BACKUP_QUEUE_INDEX_BONUS_ID));
//                queuingVoList.add(vo);
//            }
//            Utils.closeSilently(cursor);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        RLoggingVo loggingVo = new RLoggingVo(clientID, orgID, orgName,
//            Calendar.getInstance().getTime(), queuingVoList, null, null);
//        Log.i(TAG, "uploadQueuingVo requestId : " + token);
//        mConnectClient.sendNotice("RESTAURANT_LOGGING", REQUEST_TYPE_UPLOAD_QUEUING_VO, String.valueOf(token), loggingVo);
    }
    
    private class UploadQueuingVoTask implements Runnable {
        long token;
        
        public UploadQueuingVoTask(long token) {
            this.token = token;
        }
        
        @Override
        public void run() {
            uploadQueuingVo(token);
        }
    }
    
    private static int TABLE_RECORD_INDEX_ID = 0;
    private static int TABLE_RECORD_INDEX_TYPE_ID = 1;
    private static int TABLE_RECORD_INDEX_TABLE_ID = 2;
    private static int TABLE_RECORD_INDEX_MATCHED_CUSTOMER_NUMBER = 3;
    private static int TABLE_RECORD_INDEX_OCCUPIED_DATE = 4;
    private static int TABLE_RECORD_INDEX_FREE_DATE = 5;
    private static int TABLE_RECORD_INDEX_NEXT_OCCUPIED_DATE = 6;

    private static final String[] TABLE_RECORD_PROJECTION = {
//        TableRecordColumns._ID,
//        TableRecordColumns.TYPE_ID,
//        TableRecordColumns.TABLE_ID,
//        TableRecordColumns.MATCHED_CUSTOMER_NUMBER,
//        TableRecordColumns.OCCUPIED_DATE,
//        TableRecordColumns.FREE_DATE,
//        TableRecordColumns.NEXT_OCCUPIED_DATE,
    };
    
    private void uploadDiningVo(long token) {
//        if (token == -1) {
//            return;
//        }
//
//        long clientID = PreferencesHelper.getInstance(this).getLong(Constant.KEY_CLIENT_ID, -1);
//        if (clientID == -1) {
//            return;
//        }
//
//        long orgID = PreferencesHelper.getInstance(this).getLong(Constant.KEY_RESTAURANT_ID, -1);
//        if (orgID == -1) {
//            return;
//        }
//
//        String orgName = PreferencesHelper.getInstance(this).getString(
//            Constant.KEY_RESTAURANT_NAME);
//        if (orgName == null) {
//            return;
//        }
//
//        List<RDiningVo> diningVoList = new ArrayList<>();
//        SQLiteDatabase db = DatabaseHelper.getInstance(this).getReadableDatabase();
//        try {
//            Cursor cursor = db.query(TableRecordColumns.TABLE_NAME, TABLE_RECORD_PROJECTION,
//                TableRecordColumns.LAST_MODIFIED_DATE + "=?",
//                new String[]{String.valueOf(token)}, null, null, "_id ASC");
//            while (cursor != null && cursor.moveToNext()) {
//                long occupiedDate = cursor.getLong(TABLE_RECORD_INDEX_OCCUPIED_DATE);
//                long freeDate = cursor.getLong(TABLE_RECORD_INDEX_FREE_DATE);
//                long nextOccupiedDate = cursor.getLong(TABLE_RECORD_INDEX_NEXT_OCCUPIED_DATE);
//                Date timeSeated = null;
//                Date timeLeft = null;
//                Date timeNextSeated = null;
//                if (occupiedDate != 0){
//                    timeSeated = new Date(occupiedDate);
//                }
//                if (freeDate != 0){
//                    timeLeft = new Date(freeDate);
//                }
//                if (nextOccupiedDate != 0){
//                    timeNextSeated = new Date(nextOccupiedDate);
//                }
//
//                if (timeSeated == null) {
//                    continue;
//                }
//
//                RDiningVo vo = new RDiningVo(cursor.getLong(TABLE_RECORD_INDEX_TYPE_ID),
//                        cursor.getLong(TABLE_RECORD_INDEX_TABLE_ID),
//                        occupiedDate,
//                        timeSeated,
//                        timeLeft,
//                        timeNextSeated,
//                        timeSeated,
//                        cursor.getString(TABLE_RECORD_INDEX_MATCHED_CUSTOMER_NUMBER));
//                diningVoList.add(vo);
//            }
//            Utils.closeSilently(cursor);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        RLoggingVo loggingVo = new RLoggingVo(clientID, orgID, orgName,
//            Calendar.getInstance().getTime(), null, diningVoList, null);
//        Log.i(TAG, "uploadDiningVo requestId : " + token);
//        mConnectClient.sendNotice("RESTAURANT_LOGGING", REQUEST_TYPE_UPLOAD_DINING_VO, String.valueOf(token), loggingVo);
    }
    
    private class UploadDiningVoTask implements Runnable {
        long token;
        
        public UploadDiningVoTask(long token) {
            this.token = token;
        }
        
        @Override
        public void run() {
            uploadDiningVo(token);
        }
    }
    
    private static int BONUS_INDEX_ID = 0;
    private static int BONUS_INDEX_RANK = 1;
    private static int BONUS_INDEX_NAME = 2;
    private static int BONUS_INDEX_WHEEL_POSITION = 3;
    private static int BONUS_INDEX_PRIMARY_PROBABILITY = 4;
    private static int BONUS_INDEX_SECOND_PROBABILITY = 5;

    private static final String[] BONUS_PROJECTION = {
//        BonusPrizeColumns._ID,
//        BonusPrizeColumns.RANK,
//        BonusPrizeColumns.NAME,
//        BonusPrizeColumns.WHEEL_POSITION,
//        BonusPrizeColumns.PRIMARY_PROBABILITY,
//        BonusPrizeColumns.SECOND_PROBABILITY,
    };
    
    private void uploadBonusVo(long token) {
//        if (token == -1) {
//            return;
//        }
//
//        long clientID = PreferencesHelper.getInstance(this).getLong(Constant.KEY_CLIENT_ID, -1);
//        if (clientID == -1) {
//            return;
//        }
//
//        long orgID = PreferencesHelper.getInstance(this).getLong(Constant.KEY_RESTAURANT_ID, -1);
//        if (orgID == -1) {
//            return;
//        }
//
//        String orgName = PreferencesHelper.getInstance(this).getString(
//            Constant.KEY_RESTAURANT_NAME);
//        if (orgName == null) {
//            return;
//        }
//
//        List<RBonusVo> bonusVoList = new ArrayList<>();
//        SQLiteDatabase db = DatabaseHelper.getInstance(this).getReadableDatabase();
//        try {
//            Cursor cursor = db.query(BonusPrizeColumns.TABLE_NAME, BONUS_PROJECTION,
//                    BonusPrizeColumns.LAST_MODIFIED_DATE + "=?",
//                new String[]{String.valueOf(token)}, null, null, "_id ASC");
//            while (cursor != null && cursor.moveToNext()) {
//                int rank = cursor.getInt(BONUS_INDEX_RANK);
//                String uniqueId = null;
//                uniqueId = "" + rank + token;
//
//                RBonusVo vo = new RBonusVo(cursor.getInt(BONUS_INDEX_RANK),
//                        cursor.getString(BONUS_INDEX_NAME), cursor.getInt(BONUS_INDEX_WHEEL_POSITION),
//                        cursor.getInt(BONUS_INDEX_PRIMARY_PROBABILITY),
//                        cursor.getInt(BONUS_INDEX_SECOND_PROBABILITY),
//                        new Date(token), uniqueId);
//
//                bonusVoList.add(vo);
//            }
//            Utils.closeSilently(cursor);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        RLoggingVo loggingVo = new RLoggingVo(clientID, orgID, orgName,
//            Calendar.getInstance().getTime(), null, null, bonusVoList);
//        Log.i(TAG, "uploadBonusVo requestId : " + token);
//        mConnectClient.sendNotice("RESTAURANT_LOGGING", REQUEST_TYPE_UPLOAD_BONUS_VO, String.valueOf(token), loggingVo);
    }
    
    private class UploadBonusVoTask implements Runnable {
        long token;
        
        public UploadBonusVoTask(long token) {
            this.token = token;
        }
        
        @Override
        public void run() {
            uploadBonusVo(token);
        }
    }
    
    private final Runnable mUploadLoggingsRunnable = new Runnable() {
        @Override
        public void run() {
//            SQLiteDatabase db = DatabaseHelper.getInstance(CommunicationService.this).getReadableDatabase();
//            try {
//                Cursor cursor = db.query(BackupQueueColumns.TABLE_NAME, new String[] { BackupQueueColumns.LAST_MODIFIED_DATE }, null, null,
//                        BackupQueueColumns.LAST_MODIFIED_DATE, null, BackupQueueColumns._ID + " ASC");
//                while (cursor != null && cursor.moveToNext()) {
//                    long token = cursor.getLong(0);
//                    if (token > 0) {
//                        mExecutor.post(new UploadQueuingVoTask(token));
//                    }
//                }
//                Utils.closeSilently(cursor);
//
//                cursor = db.query(TableRecordColumns.TABLE_NAME, new String[] { TableRecordColumns.LAST_MODIFIED_DATE }, null, null,
//                        TableRecordColumns.LAST_MODIFIED_DATE, null, TableRecordColumns._ID + " ASC");
//                while (cursor != null && cursor.moveToNext()) {
//                    long token = cursor.getLong(0);
//                    if (token > 0) {
//                        mExecutor.post(new UploadDiningVoTask(token));
//                    }
//                }
//                Utils.closeSilently(cursor);
//
//                cursor = db.query(BonusPrizeColumns.TABLE_NAME, new String[] { TableRecordColumns.LAST_MODIFIED_DATE }, null, null,
//                        BonusPrizeColumns.LAST_MODIFIED_DATE, null, BonusPrizeColumns._ID + " ASC");
//                while (cursor != null && cursor.moveToNext()) {
//                    long token = cursor.getLong(0);
//                    if (token > 0) {
//                        mExecutor.post(new UploadBonusVoTask(token));
//                    }
//                }
//                Utils.closeSilently(cursor);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }
    };
    
    public static abstract class BaseCallback<T> extends RequestCallBack<T> {

        public BaseCallback(Object tag) {
            setUserTag(tag);
        }

        @Override
        public void onStart() {
            super.onStart();
            reportTaskStarted();
        }

        private void reportTaskStarted() {
            String squenceid = (String) getUserTag();
            Log.d(TAG, "Task : " + squenceid + " STARTED");
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            reportTaskCancelled();
        }

        private void reportTaskCancelled() {
            String squenceid = (String) getUserTag();
            Log.d(TAG, "Task : " + squenceid + " CANCELED");
        }
    }

    private <Tin, Tout> Tout requestNotice(String noticeType, Object in, Class<Tout> outClass)
        throws Exception {
        if (mConnectClient == null) {
            return null;
        }
        com.cleverm.smartpen.pushtable.Message message =
                com.cleverm.smartpen.pushtable.Message.create().messageType(
                MessageType.NOTIFICATION).header("Notice-Type", noticeType).json(in).build();
        return mConnectClient.requestByPost(message, outClass);

    }

    private void requestTableType(OnFetchOrgProfileListener listener) {
        mExecutor.post(new RequestRestaurantVoTask(this, listener));
    }

    private class RequestRestaurantVoTask implements Runnable {
        Context context;
        OnFetchOrgProfileListener listener;

        public RequestRestaurantVoTask(Context context, OnFetchOrgProfileListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        public void run() {
            OrgProfileVo orgProfileVo = new OrgProfileVo();
            PushTablePreferencesHelper preferencesHelper = PushTablePreferencesHelper.getInstance(context);
            orgProfileVo.setOrgID(preferencesHelper.getLong(Constant.KEY_RESTAURANT_ID, 0));
            orgProfileVo.setOrgName(preferencesHelper.getString(Constant.KEY_RESTAURANT_NAME));
            orgProfileVo.setProfileMd5(preferencesHelper.getString(Constant.KEY_TABLE_TYPE_MD5,
                ""));

            try {
                RestaurantVo vo = requestNotice("FETCH_ORG_INFO", orgProfileVo,
                    RestaurantVo.class);
                if (listener != null) {
                    listener.onComplete(vo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ServiceDelegate extends Binder {

        public boolean sendTextMessage(String msg) {
            return sendTextMessageInternal(msg);
        }

        public boolean updateTableInfo() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        public boolean getConnectServerState() {
            if (mConnectClient == null) {
                return false;
            }
            return mConnectClient.getConnectState();
        }

        public void requestTableType(OnFetchOrgProfileListener listener) {
            CommunicationService.this.requestTableType(listener);
        }

    }

    public class DownloadCallBack extends BaseCallback<File> {

        public DownloadCallBack(Object tag) {
            super(tag);
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            super.onLoading(total, current, isUploading);
            Log.d("DK", "onLoading[" + current + "/" + total + "]");
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            String path = responseInfo.result.getAbsolutePath();
            String squenceid = (String) getUserTag();
            mHandlerMap.remove(squenceid);
            reportDownloadSucceed(path);

            // Broadcast Sample
            LocalBroadcastManager lbmgr = LocalBroadcastManager.getInstance(
                    getApplicationContext());
            Intent intent = new Intent(Constant.ACTION_UPDATE_PACKAGE_READY);
            intent.putExtra(Constant.KEY_UPDATE_PACKAGE_PATH, path);
            intent.putExtra(Constant.KEY_SEQUENCE_ID, squenceid);
            lbmgr.sendBroadcast(intent);
            // TODO : Or We can alert a system dialog just using service context
        }

        private void reportDownloadSucceed(String path) {
            String squenceid = (String) getUserTag();
            Log.d(TAG, "onSuccess " + squenceid + " " + path);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            int code = e.getExceptionCode();
            Log.d(TAG, "onFailure " + s, e);
            Log.d(TAG, "code " + code);
            if (code == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE) {
                String squence_id = (String) getUserTag();
                File target = Utils.generateUpdatePackageFile(getApplicationContext(), squence_id);
                if (target.isFile() && target.exists()) {
                    if (target.delete()) {
                        fireUpdateOperation();
                    }
                }
            }
        }
    }
}
