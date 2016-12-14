package com.cleverm.smartpen.util.cache;

import android.content.Context;
import android.widget.Toast;

import com.cleverm.smartpen.util.DownloadUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  下午 04:58
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FileRememberUtil {


    public static void init(Context context){
        FileCacheFactory.getInstance().init(context);
    }

    public static boolean has(String dirName){
        if(!FileCacheFactory.getInstance().has(dirName)){
            return true;
        }else{
            return false;
        }
    }


    public static void put(String text,String dirName,String textKey) {
        FileCacheCreator put = null;
        try {
            put = FileCacheFactory.getInstance().put(dirName, 1024);
            put.put(textKey,new ByteArrayInputStream(text.getBytes("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }catch (FileCacheCreateAleadyExistException e) {
            e.printStackTrace();
        }
    }

    public static String get(String dirName,String textKey){
        FileCacheCreator fileCache = null;
        try {
            fileCache = FileCacheFactory.getInstance().get(dirName);
            FileEntry fileEntry = fileCache.get(textKey);
            File file = fileEntry.getFile();
            InputStream inputStream = new FileInputStream(file);
            String read = IOOperation.read(inputStream);
            return read;
        } catch (FileCacheCreateNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
