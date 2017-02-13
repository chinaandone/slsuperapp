package com.cleverm.smartpen.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.WeakHandler;

/**
 * Created by alan on 2016/11/30.
 */

public class moneyActivity extends Activity {

    private ProgressDialog mProgressDialog;
    private WebView webView;
    private String loadUrl = "http://120.77.10.145/cmbc/index.html";
    private int DelayTime=60*1000*15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iv_go_money);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getInt("initPage")==2) {
                loadUrl = "http://120.77.10.145/cmbc/apply.html";
            }
        }
        initView();
        initWebSetting();
    }

    private void initWebSetting() {
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        webView = (WebView) findViewById(R.id.webView);
//        webView.loadUrl("http://120.77.10.145/cmbc/index.html");
        webView.loadUrl(loadUrl);
        webView.requestFocusFromTouch();
        // 启用WebView对JavaScript的支持
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 触摸焦点起作用（如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件）
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                webView.loadUrl(url);
                return true;
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
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });
        doSuccessFinish();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onBack() {
        IntentUtil.goBackToVideoActivity(moneyActivity.this);
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

}
