package com.cleverm.smartpen.util.cache;

/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  下午 03:47
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FileCacheCreateAleadyExistException  extends Exception {

    private String message;

    public FileCacheCreateAleadyExistException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
