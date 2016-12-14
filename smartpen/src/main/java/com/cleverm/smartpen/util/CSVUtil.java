package com.cleverm.smartpen.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 2/8/2016.
 * Data:2/8/2016  下午 03:26
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class CSVUtil {

    private static CSVUtil INSTANCE = new CSVUtil();

    public static CSVUtil getInstance() {
        return INSTANCE;
    }

    private CSVUtil() {

    }

    /**
     * 生成为CVS文件

     * @param exportData 源数据List
     * @param map        csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName   文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File exportCSVFile(List exportData, LinkedHashMap map, String outPutPath,
                                     String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            //定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "GBK"), 1024);
            System.out.println("csvFileOutputStream：" + csvFileOutputStream);



            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext(); ) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream
                        .write("\"" + (String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" + "\"");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();




            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
                Object row = (Object) iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                        .hasNext(); ) {
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                            .next();
                    //csvFileOutputStream.write((String) BeanUtils.getProperty(row,
                    //        (String) propertyEntry.getKey()) == null ? "" : (String) BeanUtils.getProperty(row,
                    //        (String) propertyEntry.getKey().toString()) + "\t");
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }




}
