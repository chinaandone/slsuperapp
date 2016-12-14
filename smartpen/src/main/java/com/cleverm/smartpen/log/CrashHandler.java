package com.cleverm.smartpen.log;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by levy on 2016/1/7.
 * To be implement more function for log collection
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();
    public static final int ONE_MINUTE=6000;
    private static CrashHandler mInstance;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private String mLogRoot;
    private String mAction;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);

    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);

    public static synchronized CrashHandler getInstance() {
        if (mInstance == null) {
            mInstance = new CrashHandler();
        }
        return mInstance;
    }

    private CrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void init(Context context, String root) {
        init(context, root, null);
    }

    public void init(Context context, String root, String action) {
        mContext = context;
        mLogRoot = root;
        mAction = action;
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadLog();
            }
        }).start();
    }

    private void uploadLog() {
        Log.i(TAG, "uploadLog sleep");
        try {
            Thread.sleep(ONE_MINUTE * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "uploadLog START");
        File root = new File(mLogRoot);
        if (!root.exists()) return;
        ArrayList<File> logFiles = new ArrayList<File>();
        File[] dirs = root.listFiles();
        for (File dir : dirs) {
            File[] logs = dir.listFiles();
            for (File log : logs) {
                logFiles.add(log);
            }
        }

        long orgId = 100;
        try {
            String resultText = "";
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, ONE_MINUTE);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, ONE_MINUTE);
            HttpPost post = new HttpPost("http://120.25.159" +
                                         ".173:8080/cleverm/sockjs/uploadResource");
            MultipartEntity entity =new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("orgID", new StringBody(String.valueOf(orgId)));
            entity.addPart("path", new StringBody(String.valueOf("logs")));
            for (File f : logFiles) {
                entity.addPart("resFile", new FileBody(f));
            }
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity reEntity = response.getEntity();
                InputStreamReader isr = new InputStreamReader(reEntity.getContent(), "UTF-8");
                BufferedReader rd = new BufferedReader(isr);
                String line;
                while ((line = rd.readLine()) != null) {
                    resultText += line;
                }
                Log.i(TAG, "result : " + resultText);
                if ("\"1\"".equalsIgnoreCase(resultText)) {
                    FileUtil.delete(root);
                }
            } else {
                Log.i(TAG, "result fail: " + resultText);
            }
        } catch (Exception e) {
            Log.w(TAG, "", e);
        }
        Log.d(TAG, "uploadLog END");
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Date date = new Date();
        String dirName = "Log_" + sdf.format(date);
        File dir = new File(mLogRoot, dirName);
        if (dir.exists() && dir.listFiles().length > 500) {
            FileUtil.delete(dir.listFiles()[0]);
        }
        File crashFile = newFile(dir, "exception_" + sdf2.format(date) + ".log");
        writeStr2File(readThrowable(ex), crashFile);
        if (!TextUtils.isEmpty(mAction)) {
            mContext.startService(new Intent(mAction));
        }
        mDefaultHandler.uncaughtException(thread, ex);
    }

    public static File newFile(File dir, String name) {
        return newFile(new File(dir, name));
    }

    public static File newFile(File file) {
        if (file == null) return null;
        File dir = file.getParentFile();

        boolean ret;
        if (!dir.exists()) {
            ret = dir.mkdirs();
            if (!ret) return null;
        }
        try {
            ret = file.createNewFile();
        } catch (IOException e) {
            Log.w(TAG, "", e);
            return null;
        }
        return ret ? file : null;
    }

    public static boolean writeStr2File(String str, File file) {
        if (!file.exists()) {
            file = newFile(file);
        }
        if (file == null) return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(str.getBytes("UTF-8"));
        } catch (IOException e) {
            Log.w(TAG, "", e);
            return false;
        } finally {
            FileUtil.closeSilently(fos);
        }
        return true;
    }

    public static String readThrowable(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String ret = writer.toString();
        FileUtil.closeSilently(printWriter);
        FileUtil.closeSilently(writer);
        return ret;
    }
}
