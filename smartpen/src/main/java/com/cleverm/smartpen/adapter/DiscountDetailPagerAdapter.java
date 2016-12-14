package com.cleverm.smartpen.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 2016/3/1.
 * Data:2016-03-01  18:52
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DiscountDetailPagerAdapter extends PagerAdapter {

    private List<View> lists;


    public DiscountDetailPagerAdapter(List<View> lists){
        this.lists=lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(lists.get(position));
        return lists.get(position);
    }
}
