package com.cleverm.smartpen.util.thread;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  上午 11:12
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class PriorityRunnable implements Runnable {
    private final Priority priority;

    public PriorityRunnable(Priority priority) {
        this.priority = priority;
    }

    @Override
    public void run() {
        // nothing to do here.
    }

    public Priority getPriority() {
        return priority;
    }
}
