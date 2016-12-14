package com.cleverm.smartpen.app;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.StatisticsUtil;


/**
 * Created by Jimmy on 2015/9/21.
 */
public class SelectTableActivity extends BaseSelectTableActivity {

    @SuppressWarnings("unused")
    private static final String TAG = SelectTableActivity.class.getSimpleName();
    public static final String SELECTEDTABLEID="SelectedTableId";
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
    public void onTableSelected(long tableId) {
        mSelectedTableId = tableId;
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
