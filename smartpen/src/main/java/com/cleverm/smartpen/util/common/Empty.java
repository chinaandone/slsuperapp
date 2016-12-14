package com.cleverm.smartpen.util.common;

/**
 * Created by xiong,An android project Engineer,on 5/9/2016.
 * Data:5/9/2016  上午 11:54
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public enum Empty {

    INSTANCE;

    public interface Callback {
        void onDoing();
    }


    /**
     * 先判断对象非空，如果为空则不执行子条件，如果不为空，则执行对象的方法(子条件)
     * @param t
     * @param callback
     * @param <T>
     */
    public <T> void checkNull(T t, Callback callback) {
        if (t == null) {
            return;
        } else {
            callback.onDoing();
        }
    }


}
