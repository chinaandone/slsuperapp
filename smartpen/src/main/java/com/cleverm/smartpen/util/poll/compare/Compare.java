package com.cleverm.smartpen.util.poll.compare;

import org.json.JSONException;

/**
 * Created by xiong,An android project Engineer,on 7/9/2016.
 * Data:7/9/2016  下午 03:56
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public interface Compare<T1,T2,T3> {

    T1 valueLeft(String condition) throws Exception;


    T2 valueRight(String condition);

    T3 compare(T1 t1, T2 t2);


    void after();

}
