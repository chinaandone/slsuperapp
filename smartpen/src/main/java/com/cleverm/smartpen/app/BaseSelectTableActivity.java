package com.cleverm.smartpen.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cleverm.smartpen.R;
import com.cleverm.smartpen.bean.TableData;
import com.cleverm.smartpen.database.DatabaseHelper;
import com.cleverm.smartpen.database.TableColumns;
import com.cleverm.smartpen.database.TableTypeColumns;
import com.cleverm.smartpen.fragment.SelectTableFragment;
import com.cleverm.smartpen.modle.TableType;
import com.cleverm.smartpen.modle.impl.TableImpl;
import com.cleverm.smartpen.modle.impl.TableTypeImpl;
import com.cleverm.smartpen.pushtable.MessageType;
import com.cleverm.smartpen.pushtable.OrgProfileVo;
import com.cleverm.smartpen.pushtable.UpdateTableHandler;
import com.cleverm.smartpen.pushtable.bean.TableInfo;
import com.cleverm.smartpen.pushtable.bean.TableResult;
import com.cleverm.smartpen.pushtable.bean.TableTypeInfo;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.ThreadManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 2015/9/16.
 */
public abstract class BaseSelectTableActivity extends BaseActivity implements View.OnClickListener,
        SelectTableFragment.OnTableAdapterListener {
    public static final String ORGID = "OrgID";
    public static final String CLIENTID = "clientId";
    @SuppressWarnings("unused")
    private static final String TAG = BaseSelectTableActivity.class.getSimpleName();
    public static final String SELECTEDTABLEID = "SelectedTableId";
    protected TablePagerAdapter mTablePagerAdapter;
    protected long mSelectedTableId;
    private TabWidget mTableTabHost;
    private ViewPager mTableViewPager;
    private List<TableType> mTableTypes;
    public static final int GOBack = 200;
    public static final int SHOWTABLE = 201;
    public static final int SHOW_INPUY_PSW = 202;
    private EditText mInputOrgId;
    private Button mOrgIdConfirm;
    private android.support.v4.widget.DrawerLayout mDrawerLayout;
    private EditText mPsw;
    private Button mBtnPsw;
    private boolean mHaveData;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOBack: {
                    Log.v(TAG, "come hand====");
                    IntentUtil.goBackToVideoActivity(BaseSelectTableActivity.this);
                    break;
                }
                case SHOWTABLE: {
                    String data = (String) msg.obj;
                    parserGson(data);
                    initTableData();
                    init();
                    Log.v(TAG, "showtable");
                    break;
                }
                case SHOW_INPUY_PSW: {
                    mDrawerLayout.setVisibility(View.VISIBLE);
                    mInputOrgId.setText("");
                    Toast.makeText(BaseSelectTableActivity.this, "输入的商户ID有误,请重新输入！", Toast.LENGTH_LONG).show();
                    mHandler.removeMessages(GOBack);
                    mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_table);
        initView();
        mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
    }

    /**
     * 选择访问数据方式
     * 1.读取长连接数据库中的数据
     * 2.根据OrgID短连接去重新请求数据
     * 3.没有OrgID，输入OrgID，重新做第二步
     */
    private void initView() {
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        mBtnPsw = (Button) findViewById(R.id.bt_psw);
        //xiong add this line on 20160725
        setVersion((TextView)findViewById(R.id.tv_info_version),RememberUtil.getBoolean(Constant.HIDDEN_DOOR_CHARGING_KEY,false));
        mBtnPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String psw = mPsw.getText().toString();
                Log.v(TAG, "psw=" + psw);
                if (Constant.PSW.equals(psw)) {
                    //有数据前提，直接去设置桌号
                    if (mHaveData) {
                        mDrawerLayout.setVisibility(View.GONE);
                    }
                    //无数据前提，去设置OrgID
                    else {
                        mInputOrgId.setHint("请输入商户ID");
                        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        mDrawerLayout.openDrawer(Gravity.RIGHT);
                    }
                } else if(Constant.HIDDEN_DOOR_OPEN_CHARGING.equals(psw)) {
                    RememberUtil.putBoolean(Constant.HIDDEN_DOOR_CHARGING_KEY,true);
                    Toast.makeText(BaseSelectTableActivity.this,"暗门开启,拔出充电宝短信功能立即开放！",Toast.LENGTH_LONG).show();
                    mPsw.setText("");
                    setVersion((TextView) findViewById(R.id.tv_info_version), true);
                }else if(Constant.HIDDEN_DOOR_CLOSE_CHARGING.equals(psw)){
                    RememberUtil.putBoolean(Constant.HIDDEN_DOOR_CHARGING_KEY,false);
                    Toast.makeText(BaseSelectTableActivity.this,"暗门关闭,拔出充电宝短信功能立即关闭！",Toast.LENGTH_LONG).show();
                    mPsw.setText("");
                    setVersion((TextView) findViewById(R.id.tv_info_version), false);
                }else if(Constant.HIDDEN_DOOR_OPEN_ENGINEER.equals(psw)){
                    RememberUtil.putBoolean(Constant.HIDDEN_DOOR_ENGINEER_KEY,true);
                    QuickUtils.toast("工程模式开启！");
                    QuickUtils.log("工程模式开启！");
                }else if(Constant.HIDDEN_DOOR_CLOSE_ENGINEER.equals(psw)){
                    RememberUtil.putBoolean(Constant.HIDDEN_DOOR_ENGINEER_KEY,false);
                    QuickUtils.toast("工程模式关闭！");
                    QuickUtils.log("工程模式关闭！");
                } else {
                    Toast.makeText(BaseSelectTableActivity.this, getString(R.string.psw_error), Toast.LENGTH_LONG).show();
                }


                mPsw.setText("");
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
                Log.v(TAG, "orgid==Constant.DESK_ID_DEF_DEFAUL");
            }
        });
        mTableTabHost = (TabWidget) findViewById(R.id.table_category_tab_host);
        mTableViewPager = (ViewPager) findViewById(R.id.table_panel_container);
        mInputOrgId = (EditText) findViewById(R.id.et_orgid);
        mInputOrgId.addTextChangedListener(watcher);
        mOrgIdConfirm = (Button) findViewById(R.id.bt_orgid);
        mPsw = (EditText) findViewById(R.id.et_psw);
        mPsw.addTextChangedListener(watcher);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dw_psw);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
            }

            @Override
            public void onDrawerOpened(View view) {
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
            }

            @Override
            public void onDrawerClosed(View view) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
            }

            @Override
            public void onDrawerStateChanged(int i) {
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
            }
        });
        mDrawerLayout.closeDrawer(Gravity.RIGHT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mTableTypes = DatabaseHelper.getsInstance(this).obtainAllTableTypes();//得到数据库中的桌子类型
        Log.v(TAG, "mTableTypes=" + mTableTypes.size());
        if (mTableTypes.isEmpty() || mTableTypes.size() == 0) {
            /**
             * 数据库没有数据了，主动去请求数据
             */
            mHaveData = false;
            initData();
            Log.v(TAG, "initData()");
        } else {
            /**
             * 数据库有数据直接去获取
             */
            mHaveData = true;
            initTableData();
            init();
            Log.v(TAG, "initTableData()");
        }
    }

    private void setVersion(TextView tv,boolean isOpen) {
        String text;
        if(isOpen){
            text="开启";
        }else{
            text="关闭";
        }
        tv.setText("IP：" + QuickUtils.getIP() + "     " + "VersionName：" + QuickUtils.getVersionName() + "     " + "VersionCode：" + QuickUtils.getVersionCode()+"     " + "充电宝短信："+text);
    }


    private boolean isGetData=false;
    private void initData() {
        //*******************************************
        mOrgIdConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OrgID = mInputOrgId.getText().toString();
                if (OrgID == null || OrgID.equals("")) {
                    Toast.makeText(BaseSelectTableActivity.this, "商户ID不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                mDrawerLayout.setVisibility(View.GONE);
                setClientId(OrgID);
                //xiong fix bug : double data  (on 20160712)
                if(!isGetData){
                    RequestTableData(Long.parseLong(OrgID));
                }
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
                Log.v(TAG, "RequestTableData()***");
                HideInputFromWindow();
            }
        });
        long orgid = getClientId();
        QuickUtils.log("orgid-----------" + orgid);
        if (orgid == Constant.DESK_ID_DEF_DEFAULT) {
            //布局默认需要输入OrgID
        } else {
            //mDrawerLayout.setVisibility(View.GONE);
            mInputOrgId.setText(orgid + "", null);
            RequestTableData(orgid);
            isGetData=true;
            Log.v(TAG, "RequestTableData()==");
        }
    }

    private void RequestTableData(final long OrgId) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                FetchOrgInfoHandler(OrgId);
                Log.v(TAG, "FetchOrgInfoHandler()");
            }
        });
    }

    /**
     * 初始化桌子信息
     */
    private void initTableData() {
        /**
         * data of the TableTypes
         */
        mTableTypes = DatabaseHelper.getsInstance(this).obtainAllTableTypes();//得到数据库中的桌子类型
        mSelectedTableId = OrderManager.getInstance(this).getTableId();
        mTablePagerAdapter = new TablePagerAdapter(getFragmentManager());
        //select the Table
        long defaultDeskId = RememberUtil.getLong(SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        if (defaultDeskId == Constant.DESK_ID_DEF_DEFAULT) {
            return;
        }
        //设置上次选中的桌号
        OrderManager.getInstance(this).setTableId(defaultDeskId);
    }


    private void init() {
        mTableViewPager.setAdapter(mTablePagerAdapter);
        mTableViewPager.setOnPageChangeListener(mTablePagerAdapter);
        initTabHost();
    }

    private void initTabHost() {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View tabItem;
        for (int i = 0; i < mTableTypes.size(); ++i) {
            tabItem = inflater.inflate(R.layout.item_table_tab, null);
            ((TextView) tabItem.findViewById(R.id.table_type)).setText(mTableTypes.get(i).getName());
            tabItem.setTag(i);
            mTableTabHost.addView(tabItem);
            tabItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTableViewPager.setCurrentItem((Integer) v.getTag());
                    mHandler.removeMessages(GOBack);
                    mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
                }
            });
        }
    }

    class TablePagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        private List<Fragment> mTablePagers;

        public TablePagerAdapter(FragmentManager fm) {
            super(fm);
            if (mTableTypes == null || mTableTypes.isEmpty()) {
                return;
            }
            mTablePagers = new ArrayList<Fragment>();
            for (TableType tableType : mTableTypes) {
                mTablePagers.add(SelectTableFragment.newInstance(tableType.getId()));
            }
        }

        public void updateTablesDisplayStatus(long tableId) {
            for (Fragment fragment : mTablePagers) {
                ((SelectTableFragment) fragment).updateTablesDisplayStatus(tableId);
            }
        }

        @Override
        public Fragment getItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            return mTablePagers.get(i);
        }

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            mTableTabHost.setCurrentTab(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public int getCount() {
            return mTablePagers == null ? 0 : mTablePagers.size();
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

    private void setClientId(String ClientId) {
        String data = "{\"OrgID\":" + ClientId + "}";

        //xiong fixed on 20160715
        RememberUtil.putString(UpdateTableHandler.ORGID,ClientId);

        Log.v(TAG, "data=" + data);
        String path1 = Environment.getExternalStorageDirectory().getPath() + "/SystemPen";
        Log.v(TAG, "path1=" + path1);
        String path2 = Environment.getExternalStorageDirectory().getPath() + "/SystemPen/smartpen.txt";
        File file1 = new File(path1);
        File file2 = new File(path2);
        if (!file1.exists()) {
            Log.v(TAG, "path1=NO");
            file1.mkdirs();
        }
        Log.v(TAG, "path1=YES");
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            OutputStream os = new FileOutputStream(file2);
            os.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    /**
     * 根据OrgID主动获取数据
     *
     * @param OrgID
     */
    public void FetchOrgInfoHandler(long OrgID) {
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
                QuickUtils.log(TAG+ "FetchOrgInfoHandler=msg data=" + data);
                if (data == null) {//请求到的数据是空
                    mHandler.sendEmptyMessage(SHOW_INPUY_PSW);
                    Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() data=null");
                } else {
                    Gson gson = new Gson();
                    TableResult tableResult = gson.fromJson(data, TableResult.class);
                    if (tableResult.getTableList().size() == 0 || tableResult.getTableTypeList().size() == 0) {
                        mHandler.sendEmptyMessage(SHOW_INPUY_PSW);
                        Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() size()=null");
                    } else {
                        Message msg = mHandler.obtainMessage();
                        msg.what = SHOWTABLE;
                        msg.obj = data;
                        msg.sendToTarget();
                        Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget() data=" + data);
                    }
                }
                Log.v(TAG, "FetchOrgInfoHandler=msg.sendToTarget()");
            }
            //没有请求到数据
            else {
                mHandler.sendEmptyMessage(SHOW_INPUY_PSW);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mHandler.removeMessages(GOBack);
            mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            mHandler.removeMessages(GOBack);
            mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);

        }

        @Override
        public void afterTextChanged(Editable s) {
            mHandler.removeMessages(GOBack);
            mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);

        }
    };

    /**
     * 隐藏输入键盘
     */
    private void HideInputFromWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputOrgId.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mInputOrgId.getWindowToken(), 0);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //重置Fragment的preItem
        SelectTableFragment.preItem=-1;
    }
}