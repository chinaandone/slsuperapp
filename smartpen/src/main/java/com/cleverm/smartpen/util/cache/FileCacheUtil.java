package com.cleverm.smartpen.util.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  上午 12:11
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FileCacheUtil {

    /**
     * KB转Bytes
     * @param maxKBSizes
     * @return
     */
    public static long getMaxBytesSize(int maxKBSizes) {
        return maxKBSizes <= 0 ? 0 : maxKBSizes * 1024;
    }

    /**
     * 对敏感的字符进行转化
     * @param key
     * @return
     */
    public static String keyToFileName(String key){
        String filename = key.replace(":", "_");
        filename = filename.replace("/", "_s_");
        filename = filename.replace("\\", "_bs_");
        filename = filename.replace("&", "_bs_");
        filename = filename.replace("*", "_start_");
        filename = filename.replace("?", "_q_");
        filename = filename.replace("|", "_or_");
        filename = filename.replace(">", "_gt_");
        filename = filename.replace("<", "_lt_");
        return filename;
    }


    public static ByteProvider create(final InputStream is){
        return new ByteProvider() {
            @Override
            public void writeTo(OutputStream os) throws IOException {
                IOOperation.write(is,os);
            }
        };
    }

    public static ByteProvider create(final File file){
        return new ByteProvider() {
            @Override
            public void writeTo(OutputStream os) throws IOException {
                IOOperation.write(file,os);
            }
        };
    }

    public static ByteProvider create(final String str){
        return new ByteProvider() {
            @Override
            public void writeTo(OutputStream os) throws IOException {
                IOOperation.write(str,os);
            }
        };
    }


    /**
     * 创建目录
     * @param dir
     */
    public static void createDirIfNotExists(File dir){
        if (dir.exists())
            return;
        dir.mkdirs();
    }

    /**
     * 创建文件
     * @param dir
     * @param filename
     * @return
     */
    public static File createFile(File dir,String filename) {
        return new File(dir, filename);
    }


}
