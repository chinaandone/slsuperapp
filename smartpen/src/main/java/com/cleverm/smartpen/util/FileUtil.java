package com.cleverm.smartpen.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by xiong,An android project Engineer,on 4/5/2016.
 * Data:4/5/2016  上午 11:46
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class FileUtil {


    /**
     * 判断文件是否存在
     * @param file
     * @return
     */
    public static boolean checkFileExist(File file){
        if(file.exists() && file.isFile()){
            return true;
        }
        return false;
    }

    /**
     * 判断文件夹是否存在
     * @param file
     * @return
     */
    public static boolean checkFolderExist(File file){
        if(file .exists()  && file .isDirectory()){
            return true;
        }
        return false;
    }

    /**
     * 创建文件
     * @param path
     */
    public static void createFile(String path){
        File file = new File(path);
        if(!file.exists()&&!file.isDirectory()){
            file.mkdir();
        }
    }

    /**
     * 读取文件中的内容
     * @param directory
     * @param txtName
     * @return
     * @throws IOException
     */
    public static String getFileContext(String directory,String txtName) throws IOException {
        File file=new File(directory,txtName);
        File parent = file.getParentFile();
        if(parent!=null&&!parent.exists()){
            parent.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
        }
        if(file.length()<=0){
            return null;
        }
        if(!file.exists()||file.isDirectory()) throw new FileNotFoundException();
        String result = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result = result + "\n" +s;
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
