package com.cleverm.smartpen.rabbitmq;

import android.util.Log;

import com.cleverm.smartpen.bean.event.OnToastEvent;
import com.cleverm.smartpen.util.RememberUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * Created by regis on 2017/4/28.
 */

public class RabbitMQProductor {
    private String TAG = "RabbitMQPorductor";
    private String serverHost=null;
    private String[] watchID=null;
    private String SERVERHOSTKEY = "Dongleadd";
    private String WATCHIDKEY = "Watchadd";
    private String MQUSER = "supermanmq";
    private String MQPWD = "supermanmq";

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public RabbitMQProductor(){
        String watchid;
        serverHost = RememberUtil.getString(SERVERHOSTKEY,null);
        watchid = RememberUtil.getString(WATCHIDKEY,null);
        if(watchid!=null){
            watchID = watchid.split(",");
        }
        factory = new ConnectionFactory();
    }
    public RabbitMQProductor(String serverHost,String[] watchID){
        this.serverHost = serverHost;
        this.watchID = watchID;
        factory = new ConnectionFactory();
    }

    public void initMQ() throws IOException, TimeoutException {

        factory.setHost(serverHost);
        factory.setUsername(MQUSER);
        factory.setPassword(MQPWD);

        connection = factory.newConnection();
        channel = connection.createChannel();

    }

    public void sendMessage(String message){
        sendMQs(watchID,message);
    }

    public void sendMQs(String[] watchids,String message){
        for(int i=0;i<watchids.length;i++){
            sendMQ(watchids[i],message);
        }
    }

    public void sendMQ(String QUEUE_NAME,String message ){
        if(channel!=null) {
            try {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(TAG,"msg publish ioexception in Productor");
                OnToastEvent onToastEvent = new OnToastEvent("mq msg publish ioexception in Productor");
                EventBus.getDefault().post(onToastEvent);
            }
        }
    }

    public void stopMQ(){
        try {
            if (channel != null) {
                channel.close();
            }
            if(connection !=null){
                connection.close();
            }
        }catch(IOException e){
            e.printStackTrace();
            OnToastEvent onToastEvent = new OnToastEvent("mq channel or connection ioexception in Productor");
            EventBus.getDefault().post(onToastEvent);
        }catch(TimeoutException e){
            e.printStackTrace();
            OnToastEvent onToastEvent = new OnToastEvent("mq channel or connection timeoutexception in Productor");
            EventBus.getDefault().post(onToastEvent);
        }catch(Exception e){
            e.printStackTrace();
//            OnToastEvent onToastEvent = new OnToastEvent("您的呼叫已到达,请耐心等待");
//            EventBus.getDefault().post(onToastEvent);
            Log.v(TAG,".................................."+e.getMessage());
        }
    }
}
