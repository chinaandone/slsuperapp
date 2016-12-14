package com.cleverm.smartpen.util.execption;

/**
 * Created by xiong,An android project Engineer,on 22/4/2016.
 * Data:22/4/2016  下午 01:56
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class TriggerTimerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private TriggerTimerException(String message) {
        super(message);
    }

    public static void throwTriggerStartingTimerException() {
        throw new TriggerTimerException("Timer is started already");
    }

    public static void throwTriggerStoppingTimerException() {
        throw new TriggerTimerException("Timer is stopped already or haven't started yet");
    }
}