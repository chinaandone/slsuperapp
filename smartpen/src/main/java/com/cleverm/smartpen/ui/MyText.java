package com.cleverm.smartpen.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cleverm.smartpen.app.VideoActivity;

/**
 * Created by Administrator on 2016/3/17.
 */
public class MyText extends TextView implements View.OnClickListener{
    public final static String TAG = MyText.class.getSimpleName();

    private float textLength = 0f;//文本长度
    private float viewWidth = 0f;//控件的长度
    private float step = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标
    private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
    public boolean isStarting = false;//是否开始滚动
    private Paint paint = null;//绘图样式
    private String text = "";//文本内容


    public MyText(Context context) {
        super(context);
        initView();
    }

    public MyText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    private void initView() {
        setOnClickListener(this);
    }


    public void init(WindowManager windowManager) {
        paint = getPaint();
        paint.setColor(Color.WHITE);
        text = getText().toString();
        //measure()方法获取text的长度
        textLength = paint.measureText(text);
        viewWidth = VideoActivity.width;
        if (viewWidth == 0) {
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                viewWidth = display.getWidth();
            }
        }
        step = viewWidth + textLength;
        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_two_text_length = viewWidth + textLength *2f;
        y = getTextSize() + getPaddingTop();
    }


    public void startScroll() {
        isStarting = true;
        invalidate();
    }


    public void stopScroll() {
        isStarting = false;
        invalidate();
    }

    //覆写TextView的onDraw()方法，实现文本滚动显示的效果
    @Override
    public void onDraw(Canvas canvas) {
        /**
         * * drawText()方法：
         * 参数1:要显示的文本
         * 参数2：文本显示的x坐标，TextView的最左端的x坐标是0，最右端的x坐标是TextView.getWidth();
         * 参数3:文本显示的y坐标，TextView的顶端所在的y坐标。
         * 参数4：描绘文本的画笔
         */
        canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        if (!isStarting) {
            return;
        }

        step += VideoActivity.updateSpeed;//速度设置：1-10

        if (step >=temp_view_plus_two_text_length) {
            step = textLength;
        }
        invalidate();
    }

    /**
     * 响应用户点击事件：点击则停止，再点击继续开始滚动
     */
    public void onClick(View v) {

    }
}
