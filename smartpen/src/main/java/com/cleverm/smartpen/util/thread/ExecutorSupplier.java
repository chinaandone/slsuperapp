package com.cleverm.smartpen.util.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  上午 11:08
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public interface ExecutorSupplier {

    /*
   * returns the thread pool executor for background task
   */
    ThreadPoolExecutor backgroundTasks();

    /*
    * returns the thread pool executor for light weight background task
    */
    ThreadPoolExecutor lightWeightBackgroundTasks();

    /*
    * returns the thread pool executor for main thread task
    */
    Executor mainThreadTasks();
}
