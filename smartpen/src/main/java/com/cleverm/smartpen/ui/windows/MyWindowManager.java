package com.cleverm.smartpen.ui.windows;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.util.DipPxUtils;


public class MyWindowManager {

	/**
	 * 用于控制在屏幕上添加或移除悬浮窗
	 */
	private static WindowManager mWindowManager;

	/**
	 * 小悬浮窗View的实例
	 */
	private static FloatWindowSmallView smallWindow;

	/**
	 * 小悬浮窗View的参数
	 */
	private static LayoutParams smallWindowParams;


	/**
	 * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
	 * 
	 *
	 *            必须为应用程序的Context.
	 * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
	 */
	private static WindowManager getWindowManager() {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) SmartPenApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	/**
	 * 创建窗体
     * 注意，flag的值可以为： 
     * LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件 
     * LayoutParams.FLAG_NOT_FOCUSABLE  不可聚焦 
     * LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸 
	 *
	 */
	public static void createSmallWindow() {
		WindowManager windowManager = getWindowManager();
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (smallWindow == null) {
			smallWindow = new FloatWindowSmallView(SmartPenApplication.getApplication());
			if (smallWindowParams == null) {
				smallWindowParams = new LayoutParams();
				smallWindowParams.type = LayoutParams.TYPE_TOAST;
				//smallWindowParams.type = LayoutParams.TYPE_APPLICATION_PANEL;
				//smallWindowParams.token = SmartPenApplication.getApplication().getDecorView().getWindowToken();
				smallWindowParams.format = PixelFormat.RGBA_8888;
				smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
				//smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.width = FloatWindowSmallView.viewWidth;
				smallWindowParams.height = FloatWindowSmallView.viewHeight;
				//smallWindowParams.x = screenWidth / 2;
				//smallWindowParams.y = screenHeight /2;
			}
			smallWindow.setParams(smallWindowParams);
			windowManager.addView(smallWindow, smallWindowParams);

		}
	}



	/**
	 * 将小悬浮窗从屏幕上移除。
	 * 
	 *
	 *            必须为应用程序的Context.
	 */
	public static void removeSmallWindow() {
		if (smallWindow != null) {
			WindowManager windowManager = getWindowManager();
			windowManager.removeView(smallWindow);
			smallWindow = null;
		}
	}



	/**
	 * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
	 * 
	 * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
	 */
	public static boolean isWindowShowing() {
		return smallWindow != null;
	}

}
