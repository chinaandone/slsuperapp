package com.cleverm.smartpen.util.thread;

import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  上午 09:38
 * Base on clever-m.com(JAVA Service)
 * Describe:
 *
 * ThreadExecutorSupplier.
 *          getInstance().
 *          backgroundTasks().
 *          submit(new PriorityRunnable(Priority.HIGH){
 *                @Override
 *                public void run() {
 *                     EasyDeviceInfo.getInstance().TEST().doMethod();
 *                }
 *             });
 *
 *
 * Version:1.0
 * Open source
 */
public class ThreadExecutorSupplier implements ExecutorSupplier {

    /**
     * Cpu count
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private static final int IMMEDIATE_CORE_POOL_SIZE = 2;
    private static final int IMMEDIATE_WAITING_POOL_SIZE = 10;


    /**
     * for background task
     */
    private final PriorityThreadPoolExecutor mBackgroudPoolExecutor;

    /**
     * for light wight background task
     */
    private final ThreadPoolExecutor mLightWeightBackgroudPoolExecutor;


    /**
     * for main thread task
     */
    private final Executor mMainThreadExecutor;


    /**
     * single
     */
    private static ThreadExecutorSupplier sInstance;


    public static ThreadExecutorSupplier getInstance() {
        if (sInstance == null) {
            synchronized (ThreadExecutorSupplier.class) {
                sInstance = new ThreadExecutorSupplier();
            }
        }
        return sInstance;
    }


    private ThreadExecutorSupplier() {
        PriorityThreadFactory mThreadFactory = new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

        mBackgroudPoolExecutor = new PriorityThreadPoolExecutor(
                CORE_POOL_SIZE,
                mThreadFactory
        );

        mLightWeightBackgroudPoolExecutor = new PriorityThreadPoolExecutor(
                IMMEDIATE_CORE_POOL_SIZE, IMMEDIATE_WAITING_POOL_SIZE, mThreadFactory
        );

        mMainThreadExecutor = new MainThreadExecutor();

    }


    public ThreadPoolExecutor backgroundTasks() {
        return mBackgroudPoolExecutor;
    }


    public ThreadPoolExecutor lightWeightBackgroundTasks() {
        return mLightWeightBackgroudPoolExecutor;
    }


    public Executor mainThreadTasks() {
        return mMainThreadExecutor;
    }


}