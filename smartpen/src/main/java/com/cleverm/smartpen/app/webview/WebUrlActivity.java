package com.cleverm.smartpen.app.webview;

import android.os.Bundle;

/**
 * Created by regis on 2017/6/12.
 */

public class WebUrlActivity extends BasicWebActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.currentActivity = WebUrlActivity.this;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
