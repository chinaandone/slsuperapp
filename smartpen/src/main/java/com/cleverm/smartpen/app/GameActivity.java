package com.cleverm.smartpen.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.ui.MyWebView;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.NetWorkUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.StatisticsUtil;

/**
 * Created by Terry on 2016/2/24.
 */
public class GameActivity extends BaseActivity implements MyWebView.WebViewTouchEvent {

    public static final String TAG = GameActivity.class.getSimpleName();
    private MyWebView mWebView;
    //关闭按钮
    private ImageView mClose;
    private ProgressDialog mProgressDialog;
    private String mURL;
    public static final String GAME_URL = "game_url";
    public static final int ENTER = 0;
    public static final int EXIT = 1;
    private static final String INJECTION_TOKEN = "**injection**";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENTER: {
                    break;
                }
                case EXIT: {
                    IntentUtil.goBackToVideoActivity(GameActivity.this);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        initView();
    }

    @Override
    protected void initSonIntent() {
        super.initSonIntent();
        Intent in = getIntent();
        if (in == null) {
            return;
        }
        mURL = in.getStringExtra(GAME_URL);
    }

    @Override
    protected int onGetEventId() {
        if (mURL.equals(Constant.NEARBY_DISCOUNT_URL)) {
            return StatisticsUtil.APP_AROUNDDISCOUNT;
        } else {
            return StatisticsUtil.H5_GAME;
        }
    }

    @Override
    protected String onGetDesc() {
        if (mURL.equals(Constant.NEARBY_DISCOUNT_URL)) {
            return StatisticsUtil.APP_AROUNDDISCOUNT_DESC;
        } else {
            return StatisticsUtil.H5_GAME_DESC;
        }
    }


    private void initView() {
        mProgressDialog = ProgressDialog.show(this, getString(R.string.waiting), getString(R.string.isloading), true);
        mProgressDialog.setCancelable(true);
        mWebView = (MyWebView) findViewById(R.id.web);
        mClose = (ImageView) findViewById(R.id.game_close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessage(EXIT);
            }
        });
        mWebView.setWebViewTouchEventListener(this);
        Log.v(TAG, "mURL=" + mURL);
        mHandler.sendEmptyMessageDelayed(EXIT, Constant.DELAY_BACK);
        WebSettings sets = mWebView.getSettings();
        sets.setJavaScriptEnabled(true);

        //cache xiong add 20160722
        sets.setDomStorageEnabled(true);
        sets.setAppCacheMaxSize(50 * 1024 * 1024);
        sets.setAppCachePath(getCacheDir().getAbsolutePath());
        sets.setAllowFileAccess(true);
        sets.setAppCacheEnabled(true);
        if (NetWorkUtil.hasNetwork()) {
            sets.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            sets.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        if (mURL.equals(Constant.URL_H5_WANGWANG)) {
            ViewGroup.LayoutParams layoutParams = mWebView.getLayoutParams();
            layoutParams.height= RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.width=530;
            mWebView.setLayoutParams(layoutParams);

            removeAllCookie(mWebView);
            sets.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            sets.setUseWideViewPort(true);
            sets.setLoadWithOverviewMode(true);
        }
        //cache xiong add 20160722

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.v(TAG, "url= " + url);
                view.loadUrl(url);
                return true;
//                if( url.startsWith("http:") || url.startsWith("https:") ) {
//                    return false;
//                }
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressDialog.dismiss();
            }


        });
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());  //在前面加入下载监听器
        mWebView.loadUrl(mURL);

    }

    @Override
    public void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void isWebViewTouch(boolean isTouch) {
        if (isTouch) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(EXIT, Constant.DELAY_BACK);
        }
    }

    private boolean falg = true;

    class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype, long contentLength) {
            Log.v(TAG, "onDownloadStart url=" + url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        QuickUtils.log("GAME-ActivityTag=" + Constant.NEW_FLAG);
        if (mURL.equals(Constant.NEARBY_DISCOUNT_URL)) {
            Constant.NEW_FLAG = Constant.NEARBY_DISCOUNT_URL;
        } else {
            Constant.NEW_FLAG = Constant.URL;
        }
    }

    private void removeAllCookie(WebView webview) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webview.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieSyncManager.sync();
    }

    private void setDis(WebSettings settings) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;

        QuickUtils.toast(mDensity + "");
        if (mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }
    }


}
