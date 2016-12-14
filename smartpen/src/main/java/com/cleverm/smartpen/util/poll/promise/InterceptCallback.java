package com.cleverm.smartpen.util.poll.promise;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  下午 05:39
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public interface InterceptCallback<T> {

    void onSucess(T result);

    void onFail(Exception exception);
}
