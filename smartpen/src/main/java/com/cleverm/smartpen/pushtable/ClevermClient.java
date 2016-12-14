package com.cleverm.smartpen.pushtable;

import android.content.Context;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.StringCallback;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.StringBody;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.*;

public class ClevermClient implements ConnectManager.ConnectListener {
    static final NOPStringCallback nopStringCallback = new NOPStringCallback();
    private static final String TAG = ClevermClient.class.getSimpleName();
    @SuppressWarnings({"rawtypes"})
    private Map<String, NoticeHandler> noticeHandlers = new HashMap<String, NoticeHandler>();
    private Map<String, RequestHandler> requestHandlers = new HashMap<String, RequestHandler>();
    private Set<ConnectionCallback> callbacks = new HashSet<ConnectionCallback>();
    private ConnectManager connectMgr;
    private Config config;
    private Uri httpUri;
    private AndroidHttpClient httpClient;



    public ClevermClient(Context context, Config config){
        this.config = config;
        String strHttpUri = config.getHttpUrl();
        if (strHttpUri != null) {
            this.httpUri = Uri.parse(strHttpUri);
        }
        this.connectMgr = new ConnectManager(context, config, config.getWebsocketUrl(), this);
        httpClient = createHttpClient();
    }

    public void onStateChange(boolean connected) {
        for (ConnectionCallback callback : callbacks) {
            callback.onStateChange(connected, config);
        }
    }

    @Override
    public void onMessage(Message message) {
        if (message.is(MessageType.ACK)) {
            handleAcknowledgement(message);
        } else if (message.is(MessageType.NOTIFICATION)) {
            handleNotification(message);
        } else {
            handleUnsupport(message);
        }
    }

    private void handleUnsupport(Message message) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("rawtypes")
    private void handleNotification(Message notification) {
        Headers headers = notification.headerWapper();
        String noticeType = headers.getNoticeType();
        NoticeHandler handler = noticeHandlers.get(noticeType);
        if (null == handler) {
            // TODO Auto-generated method stub
        } else {
            Response<?> resp = handler.handle(headers, notification.getBody(),
                config.getServerDataParseErrorTips());
            if (null != resp) {
                Headers ackHeaders = new Headers();
                ackHeaders.set(Headers.HEADER_NOTICE_TYPE, headers.getAckNoticeType());
                resp.setHeaders(ackHeaders.getHeaders());
                resp.setBody(JSON.toJSONString(resp.getResult()));
                resp.setResult(null);
                sendMessage(resp);
            }
        }
    }

    public void registerNoticeHandler(NoticeHandler<?, ?> handler) {
        noticeHandlers.put(handler.getNoticeType(), handler);
    }

    
    public void registerRequestHandler(String requestType, RequestHandler handler) {
        requestHandlers.put(requestType, handler);
    }
    
