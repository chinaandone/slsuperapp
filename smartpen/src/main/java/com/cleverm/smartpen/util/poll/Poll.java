package com.cleverm.smartpen.util.poll;

import com.cleverm.smartpen.util.poll.download.Download;
import com.cleverm.smartpen.util.poll.promise.NoPromise;
import com.cleverm.smartpen.util.poll.promise.Promise;
import com.cleverm.smartpen.util.poll.property.Properties;
import com.cleverm.smartpen.util.poll.sort.Sort;
import com.koushikdutta.async.http.callback.RequestCallback;

/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  下午 03:10
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class Poll {

   /* public Download mDownload;

    public Intercept mIntercept;

    public Sort mSort;


    public Poll(Download download,Intercept intercept,Sort sort){
        this.mDownload=download;
        this.mIntercept=intercept;
        this.mSort=sort;
    }


    public interface RequestCallback<T,V>{

        T before(Properties properties);

        V after(Properties properties);

    }*/



   /* *//**
     * 进行Properties文件配置清单更新
     *
     * 更新完成后才开始去进行下载
     *//*
    public <T,V> Promise sumbit(RequestCallback<T,V> callback){
        Properties properties = new Properties();
        T before = callback.before(properties);//配置本地Properties
        V after = callback.after(properties);//配置云端Properties
        return new NoPromise();

    }
*/





}
