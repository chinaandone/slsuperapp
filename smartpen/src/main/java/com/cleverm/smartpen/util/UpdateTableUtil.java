package com.cleverm.smartpen.util;

import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cleverm.smartpen.app.BaseSelectTableActivity;
import com.cleverm.smartpen.application.SmartPenApplication;
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
 * Created by xiong,An android project Engineer,on 12/7/2016.
 * Data:12/7/2016  下午 02:27
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class UpdateTableUtil {

    private static final String TAG = UpdateTableUtil.class.getSimpleName()+": ";


    private static UpdateTableUtil INSTANCE = new UpdateTableUtil();

    public static UpdateTableUtil getInstance() {
        return INSTANCE;
    }

    private UpdateTableUtil() {
    }


    public void goNewTable(){
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                long orgId = getOrgId();
                QuickUtils.log("FetchOrgInfoHandler()-OrgId=" + orgId);
                if(orgId!=Constant.DESK_ID_DEF_DEFAULT){
                    FetchOrgInfoHandler(orgId);
                    Log.v(TAG, "FetchOrgInfoHandler()");
                }
            }
        });
    }


    private long getOrgId() {
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
            Log.v(TAG, "ClientId=" + ClientId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ClientId;

    }




    public void FetchOrgInfoHandler(long OrgID) {
        QuickUtils.log("tableid："+OrgID + "");
        OrgProfileVo fileVo = new OrgProfileVo();
        fileVo.setOrgID(OrgID);
        com.cleverm.smartpen.pushtable.Message message = com.cleverm.smartpen.pushtable.Message.create().messageType(MessageType.NOTIFICATION).header("Notice-Type", "FETCH_ORG_INFO").json(fileVo).build();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(Constant.DDP_URL + "/cleverm/sockjs/execCommand");
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
                Gson gson0 = new Gson();
                TableData Result = gson0.fromJson(buffer.toString(), TableData.class);
                String data = Result.getBody();
                Log.v(TAG, "FetchOrgInfoHandler=msg data=" + data);
                if (data == null) {//请求到的数据是空
                    Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() data=null");
                } else {
                    Gson gson = new Gson();
                    TableResult tableResult = gson.fromJson(data, TableResult.class);
                    if (tableResult.getTableList().size() == 0 || tableResult.getTableTypeList().size() == 0) {

                        Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() size()=null");
                    } else {
                        parserGson(data);
                        Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() data=" + data);
                    }
                }
                Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget()");
            }
            //没有请求到数据
            else {
            }
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
            RememberUtil.putString(BaseSelectTableActivity.ORGID, OrgID);
        }
        if (tableResult.getClientID() != null) {
            RememberUtil.putString(BaseSelectTableActivity.CLIENTID, tableResult.getClientID());
        }
        List<TableInfo> ListTableInfo = tableResult.getTableList();
        List<TableTypeInfo> ListTableTypeInfo = tableResult.getTableTypeList();
        insertTableData(ListTableInfo, ListTableTypeInfo);
    }

    private void insertTableData(List<TableInfo> ListTableInfo, List<TableTypeInfo> ListTableTypeInfo) {
        DatabaseHelper databaseHelper = DatabaseHelper.getsInstance(SmartPenApplication.getApplication());
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
