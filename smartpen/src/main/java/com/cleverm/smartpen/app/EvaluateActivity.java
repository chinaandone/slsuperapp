package com.cleverm.smartpen.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.Constant;
import com.cleverm.smartpen.util.IntentUtil;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.RememberUtil;
import com.cleverm.smartpen.util.StatisticsUtil;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by 95 on 2016/2/3.
 */
public class EvaluateActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = EvaluateActivity.class.getSimpleName();

    private View mRoot;
    private ImageView mClose;
    private Button mSubmit;
    private RatingBar mgeneralEvaluationRatingBar, mtaste, mService, mEnvirment;
    private TextView mgeneralEvaluation, mtasteText, mServiceText, mEnvirmentText;
    private EditText meatInput, mequipmentInput;

    public static final int GOBack = 200;
    public static final int TIME = Constant.DELAY_BACK;

    private String mgeneralEvaluationValue;
    private String mtasteTextValue;
    private String mServiceTextValue;
    private String mEnvirmentTextValue;
    private String meatInputValue;
    private String mequipmentInputValue;
    public static final int EVALUATE_1 = 1;
    public static final int EVALUATE_2 = 2;
    public static final int EVALUATE_3 = 3;
    public static final int EVALUATE_4 = 4;
    public static final int EVALUATE_5 = 5;
    public static final String SELECTEDTABLEID = "SelectedTableId";
    public static final String ORGID ="OrgID";
    public static final String CLIENTID ="clientId";
    public static final String URL = Constant.DDP_URL+"/api/api/v10/evaluation/save";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOBack: {
                    Log.v(TAG, "come hand====");
                    mHandler.removeCallbacksAndMessages(null);
                    HideInputFromWindow();
                    IntentUtil.goBackToVideoActivity(EvaluateActivity.this);
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_activity);
        init();
    }

    @Override
    protected int onGetEventId() {
        return StatisticsUtil.SERVICE_EVALUATE;
    }

    @Override
    protected String onGetDesc() {
        return StatisticsUtil.SERVICE_EVALUATE_DESC;
    }


    private void init() {
        mClose = (ImageView) findViewById(R.id.service_close);
        mClose.setOnClickListener(this);
        mSubmit = (Button) findViewById(R.id.other_confirm);
        mSubmit.setOnClickListener(this);

        mgeneralEvaluationRatingBar = (RatingBar) findViewById(R.id.general_evaluation_RatingBar);
        mgeneralEvaluationRatingBar.setRating(0f);
        mgeneralEvaluationRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                System.out.println("ratingBar=" + v);
                setRanbarText(v, mgeneralEvaluation);
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, TIME);
            }
        });

        mtaste = (RatingBar) findViewById(R.id.taste_RatingBar);
        mtaste.setRating(0f);
        mtaste.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                System.out.println("ratingBar=" + v);
                setRanbarText(v, mtasteText);
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, TIME);
            }
        });

        mService = (RatingBar) findViewById(R.id.service_RatingBar);
        mService.setRating(0f);
        mService.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                System.out.println("ratingBar=" + v);
                setRanbarText(v, mServiceText);
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, TIME);
            }
        });
        mEnvirment = (RatingBar) findViewById(R.id.huanjing_RatingBar);
        mEnvirment.setRating(0f);
        mEnvirment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                setRanbarText(v, mEnvirmentText);
                mHandler.removeMessages(GOBack);
                mHandler.sendEmptyMessageDelayed(GOBack, TIME);
            }
        });

        mgeneralEvaluation = (TextView) findViewById(R.id.general_evaluation_RatingBar_text);
        mgeneralEvaluation.setText("");
        mtasteText = (TextView) findViewById(R.id.taste_RatingBar_text);
        mtasteText.setText("");
        mServiceText = (TextView) findViewById(R.id.service_RatingBar_text);
        mServiceText.setText("");
        mEnvirmentText = (TextView) findViewById(R.id.huanjing_RatingBar_text);
        mEnvirmentText.setText("");
        meatInput = (EditText) findViewById(R.id.eat_input);
        meatInput.addTextChangedListener(watcher);
        mequipmentInput = (EditText) findViewById(R.id.equipment_input);
        mequipmentInput.addTextChangedListener(watcher);

        mHandler.removeMessages(GOBack);
        mHandler.sendEmptyMessageDelayed(GOBack, TIME);
    }


    @Override
    public void onPause() {
        super.onPause();
        mgeneralEvaluationRatingBar.setRating(0f);
        mtaste.setRating(0f);
        mService.setRating(0f);
        mEnvirment.setRating(0f);

        mgeneralEvaluation.setText("");
        mtasteText.setText("");
        mServiceText.setText("");
        mEnvirmentText.setText("");

        meatInput.setText("");
        mequipmentInput.setText("");
        mHandler.removeMessages(GOBack);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meatInput.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mequipmentInput.getWindowToken(), 0);
        mHandler.removeCallbacksAndMessages(null);
        MobclickAgent.onEvent(this, "EVALUATE");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_close: {
                mHandler.sendEmptyMessage(GOBack);
                break;
            }
            case R.id.other_confirm: {
                if(checkInput()){
                    insertData();
                    submit();
                    mHandler.sendEmptyMessage(GOBack);
                    //统计点击量
                    MobclickAgent.onEvent(this, "Click");
                    Toast.makeText(this, getString(R.string.service_submit), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, getString(R.string.service_submit_empty), Toast.LENGTH_LONG).show();
                }

                break;
            }
        }
    }

    private boolean checkInput() {
        if(mgeneralEvaluationRatingBar.getRating()==0.0f&&
                mtaste.getRating()==0.0f&&
                    mService.getRating()==0.0f&&
                        mEnvirment.getRating()==0.0f&&
                            (meatInput.getText().toString().trim().equals(""))&&
                                (mequipmentInput.getText().toString().trim().equals(""))){
            return false;
        }
        return true;
    }

    private void submit() {
        mgeneralEvaluationValue = mgeneralEvaluation.getText().toString();
        mtasteTextValue = mtasteText.getText().toString();
        mServiceTextValue = mServiceText.getText().toString();
        mEnvirmentTextValue = mEnvirmentText.getText().toString();
        meatInputValue = meatInput.getText().toString();
        mequipmentInputValue = mequipmentInput.getText().toString();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("generalEvaluation", mgeneralEvaluationValue);
        map.put("taste", mtasteTextValue);
        map.put("service", mServiceTextValue);
        map.put("Envirment", mEnvirmentTextValue);
        map.put("eatInput", meatInputValue);
        map.put("equipmentInput", mequipmentInputValue);
        map.put("tableId", RememberUtil.getLong(SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT)+"");
        map.put("orgId", RememberUtil.getString(ORGID, ""));
        map.put("clientId", RememberUtil.getString(CLIENTID, ""));
        map.put("time", df.format(new Date()));
        Gson gson = new Gson();
        String gsonData = gson.toJson(map);
        Log.v(TAG, "gsonData=" + gsonData);

        sendToService();
    }

    private void sendToService() {
        int feelWhole = getRatingBarValue(mgeneralEvaluationRatingBar.getProgress());
        int feelFlavor = getRatingBarValue(mtaste.getProgress());
        int feelService = getRatingBarValue(mService.getProgress());
        int feelEnvironment = getRatingBarValue(mEnvirment.getProgress());
        Log.v(TAG,"mgeneralEvaluationRatingBar.getNumStars()="+mgeneralEvaluationRatingBar.getNumStars()+" feelWhole="+feelWhole +"  feelFlavor="+feelFlavor+" feelService="+feelService+" feelEnvironment="+feelEnvironment);
        Log.v(TAG,"tableId="+RememberUtil.getLong(SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT)+""+" orgId="+RememberUtil.getString(ORGID, " ")+" clientId="+RememberUtil.getString(CLIENTID, " "));
        OkHttpUtils.post()
                .url(URL)
                .addParams("deviceRemark", mequipmentInputValue)
                .addParams("feelEnvironment",feelEnvironment+"")
                .addParams("feelFlavor",feelFlavor+"")
                .addParams("feelService",feelService+"")
                .addParams("feelWhole",feelWhole+"")
                .addParams("mealsRemark",meatInputValue)
                .addParams("tableId", RememberUtil.getLong(SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT)+"")
                .addParams("orgId", RememberUtil.getString(ORGID, " "))
                .addParams("clientId", RememberUtil.getString(CLIENTID, " "))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.v(TAG, "Exception=" + e.toString());
                    }

                    @Override
                    public void onResponse(String s) {
                        Log.v(TAG, "onResponse=" + s);
                    }
                });
    }


    private int getRatingBarValue(int NumStars) {
        Log.v(TAG,"NumStars="+NumStars);
        int value = 0;
        switch (NumStars) {
            case EVALUATE_1: {
                value = 20;
                break;
            }
            case EVALUATE_2: {
                value = 40;
                break;
            }
            case EVALUATE_3: {
                value = 60;
                break;
            }
            case EVALUATE_4: {
                value = 80;
                break;
            }
            case EVALUATE_5: {
                value = 100;
                break;
            }
            default: {
                value = 0;
                break;
            }
        }
        Log.v(TAG,"NumStars value="+value);
        return value;
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mHandler.removeMessages(GOBack);
            mHandler.sendEmptyMessageDelayed(GOBack, TIME);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            mHandler.removeMessages(GOBack);
            mHandler.sendEmptyMessageDelayed(GOBack, TIME);

        }

        @Override
        public void afterTextChanged(Editable s) {
            mHandler.removeMessages(GOBack);
            mHandler.sendEmptyMessageDelayed(GOBack, TIME);

        }
    };

    private void setRanbarText(float value, TextView textView) {
        int code = (int) value;
        Log.v(TAG, "code=" + code);
        switch (code) {
            case 0: {
                textView.setText("");
                break;
            }
            case 1: {
                textView.setText(getString(R.string.good1));
                break;
            }
            case 2: {
                textView.setText(getString(R.string.good2));
                break;
            }
            case 3: {
                textView.setText(getString(R.string.good3));
                break;
            }
            case 4: {
                textView.setText(getString(R.string.good4));
                break;
            }
            case 5: {
                textView.setText(getString(R.string.good5));
                break;
            }
        }
    }

    /**
     * 隐藏输入键盘
     */
    private void HideInputFromWindow(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meatInput.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mequipmentInput.getWindowToken(), 0);
    }

    private void insertData(){
        try {
            String text1 = meatInput.getText().toString().trim();
            String text2 = mequipmentInput.getText().toString().trim();
            String desc = StatisticsUtil.OTHER_COMMENT_SUBMIT_DESC +"/服务评价："+ text1 + "/设备评价："+text2;
            String desc2 ;
            int length = desc.length();
            if(length>90){
                desc2 = desc.substring(0,90);
            }else{
                desc2 = desc.substring(0,length);
            }
            //统计
            StatisticsUtil.getInstance().insert(StatisticsUtil.OTHER_COMMENT_SUBMIT, desc2);
        } catch (Exception e) {
            e.printStackTrace();
            //统计
            StatisticsUtil.getInstance().insert(StatisticsUtil.OTHER_COMMENT_SUBMIT, StatisticsUtil.OTHER_COMMENT_SUBMIT_DESC);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}