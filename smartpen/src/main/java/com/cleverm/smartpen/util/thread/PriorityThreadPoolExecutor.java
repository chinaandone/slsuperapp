package com.cleverm.smartpen.util.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  上午 11:15
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * for big num tasks
     * @param corePoolSize
     * @param threadFactory
     */
    public PriorityThreadPoolExecutor(int corePoolSize,  ThreadFactory threadFactory) {
        super(corePoolSize, corePoolSize * 2 + 1, 1, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(128), threadFactory);
    }

    /**
     * for small & Immediate tasks
     * @param corePoolSize
     * @param queueSize
     * @param threadFactory
     */
    public PriorityThreadPoolExecutor(int corePoolSize, int queueSize,ThreadFactory threadFactory) {
        super(corePoolSize, corePoolSize, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(queueSize), threadFactory);
    }

    @Override
    public Future<?> submit(Runnable task) {
        PriorityFutureTask futureTask = new PriorityFutureTask((PriorityRunnable) task);
        execute(futureTask);
        return futureTask;
    }

    private static final class PriorityFutureTask extends FutureTask<PriorityRunnable> implements Comparable<PriorityFutureTask>{

        private final PriorityRunnable priorityRunnable;

        public PriorityFutureTask(PriorityRunnable priorityRunnable) {
            super(priorityRunnable, null);
            this.priorityRunnable = priorityRunnable;
        }

        @Override
        public int compareTo(PriorityFutureTask  other) {
            Priority p1 = priorityRunnable.getPriority();
            Priority p2 = other.priorityRunnable.getPriority();
            return p2.ordinal() - p1.ordinal();
        }
    }



}