    private void handleAcknowledgement(Message inMessage) {
        Headers headers = inMessage.headerWapper();
        String requestType = null;
        String requestId = null;
        try{
            requestType = headers.getRequestType();
            requestId = headers.getRequestId();
        }catch (HeaderNotFoundException e){
            return;
        }
        
        if (!StringUtils.isNullOrEmpty(requestType)){
            RequestHandler handler = requestHandlers.get(requestType);
            if (handler != null && !StringUtils.isNullOrEmpty(requestId)) {
                handler.onSuccess(requestId);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public void subscribe(NoticeHandler patchListHandler) {
        registerNoticeHandler(patchListHandler);
    }

    public void addConnectionCallback(ConnectionCallback callback) {
        if (callbacks == null) {
            return;
        }
        callbacks.add(callback);
    }

    public void removeConnectionCallback(ConnectionCallback callback) {
        if (callbacks == null) {
            return;
        }
        callbacks.remove(callback);
    }

    public void removeAllConnectionCallback() {
        if (callbacks == null) {
            return;
        }
        callbacks.removeAll(callbacks);
        callbacks = null;
    }

    public boolean sendMessage(Message message) {
        return connectMgr.sendMessage(message);
    }

    public void sendNotice(String noticeType, Object data) {
        Message message = Message.create().messageType(MessageType.NOTIFICATION).header(
            Headers.HEADER_NOTICE_TYPE, noticeType).json(data).build();
        sendMessage(message);
    }
    
    public void sendNotice(String noticeType, String requestType, String requestId, Object data) {
        Message message = Message.create().messageType(MessageType.NOTIFICATION).header(Headers.HEADER_NOTICE_TYPE, noticeType)
                .header(Headers.HEADER_REQUEST_TYPE, requestType).header(Headers.HEADER_REQUEST_ID, requestId).json(data).build();
        sendMessage(message);
    }

    public void close() {
        removeAllConnectionCallback();
        connectMgr.close();
        httpClient.close();
    }

    public void registerClient() {
        Message message = Message.create().boardNumber(config.getBoardNumber()).messageType(
            MessageType.REGISTER).tags(config.getTags()).build();
        sendMessage(message);
    }

    public <T> T requestByGet(Message message, Class<T> clazz) {
        return requestByMethod(message, 3000, "GET", clazz);
    }

    public <T> T requestByGet(Message message, int timeout, Class<T> clazz) {
        return requestByMethod(message, 3000, "GET", clazz);
    }

    public <T> T requestByPost_null(Message message, Class<T> clazz) {
        return requestByMethod(message, 50000, "POST", clazz);
    }

    public <T> T requestByPost_null(Message message, int timeout, Class<T> clazz) {
        return requestByMethod(message, timeout, "POST", clazz);
    }

    private <T> T requestByMethod(Message message, int timeout, String method, Class<T> clazz) {
        if (httpUri == null) {
            Log.v(TAG, "httpUri is null, please set it ");
            return null;
        }
        AsyncHttpRequest request = new AsyncHttpRequest(httpUri, method);
        request.setBody(new StringBody(Message.toRawString(message)));
        request.setTimeout(timeout);
        AsyncHttpClient httpClient = AsyncHttpClient.getDefaultInstance();
        try {
            String responseText = httpClient.executeString(request, nopStringCallback).get();
            Log.v(TAG, responseText);
            Response<?> response = Response.parse(responseText);
            return JSON.parseObject(response.getBody(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T requestByPost(Message message, Class<T> clazz) {
        return requestByPost(message, 50000, clazz);
    }

    public <T> T requestByPost(Message message, int timeout, Class<T> clazz) {
        if (httpUri == null) {
            Log.v(TAG, "httpUri is null, please set it ");
            return null;
        }

        try {
            HttpPost post = new HttpPost(config.getHttpUrl());
            post.setEntity(new StringEntity(Message.toRawString(message), "utf-8"));
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseText = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                Log.v(TAG, responseText);
                Response<?> response = Response.parse(responseText);
                return JSON.parseObject(response.getBody(), clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class NOPStringCallback extends StringCallback {
        @Override
        public void onCompleted(Exception ex, AsyncHttpResponse resp, String result) {

        }
    }

    @Override
    public int onHeartbeat() {
        if (httpUri == null) {
            return ConnectManager.HEARTBEAT_STATE.UNSUPPORT;
        }
        Message message = Message.create().boardNumber(config.getBoardNumber()).messageType(
            MessageType.PING).build();
        if (requestByPost(message, 5000, Date.class) != null) {
            return ConnectManager.HEARTBEAT_STATE.SUCCESS;
        } else {
            return ConnectManager.HEARTBEAT_STATE.FAIL;
        }
    }

    public Config getConfig() {
        return this.config;
    }

    public boolean getConnectState() {
        if (connectMgr == null) {
            return false;
        }
        return connectMgr.connected();
    }

    private AndroidHttpClient  createHttpClient() {
        String userAgent = "Dalvik/1.6.0 (Linux; u; Android 4.2.2; sdk Build/JB_MR1.1)";
        AndroidHttpClient  httpClient = AndroidHttpClient.newInstance(userAgent);
        return httpClient;
    }
}
