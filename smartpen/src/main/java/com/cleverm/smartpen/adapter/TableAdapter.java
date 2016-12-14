package com.cleverm.smartpen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.modle.Table;
import com.cleverm.smartpen.util.QuickUtils;

import java.util.List;


/**
 * Created by Jimmy on 2015/9/18.
 */
public class TableAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = TableAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Table> mTables;
    private long mSelectedTableId = -1;


    private long mPreSelectedTableId = -1;

    public TableAdapter(Context context, final List<Table> tables) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mTables = tables;
    }

    public void updateTablesDisplayStatus(long tableId) {
        mPreSelectedTableId = mSelectedTableId;
        mSelectedTableId = tableId;
        QuickUtils.log("setBackgroundResource:mSelectedTableId=" + tableId);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTables == null ? 0 : mTables.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        return mTables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_table_grid, null);
            vh = new ViewHolder();
            vh.table = (TextView) convertView.findViewById(R.id.table);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Table table = (Table) getItem(position);
        vh.table.setText(table.getName());
        settableNameSize(table.getName(), vh.table);

        QuickUtils.log("setBackgroundResource -table-getId=:" + table.getId());


        if (mSelectedTableId == table.getId()) {
            vh.table.setBackgroundResource(R.mipmap.ic_table_selected);
            QuickUtils.log("setBackgroundResource:ic_table_selected");
        }else{
            vh.table.setBackgroundColor(Color.TRANSPARENT);
        }



        if (mPreSelectedTableId == table.getId()) {
            vh.table.setBackgroundColor(Color.TRANSPARENT);
            QuickUtils.log("setBackgroundResource:TRANSPARENT");
        }

        /*else {
            vh.table.setBackgroundColor(Color.TRANSPARENT);
            QuickUtils.log("setBackgroundResource:TRANSPARENT");
        }*/

        return convertView;
    }

    class ViewHolder {
        TextView table;
    }

    private void settableNameSize(String tableName, TextView tv) {
        if (tableName == null) {
            return;
        }
        int len = tableName.getBytes().length;
        Log.v(TAG, "tableName=" + tableName + "  len=" + len);
        if (len <= 6) {
            tv.setTextSize(34f);
        } else if (len < 12) {
            tv.setTextSize(25f);
        }
    }
}
