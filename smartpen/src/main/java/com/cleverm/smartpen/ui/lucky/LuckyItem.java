package com.cleverm.smartpen.ui.lucky;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.LuckyDrawUtil;
import com.cleverm.smartpen.util.QuickUtils;

/**
 * Created by xiong,An android project Engineer,on 29/8/2016.
 * Data:29/8/2016  下午 03:10
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class LuckyItem extends RelativeLayout{


    ImageView mItemImage;
    TextView mItemText;

    public LuckyItem(Context context) {
        this(context, null);
    }

    public LuckyItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        RelativeLayout.inflate(getContext(), R.layout.util_lucky_item, this);
        mItemImage = (ImageView) findViewById(R.id.util_lucky_item_image);
        mItemText = (TextView) findViewById(R.id.util_lucky_item_text);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mItemImage.setSelected(selected);
    }

    public void setImageResource(int resId){
        if(mItemImage!=null){
            mItemImage.setImageResource(resId);
        }
    }

    public void setImageBitmap(Bitmap bitmap){
        if(mItemImage!=null){
            mItemImage.setImageBitmap(bitmap);
        }
    }

    public void setImageUrl(String url){
        QuickUtils.displayImage(LuckyDrawUtil.getInstance().URL_SPLITE_IMAGE+url, mItemImage);
    }


    public void setImageText(String text){
        if(mItemText!=null){
            mItemText.setText(text);
        }
    }


}
