package com.cleverm.smartpen.app.webview;

import android.os.Bundle;

import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.StatisticsUtil;

/**
 * Created by alan on 2016/11/30.
 */

public class JJGameActivity extends BasicWebActivity {
    public static String loadUrl = "http://120.77.10.145/jjgame/index.html";
    private boolean ifBack=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.currentActivity = JJGameActivity.this;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWebSetting() {
        super.initWebSetting();
        webView.addJavascriptInterface(this,"JJ");
    }

    @Override
    public void onBack() {
//        super.onBack();
        webView.loadUrl("javascript:if(ifBack()===true){window.JJ.cancelHandler();}");
        if(ifBack){
            mHandler.removeMessages(ReturnToVideoAcvity);
//            mHandler.sendEmptyMessageDelayed(ReturnToVideoAcvity, TIME);
        }else{
            mHandler.sendEmptyMessageDelayed(ReturnToVideoAcvity, TIME);
        }
    }

    @android.webkit.JavascriptInterface
    public void cancelHandler() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(JJGameActivity.this, "js调用了Native函数", Toast.LENGTH_SHORT).show();
                ifBack=true;
                IntentUtil.goBackToVideoActivity(currentActivity);
                mHandler.removeMessages(ReturnToVideoAcvity);
            }
        });
    }

    @android.webkit.JavascriptInterface
    public void tigercount() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(JJGameActivity.this, "js调用了Native函数", Toast.LENGTH_SHORT).show();
                doStatistic(StatisticsUtil.ENTRANCE_JJSTART,StatisticsUtil.ENTRANCE_JJSTART_DESC);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }
}
