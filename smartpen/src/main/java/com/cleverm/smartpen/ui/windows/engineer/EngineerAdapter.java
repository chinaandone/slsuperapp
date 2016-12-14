package com.cleverm.smartpen.ui.windows.engineer;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.util.QuickUtils;

/**
 * Created by xiong,An android project Engineer,on 19/8/2016.
 * Data:19/8/2016  上午 11:53
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class EngineerAdapter extends SimpleAdapter<String> {

    public EngineerAdapter(Context context) {
        super(context);
    }

    @Override
    public View newView(int type, ViewGroup parent) {
        return inflater.inflate(R.layout.util_engineer_adapter, parent, false);
    }

    @Override
    public void bindView(int position, int type, View view) {
        TextView tv = (TextView) view;
        String message = items.get(position);
        int indexOf = message.indexOf(QuickUtils.log_splite);

        SpannableStringBuilder style=new SpannableStringBuilder(message);
        style.setSpan(new ForegroundColorSpan(Color.WHITE),0,indexOf+5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),indexOf+5,message.length(),Spannable.SPAN_MARK_MARK);

        tv.setText(style);
    }
}
