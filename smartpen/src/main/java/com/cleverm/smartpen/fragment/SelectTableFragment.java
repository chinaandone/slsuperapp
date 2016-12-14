package com.cleverm.smartpen.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.adapter.TableAdapter;
import com.cleverm.smartpen.app.OrderManager;
import com.cleverm.smartpen.database.DatabaseHelper;
import com.cleverm.smartpen.modle.Table;
import com.cleverm.smartpen.util.QuickUtils;

import java.util.List;


/**
 * Created by Jimmy on 2015/9/17.
 */
public class SelectTableFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = SelectTableFragment.class.getSimpleName();

    private static final String KEY_TABLE_TYPE_ID = "key_table_type_id";

    private View mFragmentView;
    private GridView mTablesGridView;
    private TableAdapter mTableAdapter;
    private List<Table> mTables;
    private OnTableAdapterListener mOnTableAdapterListener;

    public static Fragment newInstance(long tableTypeId) {
        Fragment fragment = new SelectTableFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_TABLE_TYPE_ID, tableTypeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mOnTableAdapterListener = (OnTableAdapterListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                activity.toString() + " must implement OnTableAdapterListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        long tableTypeId = bundle.getLong(KEY_TABLE_TYPE_ID);
        if (tableTypeId == 0) {
            return;
        }
        /**
         * data of the Tables
         */
        //读取数据库中的桌子信息
        mTables = DatabaseHelper.getsInstance(getActivity())
                .obtainTablesByType(tableTypeId);
        mTableAdapter = new TableAdapter(getActivity(), mTables);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mFragmentView = inflater.inflate(R.layout.fragment_select_table,
            container, false);
        initView();
        return mFragmentView;
    }

    public static long preItem=-1;

    private void initView() {
        mTablesGridView = (GridView) mFragmentView.findViewById(R.id.gv_tables);
        mTablesGridView.setAdapter(mTableAdapter);
        mTablesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuickUtils.log("mTables.get(position).getId()"+mTables.get(position).getId()+"\nOrderManager.getInstance(getActivity()).getTableId()="+OrderManager.getInstance(getActivity()).getTableId()+"\npreItem="+preItem);
                if((mTables.get(position).getId()!=preItem&&mTables.get(position).getId()!=OrderManager.getInstance(getActivity()).getTableId())||(preItem!=-1&&mTables.get(position).getId()!=preItem)){
                    preItem=mTables.get(position).getId();
                    mOnTableAdapterListener.onTableSelected(mTables.get(position).getId());
                }
            }
        });
        updateTablesDisplayStatus(OrderManager.getInstance(getActivity()).getTableId());
    }

    public void updateTablesDisplayStatus(long selectedTableId) {
        if (mTableAdapter != null) {
            mTableAdapter.updateTablesDisplayStatus(selectedTableId);
        }
    }

    public interface OnTableAdapterListener {

        void onTableSelected(long tableId);
    }
}
