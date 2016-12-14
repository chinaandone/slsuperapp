package com.cleverm.smartpen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cleverm.smartpen.util.collections.ListUtils;

import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 26/4/2016.
 * Data:26/4/2016  上午 12:46
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<Integer> imageIdList;

    private int           size;
    private boolean       isInfiniteLoop;

    public ImagePagerAdapter(Context context, List<Integer> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = ListUtils.getSize(imageIdList);
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(imageIdList);
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        holder.imageView.setImageResource(imageIdList.get(getPosition(position)));
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
