package com.cleverm.smartpen.app.webview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.cleverm.smartpen.util.ThreadManager;
import com.cleverm.smartpen.util.WeakHandler;

/**
 * Created by regis on 2017/5/23.
 */

public class BasicWebActivity extends Activity {
    private ProgressDialog mProgressDialog;
    public WebView webView;
    private String loadUrl = "http://120.77.10.145/cmbc/index.html";
    private int DelayTime=60*1000;
    public static final int TIME = 60000;
    public static final int ReturnToVideoAcvity = 200;

    public Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
//            if(bundle.getInt("initPage")==2) {
//                loadUrl = "http://120.77.10.145/cmbc/apply.html";
//            }
//
            loadUrl=bundle.getString("url");

        }
        initView();
        initWebSetting();
    }

    public void initWebSetting() {
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        webView = (WebView) findViewById(R.id.webView);
//
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }
        });
        webView.loadUrl(loadUrl);
        webView.requestFocusFromTouch();
        // 启用WebView对JavaScript的支持
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        // 触摸焦点起作用（如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件）
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                webView.loadUrl(url);
                view.loadUrl(url);
                return true;
//                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressDialog.hide();
                mHandler.sendEmptyMessageDelayed(ReturnToVideoAcvity, TIME);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });
//        doSuccessFinish();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBack() {
        IntentUtil.goBackToVideoActivity(currentActivity);
    }

    private void doSuccessFinish() {
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                finish();
                onBack();
            }
        },DelayTime);
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ReturnToVideoAcvity: {
                    onBack();
                    break;
                }

            }
        }
    };
    @Override
    public void onUserInteraction() {
        mHandler.removeMessages(ReturnToVideoAcvity);
        mHandler.sendEmptyMessageDelayed(ReturnToVideoAcvity, TIME);
        super.onUserInteraction();

    }

    @Override
    public void finish() {
        super.finish();
        Log.v("WEB","------------------------------------web finish");
        webView.destroy();
    }

    protected void doStatistic(final int eventId, final String eventDesc) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                StatisticsUtil.getInstance().insert(eventId, eventDesc);
            }
        });
    }

}
