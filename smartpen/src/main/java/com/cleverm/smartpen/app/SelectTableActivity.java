package com.cleverm.smartpen.app;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.bean.BleSetInfo;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.ServiceUtil;
import com.cleverm.smartpen.util.StatisticsUtil;

import org.json.JSONException;


/**
 * Created by Jimmy on 2015/9/21.
 */
public class SelectTableActivity extends BaseSelectTableActivity {

    @SuppressWarnings("unused")
    private static final String TAG = SelectTableActivity.class.getSimpleName();
    public static final String SELECTEDTABLEID="SelectedTableId";
    //add by zwd for record tablename for call service
    public static final String SELECTEDTABLENAME = "SelectedTableName";
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel: {
                onBackPressed();
                break;
            }
            case R.id.btn_confirm: {
                if (mSelectedTableId == -1) {
                    Toast.makeText(this, "no_table_selected", Toast.LENGTH_LONG).show();
                } else {
                    OrderManager.getInstance(this).setTableId(mSelectedTableId);
                    onBackPressed();
                    Log.v(TAG, "mSelectedTableId=" + mSelectedTableId);
                    RememberUtil.putLong(SELECTEDTABLEID,mSelectedTableId);
                    //用到的桌名也写入
                    RememberUtil.putString(SELECTEDTABLENAME,mSelectedTableName);
                    //add by zwd for record tablename for call service
//                    RememberUtil.putString(SELECTEDTABLENAME,mSelectedTableName);  //commit for get tablename from db
                    //add by Randy for get Ble set info end after set talbeId
                    ServiceUtil.getInstance().getBleSetInfo(QuickUtils.getOrgIdFromSp(),
                            String.valueOf(RememberUtil.getLong(BaseSelectTableActivity.SELECTEDTABLEID, 8888)),
                            new ServiceUtil.JsonInterface() {
                                @Override
                                public void onSucced(String json) {
                                    try {
                                        BleSetInfo bleSetInfo=ServiceUtil.getInstance().parserSingleData(json, BleSetInfo.class);
//                                        RememberUtil.putInt(SimpleAppActivity.SPE_WATCH_ADD,Integer.parseInt(bleSetInfo.getWatchadd()));
                                        //支持多个手环配置
                                        RememberUtil.putString(SimpleAppActivity.SPE_WATCH_ADD,bleSetInfo.getWatchadd());
//                                        RememberUtil.putInt(SimpleAppActivity.SPE_DONGLE_ADD,Integer.parseInt(bleSetInfo.getDongleadd()));
                                        RememberUtil.putString(SimpleAppActivity.SPE_DONGLE_ADD, bleSetInfo.getDongleadd());
                                        RememberUtil.putString(SimpleAppActivity.SPE_BLE_MAC,bleSetInfo.getMac_address());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFail(String error) {

                                }
                            });
                    //add by Randy get Ble set info end

                }
                break;
            }
            default:
                break;
        }
        mHandler.removeCallbacksAndMessages(null);
        /*Intent intent = new Intent(this, VideoActivity.class);
        IntentUtil.intentFlagNotClear(intent);
        startActivity(intent);*/
        IntentUtil.goBackToVideoActivity(SelectTableActivity.this);
    }

    @Override
    public void onTableSelected(long tableId,String tableName) {
        mSelectedTableId = tableId;
        mSelectedTableName = tableName;
        mTablePagerAdapter.updateTablesDisplayStatus(tableId);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(GOBack, Constant.DELAY_BACK);
        Log.v(TAG,"onTableSelected()  tableId="+tableId);
    }

    @Override
    protected int onGetEventId() {
        return StatisticsUtil.SETTING;
    }

    @Override
    protected String onGetDesc() {
        return StatisticsUtil.SETTING_DESC;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
