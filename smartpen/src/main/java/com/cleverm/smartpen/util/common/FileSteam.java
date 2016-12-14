package com.cleverm.smartpen.util.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by xiong,An android project Engineer,on 25/8/2016.
 * Data:25/8/2016  上午 10:24
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public enum FileSteam {

    INSTANCE;

    public final boolean write(File file, String text) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, true);//true表示追加文件
            writer.write(text);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public final String read(File file) {
        String result = "";
        try {
            java.io.File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.length() <= 0) {
                return null;
            }
            if (!file.exists() || file.isDirectory()) ;
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result = result + "\n" + s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * copy by NIO steam
     * @param source
     * @param dest
     * @param overlay 是否覆盖
     * @return
     */
    public final boolean copyByNIO(File source, File dest, Boolean overlay) {
        if (!source.exists()) {
            return false;
        } else if (!source.isFile()) {
            return false;
        }
        if (dest.exists()) {// 判断目标文件是否存在
            if (overlay) {// 如果目标文件存在并允许覆盖
                dest.delete();// 删除已经存在的目标文件，无论目标文件是目录还是单个文件
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!dest.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!dest.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }
        FileInputStream fis;
        FileOutputStream fos;
        FileChannel sourceCh = null;
        FileChannel destCh = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(dest);
            sourceCh = fis.getChannel();
            destCh = fos.getChannel();
            MappedByteBuffer mbb = sourceCh.map(FileChannel.MapMode.READ_ONLY, 0, sourceCh.size());
            destCh.write(mbb);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (sourceCh != null)
                    sourceCh.close();
                if (destCh != null)
                    destCh.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
