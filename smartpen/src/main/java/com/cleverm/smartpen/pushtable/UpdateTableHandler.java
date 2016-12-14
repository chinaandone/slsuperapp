package com.cleverm.smartpen.pushtable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cleverm.smartpen.app.VideoActivity;
import com.cleverm.smartpen.database.DatabaseHelper;
import com.cleverm.smartpen.database.TableColumns;
import com.cleverm.smartpen.database.TableTypeColumns;
import com.cleverm.smartpen.modle.impl.TableImpl;
import com.cleverm.smartpen.modle.impl.TableTypeImpl;
import com.cleverm.smartpen.pushtable.bean.TableInfo;
import com.cleverm.smartpen.pushtable.bean.TableResult;
import com.cleverm.smartpen.pushtable.bean.TableTypeInfo;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.RememberUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UpdateTableHandler extends NoticeHandler<RestaurantVo, Void> {
    private static final String TAG = UpdateTableHandler.class.getSimpleName();

    private static final String SYSTEMCFG_DIR = "/clevermodel/systemcfg/";
    private static final String BASIC_SETTINGS_FILE = SYSTEMCFG_DIR + "basic_settings.cfg";
    private static final String REPEATER_SETTINGS_FILE = SYSTEMCFG_DIR + "rep_settings.cfg";
    private static final String CALLER_SETTINGS_FILE = SYSTEMCFG_DIR + "caller_settings.cfg";
    public static final String ORGID ="OrgID";
    public static final String CLIENTID ="clientId";


    private Context mContext;

    public UpdateTableHandler(Context context) {
        super("CONTENT_TABLE");
        mContext = context;
    }

    @Override
    public Response<Void> onSuccess(Headers headers, RestaurantVo in) {
        /**
         * UpdataTable
         */
        Log.v(TAG,"Response<Void> onSuccess"+JSON.toJSONString(in));
        parserGson(JSON.toJSONString(in));
//        if (in == null) return null;
//
//        String digest = headers == null ? Utils.getMD5(JSON.toJSONString(in)) : headers
//            .getContentMD5();
//        try {
//            if (update(in)) {
//                Intent intent = new Intent(Constant.ACTION_RESET_DESK);
//                Utils.sendLocalBroadcast(mContext, intent);
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//            return null;
//        }
//
//        PreferencesHelper.getInstance(mContext).putString(Constant.KEY_TABLE_TYPE_MD5,
//            digest);

        return null;
    }

    /**
     * get TableInfo
     * @param data
     */
    private void parserGson(String data){
        Log.v(TAG,"parserGson()="+data);
        Gson gson=new Gson();
        TableResult tableResult=gson.fromJson(data,TableResult.class);
        String OrgID=tableResult.getOrgID();
        Log.v(TAG,"OrgID="+OrgID+" ClientID="+tableResult.getClientID());
        RememberUtil.putString(ORGID,OrgID);
        RememberUtil.putString(CLIENTID,tableResult.getClientID());
        List<TableInfo> ListTableInfo=tableResult.getTableList();
        List<TableTypeInfo> ListTableTypeInfo=tableResult.getTableTypeList();
        insertTableData(ListTableInfo, ListTableTypeInfo);
    }

    /**
     * Terry Test
     */
    private void insertTableData(List<TableInfo> ListTableInfo,List<TableTypeInfo> ListTableTypeInfo){
        DatabaseHelper databaseHelper=DatabaseHelper.getsInstance(mContext);
        databaseHelper.deleteAll(TableColumns.TABLE_NAME );
        databaseHelper.deleteAll(TableTypeColumns.TABLE_NAME );
        Log.v(TAG,"insertTableData()");
        for(int i=0;i<ListTableTypeInfo.size();i++){
            String typeId=ListTableTypeInfo.get(i).getTypeId();
            long id=Long.parseLong(typeId);
            String typeName=ListTableTypeInfo.get(i).getTypeName();
            String minimum=ListTableTypeInfo.get(i).getMinimum();
            int min=Integer.parseInt(minimum);
            String capacity=ListTableTypeInfo.get(i).getCapacity();
            int max=Integer.parseInt(capacity);
            String description=ListTableTypeInfo.get(i).getDescription();
            databaseHelper.insertTableType(new TableTypeImpl(id,typeName,min,max,description));
        }

        for(int i=0;i<ListTableInfo.size();i++){
            Log.v(TAG,"ListTableInfo.size()="+ListTableInfo.size());
            long typeid    =Long.parseLong(ListTableInfo.get(i).getTypeId());
            long tableId=Long.parseLong(ListTableInfo.get(i).getTableId());
            String name=ListTableInfo.get(i).getTableName();
            databaseHelper.insertTable(new TableImpl(tableId, typeid, name));
        }
        sendUpdateTableHandlerSuccess();

//        databaseHelper.insertTableType(new TableTypeImpl(1L,"2 People",2,2,"1"));
//        databaseHelper.insertTableType(new TableTypeImpl(2L,"3 People",3,3,"2"));
//        databaseHelper.insertTableType(new TableTypeImpl(3L,"4 People",4,4,"3"));
//        databaseHelper.insertTableType(new TableTypeImpl(4L,"5 People",5,5,"4"));
//        databaseHelper.insertTableType(new TableTypeImpl(5L,"6 People",6,6,"5"));
//
//        databaseHelper.insertTable(new TableImpl(1L, 1L, "A01"));
//        databaseHelper.insertTable(new TableImpl(12L, 1L, "A02"));
//        databaseHelper.insertTable(new TableImpl(13L, 1L, "A03"));
//
//        databaseHelper.insertTable(new TableImpl(21L, 2L, "B01"));
//        databaseHelper.insertTable(new TableImpl(212L, 3L, "C02"));
//        databaseHelper.insertTable(new TableImpl(213L, 3L, "C03"));

    }

    private void sendUpdateTableHandlerSuccess() {
        Intent in=new Intent(mContext, VideoActivity.UpdateTableHandlerSuccess.class);
        in.setAction("UpdateTableHandlerSuccess");
        mContext.sendBroadcast(in);
    }

    private boolean update(RestaurantVo in) {
        boolean flag = true;

//        // update basic info of Client
//        PreferencesHelper.getInstance(mContext).putString(Constant.KEY_RESTAURANT_NAME, in.getOrgName());
//        PreferencesHelper.getInstance(mContext).putLong(Constant.KEY_RESTAURANT_ID, in.getOrgID());
//        PreferencesHelper.getInstance(mContext).putLong(Constant.KEY_CLIENT_ID, in.getClientID());
//
//        // First found devices
//        List<RDeviceVo> devices = in.getDeviceList();
//        HashMap<Long, RDeviceVo> deviceMap = new HashMap<>();
//        for (RDeviceVo d : devices) {
//            // update profile by specific device type and tag
//            if (d.getDeviceType() == RDeviceVo.TYPE_VIRTUAL && !"basic".equalsIgnoreCase(
//                d.getName())) {
//                updateProfile(d);
//            } else {
//                deviceMap.put(d.getDeviceId(), d);
//            }
//        }
//
//        // update table info in database
//        ContentResolver resolver = mContext.getContentResolver();
//        List<RTableTypeVo> tableTypeList = in.getTableTypeList();
//        List<RTableVo> tableList = in.getTableList();
//        List<RTableZoneVo> tableZoneList = in.getTableZoneList();
//        HashMap<Long, RTableZoneVo> zoneMap = new HashMap<>();
//        for (RTableZoneVo z : tableZoneList) {
//            zoneMap.put(z.getZoneId(), z);
//        }
//
//        Log.i(TAG, "TableType Size:" + tableTypeList.size());
//        Log.i(TAG, "Table Size:" + tableList.size());
//        Log.i(TAG, "TableZone Size:" + tableZoneList.size());
//
//        Collections.sort(tableTypeList, new Comparator<RTableTypeVo>() {
//            @Override
//            public int compare(RTableTypeVo lhs, RTableTypeVo rhs) {
//                return lhs.getCapacity() - rhs.getCapacity();
//            }
//        });
//
//        if (tableTypeList.size() != 0) {
//            int id = resolver.delete(CleverModelProvider.URI_TABLE_TYPE_ACCESS, null, null);
//            if (id < 0) {
//                throw new RuntimeException("clear table type data");
//            }
//        }
//        ArrayList<ContentProviderOperation> insertOperations = new ArrayList<>();
//        for (RTableTypeVo t : tableTypeList) {
//            ContentValues cv = new ContentValues();
//            cv.put(TableTypeColumns._ID, t.getTypeId());
//            cv.put(TableTypeColumns.NAME, t.getTypeName());
//            cv.put(TableTypeColumns.MIN, t.getMinimum());
//            cv.put(TableTypeColumns.MAX, t.getCapacity());
//            cv.put(TableTypeColumns.DESCRIPTION, t.getMinimum() + "-" + t.getCapacity());
//            insertOperations.add(ContentProviderOperation.newInsert(
//                CleverModelProvider.URI_TABLE_TYPE_ACCESS).withValues(cv).build());
//            Log.d(TAG, "typeId : " + t.getTypeId());
//        }
//        try {
//            resolver.applyBatch(CleverModelProvider.AUTHORITY, insertOperations);
//        } catch (RemoteException | OperationApplicationException e) {
//            Log.e(TAG, "", e);
//        }
//
//        if (tableList.size() != 0) {
//            int id = resolver.delete(CleverModelProvider.URI_TABLE_ACCESS, null, null);
//            if (id < 0) {
//                throw new RuntimeException("clear table data");
//            }
//        }
//        insertOperations.clear();
//        for (RTableVo t : tableList) {
//            ContentValues cv = new ContentValues();
//            cv.put(TableColumns._ID, t.getTableId());
//            cv.put(TableColumns.TYPE_ID, t.getTypeId());
//            cv.put(TableColumns.NAME, t.getTableName());
//            cv.put(TableColumns.STATE, TableState.INVALID.ordinal());
//            try {
//                cv.put(TableColumns.SERIES_NUMBER, deviceMap.get(t.getBeeperID()).getName());
//            } catch (NullPointerException e) {
//                Log.d(TAG, "", e);
//                continue;
//            }
//            Long zoneId = t.getZoneId();
//            if (zoneId != null) {
//                RTableZoneVo z = zoneMap.get(zoneId);
//                if (z != null){
//                    cv.put(TableColumns.BLOCK, z.getZoneName());
//                    cv.put(TableColumns.BLOCK_ORDER, zoneId);
//                }
//            }
//            cv.put(TableColumns.LAST_MODIFIED_DATE, Calendar.getInstance().getTimeInMillis());
//            insertOperations.add(ContentProviderOperation.newInsert(
//                CleverModelProvider.URI_TABLE_ACCESS).withValues(cv).build());
//            Log.d(TAG, t.toString());
//        }
//        try {
//            resolver.applyBatch(CleverModelProvider.AUTHORITY, insertOperations);
//        } catch (RemoteException | OperationApplicationException e) {
//            e.printStackTrace();
//        }
//
//        TableMatcher.init(mContext);
//
//        // update cm_433 setting files
//        File fSystemCfgDir = new File(SYSTEMCFG_DIR);
//        if (!fSystemCfgDir.exists()) {
//            Log.d(TAG, SYSTEMCFG_DIR + " is not exist");
//            return flag;
//        }
//
//        StringBuilder baseConfig = new StringBuilder();
//        baseConfig.append("[cm_433_settings]\n");
//        StringBuilder callerConfig = new StringBuilder();
//        callerConfig.append("[caller]\n");
//        StringBuilder repeaterConfig = new StringBuilder();
//        repeaterConfig.append("[repeater]\n");
//
//        for (RDeviceVo d : devices) {
//            int type = d.getDeviceType();
//            String config = "";
//            try {
//                JSONObject jsonObject = new JSONObject(d.getParameter01());
//                if (type == RDeviceVo.TYPE_CALLER) {
//                    config = config + "ID=" + jsonObject.getString("sn") + "\n";
//                    config = config + "ADDR=" + jsonObject.getString("addr") + "\n";
//                    config = config + "STATE=" + 1 + "\n\n";
//                    callerConfig.append(config);
//                } else if (type == RDeviceVo.TYPE_REPEATER) {
//                    config = config + "ID=" + jsonObject.getString("sn") + "\n";
//                    config = config + "ADDR=" + jsonObject.getString("addr") + "\n";
//                    config = config + "STATE=" + 1 + "\n\n";
//                    repeaterConfig.append(config);
//                } else {
//                    jsonObject.getString("PRIVATE_KEY");
//                    baseConfig.append("PRIVATE_KEY=").append(jsonObject.getString("PRIVATE_KEY"))
//                        .append("\n");
//                    baseConfig.append("HIGH_CHANNEL=").append(jsonObject.getString(
//                        "HIGH_CHANNEL")).append("\n");
//                    baseConfig.append("HIGH_BACKUP_CHANNEL=").append(jsonObject.getString(
//                        "HIGH_BACKUP_CHANNEL")).append("\n");
//                    baseConfig.append("LOW_CHANNEL=").append(jsonObject.getString("LOW_CHANNEL"))
//                        .append("\n");
//                }
//            } catch (JSONException e) {
//                Log.e(TAG, "", e);
//            }
//        }
//
//        if (flushFile(BASIC_SETTINGS_FILE, baseConfig.toString()) &&
//            flushFile(CALLER_SETTINGS_FILE, callerConfig.toString()) &&
//            flushFile(REPEATER_SETTINGS_FILE, repeaterConfig.toString())) {
//            Intent intent = new Intent();
//            intent.setAction(Constant.ACTION_UPDATE_CONFIG);
//            mContext.startService(intent);
//        } else {
//            flag = false;
//        }

        return flag;
    }

    private void updateProfile(RDeviceVo d) {
        try {
            JSONObject profile = new JSONObject(d.getParameter01());
            if ("local_profile".equals(d.getName())) {
                updateLocalProfileFile(profile);
            }
            if ("print_profile".equals(d.getName())){
                updatePrintProfile(profile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean updateLocalProfileFile(JSONObject jsonObject) {
        File dir = new File(Constant.RESOURCEPATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) return false;
        }
        return flushFile(Constant.LOCAL_PROFILE_PATH, jsonObject.toString());
    }

    private void updatePrintProfile(JSONObject jsonObject) {
        try {
            String restaurant_comments = jsonObject.getString(Constant.KEY_RESTAURANT_COMMENTS);
            PreferencesHelper.getInstance(mContext).putString(Constant.KEY_RESTAURANT_COMMENTS,
                    restaurant_comments);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean flushFile(String filePath, String content) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            file.setExecutable(true, false);
            file.setWritable(true, false);
            file.setReadable(true, false);
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "", e);
            return false;
        } finally {
            Utils.closeSilently(fos);
        }
        return true;
    }

    private String md5(String message) {
        String hashtext = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(message.getBytes());
            byte[] digest = md5.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return hashtext;
    }

    @Override
    public void onError(int code, String message) {
        // TODO Auto-generated method stub
    }


}
