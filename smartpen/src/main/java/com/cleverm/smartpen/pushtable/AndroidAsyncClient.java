package com.cleverm.smartpen.pushtable;

import android.util.Log;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.net.URI;
import java.util.concurrent.Future;

public class AndroidAsyncClient implements WebSocketClient,
    AsyncHttpClient.WebSocketConnectCallback, WebSocket.StringCallback,
        CompletedCallback {

    private static String TAG = "AndroidAsyncClient";
    private WebSocket webSocket;
    private Future<WebSocket> webSocketFuture;
    private URI mURI;
    private WebSocketListener mListener;

    public AndroidAsyncClient(URI uri, WebSocketListener listener) {
        mURI = uri;
        mListener = listener;
    }

    public void connect() {
        Log.v(TAG, "AndroidAsyncClient connect, webSocket null or not open");
        webSocketFuture = AsyncHttpClient.getDefaultInstance().websocket(
            mURI.toString(), null, AndroidAsyncClient.this);
        if (webSocket != null) {
            webSocket.setStringCallback(null);
            webSocket.setClosedCallback(null);
        }
    }

    @Override
    public boolean isDisconnected() {
        return !(webSocketFuture != null && !webSocketFuture.isDone())
               && !(webSocket != null && webSocket.isOpen());
    }

    @Override
    public boolean send(String data) {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(data);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCompleted(Exception ex, WebSocket ws) {
        webSocket = ws;
        Log.i(TAG, "websocket onCompleted " + webSocket);
        if (ex != null) {
            Log.d(TAG, "onCompleted error", ex);
            mListener.onDisconnect(ex.getMessage(), ex);
            return;
        } else {
            mListener.onConnect();
            webSocket.setStringCallback(this);
            webSocket.setClosedCallback(this);
        }
    }

    @Override
    public void onStringAvailable(String s) {
        //���͵�������Ϣ���Ǵ���������
        Log.i(TAG, "onStringAvailable " + s);
        mListener.onMessage(s);
    }

    @Override
    public void onCompleted(Exception ex) {
        if (webSocket != null) {
            Log.e(TAG, "closed callback ", ex);
            close();
        }
    }

    @Override
    public void close() {
        try {
            if (webSocket != null) {
                webSocket.setStringCallback(null);
                webSocket.setClosedCallback(null);
                webSocket.close();
                webSocket = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "close websocket exception ", e);
        }

    }
}