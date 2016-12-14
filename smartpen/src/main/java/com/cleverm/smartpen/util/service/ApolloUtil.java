package com.cleverm.smartpen.util.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cleverm.apollo.communication.socket.SocketConnection;
import com.cleverm.smartpen.service.ApolloService;
import com.cleverm.smartpen.util.QuickUtils;

/**
 * Created by xiong,An android project Engineer,on 17/5/2016.
 * Data:17/5/2016  上午 10:47
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class ApolloUtil {

    private ApolloService mApolloService;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mApolloService = ((ApolloService.ApolloServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static ApolloUtil INSTANCE = new ApolloUtil();

    private ApolloUtil(){

    }

    public static ApolloUtil getInstance(){
        return INSTANCE;
    }

    public void bindService(Context context){
        context.bindService(new Intent(context, ApolloService.class), mConn, context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context){
        context.unbindService(mConn);
    }

    private ApolloService getApolloServcie(){
        return mApolloService;
    }

    public SocketConnection getApolloServiceConnection(){
        QuickUtils.log("ApolloServcie = " + getApolloServcie());
        if(getApolloServcie()!=null){
            return getApolloServcie().getApolloConnection();
        }
        return null;
    }

}
