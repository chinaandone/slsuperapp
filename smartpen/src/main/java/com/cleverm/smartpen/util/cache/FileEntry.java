package com.cleverm.smartpen.util.cache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiong,An android project Engineer,on 29/3/2016.
 * Data:29/3/2016  下午 11:58
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FileEntry {
    private String key;
    private File file;
    public FileEntry(String key, File file) {
        this.key = key;
        this.file = file;
    }
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public String getKey() {
        return key;
    }

    public File getFile() {
        return file;
    }
}
