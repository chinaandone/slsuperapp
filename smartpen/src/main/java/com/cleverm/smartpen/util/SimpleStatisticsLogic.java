package com.cleverm.smartpen.util;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.statistic.dao.StatsDao;
import com.cleverm.smartpen.util.excle.CreateExcel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;

/**
 * Created by xiong,An android project Engineer,on 29/7/2016.
 * Data:29/7/2016  下午 05:44
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class SimpleStatisticsLogic {

    public static final String UPLOAD_FILE_URL=Constant.DDP_URL+"/api/api/v10/uploadAccessLog/saveCSV";

    private static SimpleStatisticsLogic INSTANCE = new SimpleStatisticsLogic();
    private static final int POLLTIME=1800;//1800s = 30 分钟
    private Timer timer;
    private Activity context;
    private static AtomicInteger count=new AtomicInteger();//retry机制

    public static SimpleStatisticsLogic getInstance() {
        return INSTANCE;
    }

    private  SimpleStatisticsLogic(){

    }

    public void start(final Activity context){
        this.context=context;
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                go();
            }
        });
    }

    private void go() {
        if(NetWorkUtil.hasNetwork()){
            startSql();
        }else{
            initTimeStart();
        }
    }

    private void initTimeStart() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (NetWorkUtil.hasNetwork()) {
                    startSql();
                }
            }
        }, 1000, POLLTIME);
    }

    private void startSql() {
        if(timer!=null){
            timer.cancel();
        }
        doSql();
    }



    private Cursor exeSql(String sql) {
        return SmartPenApplication.getDb().rawQuery(sql, null);
    }

    private void updateSql(String sql){
        SmartPenApplication.getDb().execSQL(sql);
    }

    private void doSql() {
        String QUERY_CONDITION = StatsDao.Properties.Flag.columnName+" = "+"'"+0+"'";

        String SQL_DATA_ID="select _id from t_stats where "+QUERY_CONDITION;
        Cursor cursor1 = exeSql(SQL_DATA_ID);

        if(cursor1.getCount()<=0){
            return;
        }

        String SQL_DATA="select * from t_stats where "+QUERY_CONDITION;
        Cursor cursor = exeSql(SQL_DATA);

        File file = doExport(cursor);
        String sqlUploadTemp = doId(cursor1);

        if(file!=null){
            //StringBuilder builder = readFileByLines(file);
            doUpload(sqlUploadTemp,file);
        }

    }

    private String doId(Cursor cursor) {
        StringBuilder builder=new StringBuilder();
        while (cursor.moveToNext()){
            String string = cursor.getString(0);
            if(!cursor.isLast()){
                builder.append(string+",");
            }else{
                builder.append(string);
            }
        }
        cursor.close();
        return builder.toString();
    }


    public static StringBuilder readFileByLines(File file) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                builder.append("line " + line + ": " + tempString);
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return builder;
    }



    private File doExport(Cursor cursor) {
        int rowCount = 0;
        int colCount = 0;
        FileWriter fw;
        BufferedWriter bfw;
        //String path = CreateExcel.EXPORT_PATH + File.separator+getTimestamp()+".csv";
        String path = AlgorithmUtil.FILE_MFILE + File.separator+ getTimestamp()+".csv";

        File saveFile = new File(path);
        try {
            rowCount = cursor.getCount();//数据行数
            colCount = cursor.getColumnCount();//列数
            fw = new FileWriter(saveFile);
            String encoding = fw.getEncoding();
            bfw = new BufferedWriter(fw);
            if (rowCount > 0) {
                cursor.moveToFirst();
                // 写入表头
                for (int i = 0; i < colCount; i++) {
                    if (i != colCount - 1)
                        bfw.write(cursor.getColumnName(i) + ',');
                    else
                        bfw.write(cursor.getColumnName(i));
                }
                // 写好表头后换行
                bfw.newLine();

                // 写入数据
                for (int i = 0; i < rowCount; i++) {
                    cursor.moveToPosition(i);
                    Log.v("导出数据", "正在导出第" + (i + 1) + "条");
                    for (int j = 0; j < colCount; j++) {
                        if (j != colCount - 1)
                            bfw.write(cursor.getString(j) + ',');
                        else
                            bfw.write(cursor.getString(j));
                    }
                    // 写好每条记录后换行
                    bfw.newLine();
                }
            }
            // 将缓存数据写入文件
            bfw.flush();
            // 释放缓存
            bfw.close();
            Log.v("导出数据", "导出完毕！");
            return saveFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }
    }

    private void doUpload(final  String sqlUploadTemp,final File file) {
        OkHttpUtils.post()
                .addParams("orgId", QuickUtils.getOrgIdFromSp())
                .addParams("tableId", QuickUtils.getDeskId()+"")
                .addParams("path", "statistic")
                .addFile("resFile", file.getName(), file)//
                .url(UPLOAD_FILE_URL)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e) {
                        doRetry(sqlUploadTemp, file);
                    }

                    @Override
                    public void onResponse(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                            if (code.equals("1000")) {
                                doUpdateSql(sqlUploadTemp);
                                QuickUtils.log("上传点击事件数据成功！");
                            } else {
                                doRetry(sqlUploadTemp, file);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

    }




    private void doUpdateSql(String sqlUploadTemp) {
        String SET_CONDITION = StatsDao.Properties.Flag.columnName+" = "+"'"+1+"'";
        String SQL_DATA="update t_stats set "+SET_CONDITION+" where "+StatsDao.Properties.Id.columnName+" in "+"("+sqlUploadTemp+")";
        QuickUtils.log( "sqlUploadTemp=" + SQL_DATA);
        updateSql(SQL_DATA);
    }

    private void doRetry(final  String sqlUploadTemp,final File file) {
        count.getAndIncrement();
        if(count.get()<3){//retry 3 times
            doUpload(sqlUploadTemp,file);
        }
    }

    /**
     * 以时间戳为文件名
     * @return
     */
    private String getTimestamp(){
        return System.currentTimeMillis()+"";
    }


}
