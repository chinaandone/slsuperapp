package com.cleverm.smartpen.rabbitmq;

import android.util.Log;

import com.cleverm.smartpen.bean.event.OnToastEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by regis on 2017/4/28.
 */

public class RabbitMQRunnable implements Runnable {
    private String TAG = "Runnable";
    private RabbitMQProductor rabbitMQProductor;
    private String mqMessage;
    public RabbitMQRunnable(RabbitMQProductor rabbitMQProductor) {
        this.rabbitMQProductor = rabbitMQProductor;
    }

    public RabbitMQRunnable(RabbitMQProductor rabbitMQProductor,String mqMessage){
        this.rabbitMQProductor = rabbitMQProductor;
        this.mqMessage = mqMessage;
    }

    public void setMqMessage(String mqMessage) {
        this.mqMessage = mqMessage;
    }

    @Override
    public void run() {
        try {
            rabbitMQProductor.initMQ();
        } catch (IOException e) {
            e.printStackTrace();
            OnToastEvent onToastEvent = new OnToastEvent("mq server connect ioexception");
            EventBus.getDefault().post(onToastEvent);
            return;
        } catch (TimeoutException e) {
            e.printStackTrace();
            Log.v(TAG,"connect time out in runnable");
            OnToastEvent onToastEvent = new OnToastEvent("mq server connect time out in runnable");
            EventBus.getDefault().post(onToastEvent);
            return;
        }
        rabbitMQProductor.sendMessage(mqMessage);
        rabbitMQProductor.stopMQ();
    }
}
