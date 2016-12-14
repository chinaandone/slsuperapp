package com.cleverm.smartpen.util.cache;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  上午 12:09
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FileCacheStore {

    private static final String TAG = "FileCacheStore";

    private File cacheDir;
    private long maxBytesSize;
    private Map<String, CacheFile> cacheFileMap;//LinkedHashMap有序


    /**
     * 使用ReadWriteLock 实现加锁功能
     */
    private ReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock readLock = rwl.readLock();//读锁
    private Lock writeLock = rwl.writeLock();//写锁

    private AtomicLong currentBytesSize = new AtomicLong();//用于判断是否map内存容量越界;

    /**
     * 指定File存储路径和存储最大字节
     *
     * @param cacheDir
     * @param maxBytesSize
     */
    public FileCacheStore(File cacheDir, long maxBytesSize) {
        this.cacheDir = cacheDir;
        this.maxBytesSize = maxBytesSize;
        cacheFileMap = Collections.synchronizedMap(new LinkedHashMap<String, CacheFile>(1024));
        FileCacheUtil.createDirIfNotExists(cacheDir);
        runThread();
    }

    private void runThread() {
        new Thread(new InitializerRunnable()).start();
    }

    public File get(String key) {
        readLock.lock();
        try {
            //从map中取出File文件
            CacheFile cacheFile = cacheFileMap.get(key);
            if (cacheFile == null) {
                return null;
            }
            //如果file文件存在，就返回它
            if (cacheFile.file.exists()) {
                //取过一次的file我们将它放到map集合的尾部
                moveHitEntryToFirst(key, cacheFile);
                return cacheFile.file;
            }
            removeCacheFileFromMap(key, cacheFile);
            return null;
        }  finally {
            readLock.unlock();
        }
    }


    public void write(String key,  ByteProvider provider) throws IOException {
        writeLock.lock();
        try {
            FileCacheUtil.createDirIfNotExists(cacheDir);
            File file = FileCacheUtil.createFile(cacheDir, key);
            copyProviderToFile(provider, file);
            putToCachMapAndCheckMaxThresold(file);
        } finally {
            writeLock.unlock();
        }
    }

    public void move(String key,File sourceFile) {
        writeLock.lock();
        try {
            FileCacheUtil.createDirIfNotExists(cacheDir);
            File file = FileCacheUtil.createFile(cacheDir, key);
            sourceFile.renameTo(file);
            putToCachMapAndCheckMaxThresold(file);
        } finally {
            writeLock.unlock();
        }
    }

    private void copyProviderToFile(ByteProvider provider, File file) throws IOException {
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            provider.writeTo(os);
        } finally {
            IOOperation.close(os);
        }
    }


    public void delete(String key) {
        writeLock.lock();
        try {
            CacheFile cacheFile = cacheFileMap.get(key);
            if (cacheFile == null) {
                return;
            }
            removeCacheFileFromMap(key, cacheFile);
            cacheFile.file.delete();
        } finally {
            writeLock.unlock();
        }
    }

    public void deleteAll() {
        writeLock.lock();
        try {
            ArrayList<String> keys = new ArrayList<String>(cacheFileMap.keySet());
            for (String key : keys) {
                delete(key);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 一初始化就将所有的file放入内存中
     */
    private class InitializerRunnable implements Runnable {

        @Override
        public void run() {
            writeLock.lock();//写锁挂住
            try {
                //列出file目录下的所有文件
                File[] cachedFiles = cacheDir.listFiles();
                for (File file : cachedFiles) {
                    //将file文件放入map集合中
                    putFileToCacheMap(file);
                }
            } catch (Exception ex) {
                Log.e(TAG, "FileCacheStor.Initializer: fail to initialize - "
                        + ex.getMessage(), ex);
            } finally {
                writeLock.unlock();//写锁释放
            }
        }
    }


    private void putFileToCacheMap(File file) {
        cacheFileMap.put(file.getName(), new CacheFile(file));
        currentBytesSize.addAndGet(file.length());
    }

    private void moveHitEntryToFirst(String filename, CacheFile cachedFile) {
        //从内存中删除该file
        cacheFileMap.remove(filename);
        //然后再次添加该file到内存
        cacheFileMap.put(filename, cachedFile);
    }

    private void removeCacheFileFromMap(String filename, CacheFile cachedFile) {
        currentBytesSize.addAndGet(-cachedFile.size);
        cacheFileMap.remove(filename);
    }

    private void putToCachMapAndCheckMaxThresold(File file) {
        putFileToCacheMap(file);
        checkMaxThresoldAndDeleteOldestWhenOverflow();
    }

    private void checkMaxThresoldAndDeleteOldestWhenOverflow() {
        if (isOverflow()) {
            List<Map.Entry<String, CacheFile>> deletingCandidates = getDeletingCandidates();
            for (Map.Entry<String, CacheFile> entry : deletingCandidates) {
                delete(entry.getKey());
            }
        }
    }

    /**
     * 超过了内存指定的最大大小
     * @return
     */
    private boolean isOverflow() {
        if (maxBytesSize <= 0) {
            return false;
        }
        return currentBytesSize.get() > maxBytesSize;
    }

    private List<Map.Entry<String, CacheFile>> getDeletingCandidates() {
        List<Map.Entry<String, CacheFile>> deletingCandidates = new ArrayList<Map.Entry<String, CacheFile>>();
        long cadidateFileSizes = 0;
        for (Map.Entry<String, CacheFile> entry : cacheFileMap.entrySet()) {
            deletingCandidates.add(entry);
            cadidateFileSizes += entry.getValue().file.length();
            if (currentBytesSize.get() - cadidateFileSizes < maxBytesSize) {
                break;
            }
        }
        return deletingCandidates;
    }


}
