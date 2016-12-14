package com.cleverm.smartpen.util.cache;

import java.io.File;

/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  上午 10:14
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class CacheFile {

    public File file;

    public long size;

    public CacheFile(File file) {
        super();
        this.file = file;
        this.size = file.length();
    }
}
