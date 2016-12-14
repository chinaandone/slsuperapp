package com.cleverm.smartpen.util.poll.download;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xiong,An android project Engineer,on 8/9/2016.
 * Data:8/9/2016  上午 10:41
 * Base on clever-m.com(JAVA Service)
 * Describe: 队列下载
 * Version:1.0
 * Open source
 */
public class Queue<T> {

    public  BlockingQueue<T> init(){
        return new LinkedBlockingQueue<T>();
    }


    public void put(BlockingQueue queue,T value){
        queue.add(value);
    }

    public T poll(BlockingQueue queue){
        return (T) queue.poll();
    }


}
