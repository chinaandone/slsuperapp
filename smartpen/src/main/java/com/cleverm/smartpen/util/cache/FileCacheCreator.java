package com.cleverm.smartpen.util.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiong,An android project Engineer,on 29/3/2016.
 * Data:29/3/2016  下午 11:57
 * Base on clever-m.com(JAVA Service)
 * Describe: 是一个目录中多个file的对象表现体
 * Version:1.0
 * Open source
 */
public interface FileCacheCreator {

    /**
     * 根据key去拿File文件
     *
     * @param key
     * @return
     */
    FileEntry get(String key);

    /**
     * 存放File文件
     * @param key
     * @param provider
     * @throws IOException
     */
    void put(String key, ByteProvider provider) throws IOException;

    void put(String key, InputStream inputStream) throws IOException;

    void put(String key, File sourceFile, boolean move) throws IOException;

    /**
     * 根据key移除File文件
     * @param key
     */
    void remove(String key);


    /**
     * 清空File存储
     */
    void clear();


}
