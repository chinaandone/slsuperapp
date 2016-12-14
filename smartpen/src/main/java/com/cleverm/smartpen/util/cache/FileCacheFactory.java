package com.cleverm.smartpen.util.cache;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  下午 03:30
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FileCacheFactory {

    private Context context;

    private File cacheBaseDir;

    private HashMap<String, FileCacheCreator> cacheMap = new HashMap<String, FileCacheCreator>();

    private static FileCacheFactory INSTANCE =new FileCacheFactory();

    private FileCacheFactory(){

    }

    public static FileCacheFactory getInstance(){
        return INSTANCE;
    }

    public  void init(Context context){
        //this.context= ContextManager.getContext();
        this.context= context;
        cacheBaseDir = context.getCacheDir();
        //cacheBaseDir = Environment.getExternalStorageDirectory().getAbsoluteFile();
    }

    public FileCacheCreator put(String cacheName, int maxKbSizes) throws FileCacheCreateAleadyExistException {
        //synchronized (cacheMap){
            FileCacheCreator fileCacheCreator = cacheMap.get(cacheName);
            if(fileCacheCreator!=null){
                throw new FileCacheCreateAleadyExistException(String.format("FileCacheCreator[%s] Aleady exists", cacheName));
            }
            File cacheDir = new File(cacheBaseDir, cacheName);
            fileCacheCreator=new FileCacheCreateImpl(cacheDir,maxKbSizes);
            cacheMap.put(cacheName,fileCacheCreator);
            return fileCacheCreator;
        //}
    }

    public FileCacheCreator get(String cacheName) throws FileCacheCreateNotFoundException {
        //synchronized (cacheMap) {
            FileCacheCreator cache = cacheMap.get(cacheName);
            if (cache == null) {
                throw new FileCacheCreateNotFoundException(String.format("FileCacheCreator[%s] not founds.", cacheName));
            }
            return cache;
        //}
    }

    /**
     * 如果目录已经有了,就会立即返回true。
     * 所以注意:如果你要在一个目录中存储多个文件，请把put操作放在一起，别分开
     * @param cacheName
     * @return
     */
    public boolean has(String cacheName) {
        return cacheMap.containsKey(cacheName);
    }



}
