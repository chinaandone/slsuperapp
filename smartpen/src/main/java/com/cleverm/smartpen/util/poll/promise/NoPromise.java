package com.cleverm.smartpen.util.poll.promise;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  下午 06:02
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class NoPromise implements Promise{

    public NoPromise(){

    }



    @Override
    public Promise compare(CompareCallback callback) {




        return this;
    }

    @Override
    public Promise sort(SortCallback callback) {
        return this;
    }

    @Override
    public Promise intercept(InterceptCallback callback) {
        return this;
    }
}
