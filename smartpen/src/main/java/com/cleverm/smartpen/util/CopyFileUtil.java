package com.cleverm.smartpen.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by xiong,An android project Engineer,on 2016/3/20.
 * Data:2016-03-20  22:14
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class CopyFileUtil {
    private static String MESSAGE = "";

    /**
     * 复制单个文件
     *
     * @param srcFileName
     *            待复制的文件名
     * @param destFileName
     *            目标文件名
     * @param overlay
     *            如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String destFileName,
                                   boolean overlay) {

        long start = System.currentTimeMillis();

        Log.e("copyFile","srcFileName="+srcFileName+"/destFileName="+destFileName);

        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            MESSAGE = "源文件：" + srcFileName + "不存在！";
            Log.e("copyFile",MESSAGE);
            return false;
        } else if (!srcFile.isFile()) {
            MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";
            Log.e("copyFile", MESSAGE);
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                long end = System.currentTimeMillis();
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                Log.e("copyFile","copy耗时"+(end-start));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * NIO的效率
     * srcFileName=/storage/emulated/0/muyevideo/150.mp4/destFileName=/storage/emulated/0/muyepaly/150.mp4
     * 传统IO：copy耗时6283  NIO: copy耗时449
     * srcFileName=/storage/emulated/0/muyevideo/156.mp4/destFileName=/storage/emulated/0/muyepaly/156.mp4
     * 传统IO: copy耗时1621 NIO： copy耗时274
     * @param sourcePath
     * @param destPath
     * @param overlay
     * @return
     */
    public static boolean copyByNIO(String sourcePath, String destPath,Boolean overlay) {

        long start = System.currentTimeMillis();

        QuickUtils.log("copyFile："+"srcFileName="+sourcePath+"/destFileName="+destPath);

        File source = new File(sourcePath);

        // 判断源文件是否存在
        if (!source.exists()) {
            MESSAGE = "源文件：" + sourcePath + "不存在！";
            QuickUtils.log("copyFile："+MESSAGE);
            return false;
        } else if (!source.isFile()) {
            MESSAGE = "复制文件失败，源文件：" + sourcePath + "不是一个文件！";
            QuickUtils.log("copyFile：" + MESSAGE);
            return false;
        }


        // 判断目标文件是否存在
        File dest = new File(destPath);
        if (dest.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destPath).delete();
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
        FileChannel sourceCh=null;
        FileChannel destCh =null;
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
        }finally {
            try {
                long end = System.currentTimeMillis();
                if (sourceCh != null)
                    sourceCh.close();
                if (destCh != null)
                    destCh.close();
                QuickUtils.log("copyFile：" + "copy耗时" + (end - start));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
