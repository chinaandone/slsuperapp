package com.cleverm.smartpen.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cleverm.apollo.communication.common.Connection;
import com.cleverm.apollo.communication.common.ConnectionCallback;
import com.cleverm.apollo.communication.common.ConnectionInfo;
import com.cleverm.apollo.communication.common.ServerEventListener;

import com.cleverm.apollo.communication.protocal.parser.data.RawParser;
import com.cleverm.apollo.communication.protocal.parser.frame.RawFrame;
import com.cleverm.apollo.communication.socket.SocketConnection;
import com.cleverm.apollo.communication.socket.server.SocketServer;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiong,An android project Engineer,on 17/5/2016.
 * Data:17/5/2016  上午 10:32
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class ApolloService extends Service implements ServerEventListener, ConnectionCallback<RawParser> {

    private SocketServer socketServer;
    private SocketConnection<RawParser> mConnection;
    private List<String> ipClien;


    @Override
    public void onCreate() {
        super.onCreate();
        if (ipClien == null) {
            ipClien = new ArrayList<String>();
        }
        if (socketServer == null) {
            socketServer = new SocketServer(30303, this, RawParser.class, new RawFrame());
        }
        if (socketServer != null) {
            socketServer.start();
        }
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socketServer != null) {
            socketServer.stop();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ApolloServiceBinder();
    }

    public class ApolloServiceBinder extends Binder {
        public ApolloService getService() {
            return ApolloService.this;
        }
    }

    @Override
    public void onNewConnection(Connection connection) {
        if (mConnection != null) {
            mConnection.registerCallback(null);
            mConnection.close();
            return;
        }

        if (connection instanceof SocketConnection) {
            mConnection = (SocketConnection<RawParser>) connection;
            mConnection.registerCallback(this);
            mConnection.open();
            final String msg = mConnection.getInfo().tag;
            Log.e("接收到的数据", msg + "------------------");
            if (!ipClien.contains(msg)) {
                ipClien.add(msg);
            }
        }

    }

    public SocketConnection getApolloConnection(){
        if(mConnection!=null){
            return mConnection;
        }
        return null;
    }


    @Override
    public void onRawMessage(byte[] bytes) {

    }

    @Override
    public void onMessage(ConnectionInfo connectionInfo, RawParser rawParser) {

    }

    @Override
    public void onConnected(ConnectionInfo connectionInfo) {

    }

    @Override
    public void onDisconnected(ConnectionInfo connectionInfo, int i) {

    }
}
