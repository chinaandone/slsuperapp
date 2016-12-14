package com.cleverm.smartpen.util.execption;

/**
 * Created by xiong,An android project Engineer,on 22/4/2016.
 * Data:22/4/2016  下午 01:54
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class NullTimerHandlerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private NullTimerHandlerException() {
        super("Haven't set handler to AndroidTimer yet");
    }

    public static void throwException() {
        throw new NullTimerHandlerException();
    }
}