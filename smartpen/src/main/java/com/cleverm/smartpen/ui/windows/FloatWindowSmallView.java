package com.cleverm.smartpen.ui.windows;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleverm.smartpen.R;

/**
 * 悬浮小窗
 * 
 * @author xiongwei
 * 
 */
public class FloatWindowSmallView extends LinearLayout {
	


	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录小悬浮窗的高度
	 */
	public static int viewHeight;


	/**
	 * 用于更新小悬浮窗的位置
	 */
	private WindowManager windowManager;

	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;

	/**
	 * layout 布局的root
	 */
	private View rootView;



	/**
	 * Ver3.25版本的业务逻辑发生变化:头像上的问号不现实,出现数字时显示数字,同时message不再显示
	 * @param context
	 */
	public FloatWindowSmallView(Application context) {
		super(context);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		rootView = findViewById(R.id.small_window_layout);
		viewWidth = rootView.getLayoutParams().width;
		viewHeight = rootView.getLayoutParams().height;
	}



	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}


}
