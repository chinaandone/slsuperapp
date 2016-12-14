package com.cleverm.smartpen.util.poll.promise;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  下午 03:31
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public interface Promise {


    Promise compare(CompareCallback callback);

    Promise sort(SortCallback callback);

    Promise intercept(InterceptCallback callback);



}
