package com.cleverm.smartpen.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.bean.DiscountAdInfo;
import com.cleverm.smartpen.util.QuickUtils;

import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 2016/3/1.
 * Data:2016-03-01  18:06
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DiscountDetailAdapter extends QuickAdapter<DiscountAdInfo> {


    public DiscountDetailAdapter(Context context,List<DiscountAdInfo> list) {
        super(context, R.layout.adapter_discout_detail, list);
    }

    @Override
    public void convert(ViewHolder viewHolder, DiscountAdInfo item, int position) {
        ImageView ivAdapterImage = viewHolder.getView(R.id.ivAdapterImage);
        QuickUtils.displayImage(QuickUtils.spliceUrl(item.getPictruePath(),item.getQiniuPath()), ivAdapterImage);
    }
}
