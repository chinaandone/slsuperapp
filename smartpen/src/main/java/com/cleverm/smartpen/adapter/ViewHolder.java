package com.cleverm.smartpen.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * base adapter ViewHolder
 * 
 * 我们使用了一个SparseArray<View>用于存储与之对于的convertView的所有的控件，当需要拿这些控件时，通过getView(id)进行获取
 * 
 * @author xiongwei
 * 
 */
public class ViewHolder {

	/**
	 * static view array
	 */
	private final SparseArray<View> mViews;
	private View mConvertView;
	private int mLayoutId;
	private int mPosition;

	/**
	 * if convertView is null , LayoutInflater new view layout
	 * @param context
	 * @param parent
	 * @param layoutId
	 * @param position
	 */
	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mViews = new SparseArray<View>();
		this.mLayoutId=layoutId;
		this.mPosition=position;
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * get ViewHolder
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {

		View view = mViews.get(viewId);
		if (view == null) {  
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * finally you need return this convertView
	 * @return
	 */
	public View getConvertView() {
		return mConvertView;
	}
	
	public int getLayoutId(){
		return mLayoutId;
	}
	
	public int getPosition() {
		return mPosition;
	}

}
