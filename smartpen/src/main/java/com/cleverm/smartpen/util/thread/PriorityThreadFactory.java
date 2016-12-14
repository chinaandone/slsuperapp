package com.cleverm.smartpen.util.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  上午 11:06
 * Base on clever-m.com(JAVA Service)
 * Describe:在系统的DefaultThreadFactory上 添加了优先级
 * Version:1.0
 * Open source
 */
public class PriorityThreadFactory implements ThreadFactory {

    private final int mThreadPriority;

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public PriorityThreadFactory(int priority) {
        this.mThreadPriority = priority;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(final Runnable r) {
        Runnable wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    android.os.Process.setThreadPriority(mThreadPriority);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                r.run();
            }
        };
        return new Thread(group, wrapperRunnable, namePrefix + threadNumber.getAndIncrement());
    }
}
