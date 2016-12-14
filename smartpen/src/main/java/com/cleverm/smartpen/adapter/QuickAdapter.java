package com.cleverm.smartpen.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cleverm.smartpen.bean.BaseInfoBean;

/**
 * @author xiongwei
 * @param <T>
 *            ArrayList<T>
 *            
 *     子类注意覆写构成方法时，layout布局加上
 *            
 *            
 */
public abstract class QuickAdapter<T extends BaseInfoBean> extends BaseAdapter {

	protected LayoutInflater mInflater;
	// Layout XML id
	protected Context mContext;
	protected final int mLayoutId;
	protected List<T> list;

	public QuickAdapter(Context context, int mLayoutId, List<T> list) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mLayoutId = mLayoutId;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public T getItem(int position) {
		if (list == null || position < 0 || position >= list.size()) return null;
        return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * use like this 
	 * TextView mTitle = viewHolder.getView(R.id.id_tv_title);
	 * mTitle.setText((String)list.get(position));
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
		convert(viewHolder, getItem(position), position);
		return viewHolder.getConvertView();
	}
	
	/**
	 * 把viewHolder和本Item对于的Bean对象给传出去
	 * @param viewHolder
	 * @param item
	 * @param position
	 */
	public abstract void convert(ViewHolder viewHolder, T item, int position);

}
