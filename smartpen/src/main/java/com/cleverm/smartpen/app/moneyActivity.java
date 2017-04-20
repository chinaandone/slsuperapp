package com.cleverm.smartpen.app;

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
import com.cleverm.smartpen.util.LuckyDrawUtil;
import com.cleverm.smartpen.util.WeakHandler;

/**
 * Created by alan on 2016/11/30.
 */

public class moneyActivity extends Activity {

    private ProgressDialog mProgressDialog;
    private WebView webView;
    private String loadUrl = "http://120.77.10.145/cmbc/index.html";
    private int DelayTime=60*1000;
    public static final int TIME = 60000;
    public static final int GOBack = 200;
    public static final int GOGM = 201;
    private int GOGM_TIME = 2*60000;

    public static final String gmCruzeUrl= "http://e.cn.miaozhen.com/r/k=2040258&p=75fWP&dx=__IPDX__&rt=2&ns=__IP__&ni=__IESID__&v=__LOC__&xa=__ADPLATFORM__&tr=__REQUESTID__&ro=sm&vo=385796fdd&vr=2&o=http%3A%2F%2Fwww.mychevy.com.cn%2Fmychevy_activity%2F1004%3Futm_source%3Dxcr%26utm_medium%3Dxcr%26utm_term%3DSP-CH1700154_HS-201703323_MOB228_72908728%26utm_campaign%3Dxcr";
    public static final String gmCruzeGameUrl = "http://120.77.10.145/gmgame/index.html";
    public static final String cmbcIndexUrl = "http://120.77.10.145/cmbc/index.html";
    public static final String cmbcApplyUrl = "http://120.77.10.145/cmbc/apply.html";
    public String phone = "";
    public int prizeGetId = 0;
    public static final int loadTime = 2;
    private int currentloadTime = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iv_go_money);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
//            if(bundle.getInt("initPage")==2) {
//                loadUrl = "http://120.77.10.145/cmbc/apply.html";
//            }
//
            loadUrl=bundle.getString("url");
            if(loadUrl.equals(gmCruzeUrl)){
                phone=bundle.getString("phone");
                prizeGetId=bundle.getInt("prizegetid");
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
//        Button btn = (Button)findViewById(R.id.btnjava);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String tel = "18551663299";
//                String javascript = "";
////                javascript = javascript + "var btn = document.getElementsByClassName('btnSub')[0];btn.onclick = function(){if(aa()==false){;}else{alert(\"abcedf\");};}";
//                javascript = javascript + "document.getElementsByTagName('select')[0].options[2].selected=true;";
//                javascript = javascript + "document.getElementsByTagName('input')[4].value=13655789632;";
////                javascript = javascript + "document.getElementsByTagName('input')[5].value='上海弘昆汽车销售服务有限公司';";
//                javascript = javascript + "document.getElementById('dealer_id').value=763;";
////                String js = "var newtel = function(){document.getElementsByTagName('input')[4].value="+tel+"}";
//                Log.v("JAVASCRIPT",javascript);
//                webView.loadUrl("javascript:"+javascript);
//            }
//        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100) {
                    Log.v("OnProgressChanged", "OnProgressChanged finish 100%");
                    currentloadTime++;

                    if(currentloadTime>=loadTime){
                        if(phone!=null && phone.length()>0) {
                            String javascript = "document.getElementsByTagName('select')[0].options[2].selected=true;";
    //                        javascript = javascript + "document.getElementsByTagName('input')[4].value=13655789632;";
    //                      javascript = javascript + "document.getElementsByTagName('input')[5].value='上海弘昆汽车销售服务有限公司';";
                            //763=上海弘昆汽车销售服务有限公司；137=上海锦骏汽车销售服务有限公司
                            javascript = javascript + "document.getElementById('dealer_id').value=137;";
                            javascript = javascript + "document.getElementsByTagName('input')[4].value=" + phone+";";
//                            javascript = javascript + "document.getElementsByClassName('required')[2].innerText='';";
//                            javascript = javascript + "document.getElementsByClassName('required')[3].innerText=\"\";";
                            javascript = javascript + "var btn = document.getElementsByClassName('btnSub')[0];btn.onclick = function(){if(aa()==false){;}else{window.GM.submitPhone();}};";
                            webView.loadUrl("javascript:" + javascript);
                        }
                    }
                }
            }
        });
//        webView.loadUrl("http://120.77.10.145/cmbc/index.html");
        webView.loadUrl(loadUrl);
        webView.requestFocusFromTouch();
        // 启用WebView对JavaScript的支持
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.addJavascriptInterface(this,"GM");
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
                mHandler.sendEmptyMessageDelayed(GOBack, TIME);
                mHandler.sendEmptyMessageDelayed(GOGM,GOGM_TIME);
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

        new WeakHandler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        goGMinfo();
                    }
                },GOGM_TIME
        );
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOBack: {
                    onBack();
                    break;
                }
                case GOGM:{
                    //3分钟直接跳到gm的留咨页面
                    goGMinfo();
                    break;
                }
            }
        }
    };
    @Override
    public void onUserInteraction() {
        mHandler.removeMessages(GOBack);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
        super.onUserInteraction();

    }

    @Override
    public void finish() {
        super.finish();
        webView.destroy();
    }

    //游戏内容3分钟后跳转到留咨页面
    private void goGMinfo(){
        webView.loadUrl(gmCruzeUrl);
    }

    @android.webkit.JavascriptInterface
    public void submitPhone() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(moneyActivity.this, "js调用了Native函数", Toast.LENGTH_SHORT).show();
                LuckyDrawUtil.getInstance().inputphone(phone,String.valueOf(prizeGetId));

            }
        });
    }
}
