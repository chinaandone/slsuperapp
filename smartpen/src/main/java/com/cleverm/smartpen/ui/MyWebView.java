package com.cleverm.smartpen.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by 95 on 2016/1/7.
 */
public class MyWebView extends WebView {
    public static final String TAG=MyWebView.class.getSimpleName();
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG,"MyWebView onTouchEvent===");
        mWebViewTouchEvent.isWebViewTouch(true);
        return super.onTouchEvent(event);
    }


    private WebViewTouchEvent mWebViewTouchEvent;
    public void setWebViewTouchEventListener(WebViewTouchEvent mWebViewTouchEvent){
        this.mWebViewTouchEvent=mWebViewTouchEvent;
    }

    public interface WebViewTouchEvent {
        void isWebViewTouch(boolean isTouch);
    }
}
