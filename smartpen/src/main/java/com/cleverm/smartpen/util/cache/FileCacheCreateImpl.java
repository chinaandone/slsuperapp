package com.cleverm.smartpen.util.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  上午 12:06
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FileCacheCreateImpl implements FileCacheCreator {

    /**
     * 真真业务逻辑操作对象
     */
    private FileCacheStore cacheStore;

    public FileCacheCreateImpl(File cacheDir,int maxKBSizes){
        long maxBytesSize = FileCacheUtil.getMaxBytesSize(maxKBSizes);
        cacheStore = new FileCacheStore(cacheDir,maxBytesSize);
    }


    @Override
    public FileEntry get(String key) {
        File file = cacheStore.get(FileCacheUtil.keyToFileName(key));
        if(file==null){
            return null;
        }
        if(file.exists()){
            return new FileEntry(key,file);
        }
        return null;
    }

    @Override
    public void put(String key, ByteProvider provider) throws IOException {
        cacheStore.write(FileCacheUtil.keyToFileName(key),provider);
    }

    @Override
    public void put(String key, InputStream inputStream) throws IOException {
        put(key,FileCacheUtil.create(inputStream));
    }

    @Override
    public void put(String key, File sourceFile, boolean move) throws IOException {
        if(move){
            cacheStore.move(FileCacheUtil.keyToFileName(key),sourceFile);
        }else{
            put(key,FileCacheUtil.create(sourceFile));
        }
    }

    @Override
    public void remove(String key) {
        cacheStore.delete(FileCacheUtil.keyToFileName(key));
    }

    @Override
    public void clear() {
        cacheStore.deleteAll();
    }
}
