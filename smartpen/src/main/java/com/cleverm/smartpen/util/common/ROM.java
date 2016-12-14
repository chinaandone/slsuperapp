package com.cleverm.smartpen.util.common;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  上午 10:47
 * Base on clever-m.com(JAVA Service)
 * Describe: 硬盘
 * Version:1.0
 * Open source
 */
public enum ROM {
    INSTANCE;

    public final String information() {
        return String.valueOf(Math.round(RAM.INSTANCE.convertToMb(availROM()))) + "MB" + "/" + String.valueOf(Math.round(RAM.INSTANCE.convertToMb(totalROM())) + "MB");
    }

    public final long availROM() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        long availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    /**
     * 内部存储
     * @return
     */
    public final long totalROM() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        long totalBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        } else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
        }
        return totalBlocks * blockSize;
    }

    /**
     * 是否有外部存储
     *
     * @return
     */
    private boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 外部存储 - SD
     * @return
     */
    public final long availExternalROM() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize;
            long availableBlocks;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks();
            }
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    public final long totalExternalROM() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize;
            long totalBlocks;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
            } else {
                blockSize = stat.getBlockSize();
                totalBlocks = stat.getBlockCount();
            }
            return totalBlocks * blockSize;
        } else {
            return 0;
        }
    }


}
