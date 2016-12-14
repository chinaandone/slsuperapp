package com.cleverm.smartpen.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cleverm.smartpen.bean.DiscountAdInfo;
import com.cleverm.smartpen.bean.DiscountInfo;
import com.cleverm.smartpen.bean.DiscountRollInfo;
import com.cleverm.smartpen.bean.TableData;
import com.cleverm.smartpen.database.DatabaseHelper;
import com.cleverm.smartpen.database.TableColumns;
import com.cleverm.smartpen.database.TableTypeColumns;
import com.cleverm.smartpen.modle.impl.TableImpl;
import com.cleverm.smartpen.modle.impl.TableTypeImpl;
import com.cleverm.smartpen.pushtable.MessageType;
import com.cleverm.smartpen.pushtable.OrgProfileVo;
import com.cleverm.smartpen.pushtable.bean.TableInfo;
import com.cleverm.smartpen.pushtable.bean.TableResult;
import com.cleverm.smartpen.pushtable.bean.TableTypeInfo;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.ServiceUtil;
import com.cleverm.smartpen.util.WeakHandler;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 2016/3/19.
 * Data:2016-03-19  12:01
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DownloadPicassoService extends Service {
    public static final String TAG =DownloadPicassoService.class.getSimpleName();
    public static final String PICASSO_JSON = "PICASSO_JSON";
    public static final String ORGID = "OrgID";
    public static final String CLIENTID = "clientId";
    private String json = null;
    public static final int SHOWTABLE = 201;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWTABLE: {
                    String data = (String) msg.obj;
                    parserGson(data);
                    Log.v(TAG, "showtable");
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getIntent(intent);
        startDownloader();
        return Service.START_REDELIVER_INTENT;
    }


    private void getIntent(Intent intent) {
        json = intent.getStringExtra(PICASSO_JSON);
    }


    /**
     * 定时1分钟后开启执行任务
     */
    private void startDownloader() {
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                parserJosn();
            }
        }, 60000);
        /**
         * 根据OrgId更新数据
         */
        updataTableData();
    }

    private void updataTableData() {
        long orgid = getClientId();
        if (orgid == Constant.DESK_ID_DEF_DEFAULT) {
            //没有OrgID
        } else {
            //有OrgID
            RequestTableData(orgid);
            Log.v(TAG, "RequestTableData()==");
        }
    }

    private void parserJosn() {
        if (json != null) {
            try {
                List<DiscountInfo> discountInfos = ServiceUtil.getInstance().parserDiscountData(json);
                for (int i = 0; i < discountInfos.size(); i++) {
                    String  out_image = QuickUtils.spliceUrl(discountInfos.get(i).getPictruePath(),discountInfos.get(i).getQiniuPath());
                    QuickUtils.loadImage(out_image);
                    List<DiscountAdInfo> advertisementList = discountInfos.get(i).getAdvertisementList();
                    List<DiscountRollInfo> rollDetailList = discountInfos.get(i).getRollDetailList();
                    if (advertisementList != null && advertisementList.size() > 0) {
                        for (int j = 0; j < advertisementList.size(); j++) {
                            String image = QuickUtils.spliceUrl(advertisementList.get(j).getPictruePath(),advertisementList.get(j).getQiniuPath());
                            QuickUtils.loadImage(image);
                        }
                    }
                    if (rollDetailList != null && rollDetailList.size() > 0) {
                        for (int j = 0; j < rollDetailList.size(); j++) {
                            String image = QuickUtils.spliceUrl(rollDetailList.get(j).getPictruePath(),advertisementList.get(j).getQiniuPath());
                            QuickUtils.loadImage(image);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private long getClientId() {
        long ClientId = Constant.DESK_ID_DEF_DEFAULT;
        String path = Environment.getExternalStorageDirectory().getPath() + "/SystemPen/smartpen.txt";
        File file = new File(path);
        if (!file.exists()) {
            return ClientId;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String data = br.readLine();
            Log.v(TAG, "data=" + data);
            JSONObject object = new JSONObject(data);
            ClientId = Long.parseLong(object.getString("OrgID"));
            Log.v(TAG, "OrgID=" + ClientId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ClientId;
    }

    private void RequestTableData(final long OrgId) {
        new Thread() {
            @Override
            public void run() {
                FetchOrgInfoHandler(OrgId);
                Log.v(TAG, "FetchOrgInfoHandler()");
            }
        }.start();
    }


    /**
     * 根据OrgID主动获取数据
     * @param OrgID
     */
    public void FetchOrgInfoHandler(long OrgID) {
        OrgProfileVo fileVo = new OrgProfileVo();
        fileVo.setOrgID(OrgID);
        com.cleverm.smartpen.pushtable.Message message = com.cleverm.smartpen.pushtable.Message.create().messageType(MessageType.NOTIFICATION).header("Notice-Type", "FETCH_ORG_INFO").json(fileVo).build();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(Constant.DDP_URL+"/cleverm/sockjs/execCommand");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.connect();
            String input = JSON.toJSONString(message);
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes("utf-8"));
            os.flush();
            os.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf8"));
            String valueString = null;
            StringBuffer buffer = new StringBuffer();
            while ((valueString = reader.readLine()) != null) {
                buffer.append(valueString);
            }
            Log.v(TAG, "FetchOrgInfoHandler=" + buffer.toString());
            //请求到数据
            if (buffer.toString() != null) {
                Gson gson0=new Gson();
                TableData Result=gson0.fromJson(buffer.toString(),TableData.class);
                String data =Result.getBody();
                Log.v(TAG, "FetchOrgInfoHandler=msg data="+data);
                if(data==null){//请求到的数据是空
                    Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() data=null");
                }else {
                    Gson gson=new Gson();
                    TableResult tableResult=gson.fromJson(data,TableResult.class);
                    if(tableResult.getTableList().size()==0 || tableResult.getTableTypeList().size()==0){
                        Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() size()=null");
                    }else {
                        Message msg = mHandler.obtainMessage();
                        msg.what = SHOWTABLE;
                        msg.obj = data;
                        msg.sendToTarget();
                        Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() data="+data);
                    }
                }
                Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget()");
            }
            //没有请求到数据
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }
    }


    private void parserGson(String data) {
        Gson gson = new Gson();
        Log.v(TAG, "parserGson()=" + data);
        TableResult tableResult = gson.fromJson(data, TableResult.class);
        String OrgID = tableResult.getOrgID();
        Log.v(TAG, "OrgID=" + OrgID + " ClientID=" + tableResult.getClientID());
        if (OrgID != null) {
            RememberUtil.putString(ORGID, OrgID);
        }
        if (tableResult.getClientID() != null) {
            RememberUtil.putString(CLIENTID, tableResult.getClientID());
        }
        List<TableInfo> ListTableInfo = tableResult.getTableList();
        List<TableTypeInfo> ListTableTypeInfo = tableResult.getTableTypeList();
        insertTableData(ListTableInfo, ListTableTypeInfo);
    }


    private void insertTableData(List<TableInfo> ListTableInfo, List<TableTypeInfo> ListTableTypeInfo) {
        DatabaseHelper databaseHelper = DatabaseHelper.getsInstance(this);
        databaseHelper.deleteAll(TableColumns.TABLE_NAME);
        databaseHelper.deleteAll(TableTypeColumns.TABLE_NAME);
        Log.v(TAG, "insertTableData()");
        for (int i = 0; i < ListTableTypeInfo.size(); i++) {
            String typeId = ListTableTypeInfo.get(i).getTypeId();
            long id = Long.parseLong(typeId);
            String typeName = ListTableTypeInfo.get(i).getTypeName();
            String minimum = ListTableTypeInfo.get(i).getMinimum();
            int min = Integer.parseInt(minimum);
            String capacity = ListTableTypeInfo.get(i).getCapacity();
            int max = Integer.parseInt(capacity);
            String description = ListTableTypeInfo.get(i).getDescription();
            databaseHelper.insertTableType(new TableTypeImpl(id, typeName, min, max, description));
        }

        for (int i = 0; i < ListTableInfo.size(); i++) {
            Log.v(TAG, "ListTableInfo.size()=" + ListTableInfo.size());
            long typeid = Long.parseLong(ListTableInfo.get(i).getTypeId());
            long tableId = Long.parseLong(ListTableInfo.get(i).getTableId());
            String name = ListTableInfo.get(i).getTableName();
            databaseHelper.insertTable(new TableImpl(tableId, typeid, name));
        }
    }

}
