package com.cleverm.smartpen.util.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  上午 09:40
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class IOOperation {


    /**
     * IO流读操作
     *
     * @param is
     * @throws Exception
     */
    public static String read(InputStream is) throws IOException {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(is);
            StringBuilder builder = new StringBuilder();
            char[] readDate = new char[1024];
            int len = -1;
            while ((len = reader.read(readDate)) != -1) {
                builder.append(readDate, 0, len);
            }
            return builder.toString();
        } finally {
            close(reader);
        }
    }

    public static void write(InputStream is, OutputStream out) throws IOException {
        byte[] buff = new byte[4096];
        int len = -1;
        while ((len = is.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
    }

    public static void write(File source, OutputStream os) throws IOException {
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(source));
            IOOperation.write(is, os);
        } finally {
            IOOperation.close(is);
        }
    }

    public static void write(InputStream is, File target) throws IOException {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(target));
            IOOperation.write(is, os);
        } finally {
            IOOperation.close(os);
        }

    }

    public static void write(String str, OutputStream os) throws IOException {
        os.write(str.getBytes());
    }


    public static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }


}
