package com.cleverm.smartpen.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xiong,An android project Engineer,on 1/7/2016.
 * Data:1/7/2016  下午 06:08
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class ThreadManager {

    private static ThreadManager INSTANCE;
    private int HOW_MANY_THREADS = 15;
    ExecutorService executorService;

    private ThreadManager() {
        executorService = Executors.newFixedThreadPool(HOW_MANY_THREADS);
    }

    public static ThreadManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadManager();
        }
        return INSTANCE;
    }

    public void onDestory() {
        executorService.shutdown();
    }

    public void execute(Runnable runnable) {
        if (!executorService.isShutdown()) {
            executorService.execute(runnable);
        } else
            throw new IllegalStateException("Thread pool executor already destroyed");
    }
}
