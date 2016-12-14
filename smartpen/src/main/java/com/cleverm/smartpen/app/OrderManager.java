package com.cleverm.smartpen.app;

import android.content.Context;

import com.cleverm.smartpen.modle.impl.OrderedCuisineImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Jimmy on 2015/9/8.
 */
public class OrderManager {

    @SuppressWarnings("unused")
    private static final String TAG = OrderManager.class.getSimpleName();

    private static final Object mLock = new Object();
    private static OrderManager sInstance = null;

    private Vector<OrderedCuisineImpl> mOrderFoodCache = new Vector<OrderedCuisineImpl>();
    private List<OnOrderListener> mOrderListener = new ArrayList<OnOrderListener>();
    private long mTableId = -1;

    public OrderManager(Context context) {
        //TODO
    }

    public static OrderManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OrderManager(context);
                }
            }
        }
        return sInstance;
    }

    public Vector<OrderedCuisineImpl> getOrderFoodCache() {
        return mOrderFoodCache;
    }

    public void addOrderListener(OnOrderListener listener) {
        if (!mOrderListener.contains(listener)) {
            mOrderListener.add(listener);
        }
    }

    public void removeOrderListener(OnOrderListener listener) {
        mOrderListener.remove(listener);
    }

    private void notifyOrderChanged() {
        for (OnOrderListener listener : mOrderListener) {
            listener.onOrderFoodChanged();
        }
    }

    public int findCuisine(final OrderedCuisineImpl orderedCuisine) {
        for (int i = 0; i < mOrderFoodCache.size(); ++i) {
            if (orderedCuisine.getId() == mOrderFoodCache.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    public void addCuisine(final OrderedCuisineImpl orderedCuisine) {
        final int position = findCuisine(orderedCuisine);
        if (position != -1) {
            increaseCuisineCount(position);
        } else {
            mOrderFoodCache.add(orderedCuisine);
            notifyOrderChanged();
        }
    }

    public void increaseCuisineCount(final int position) {
        mOrderFoodCache.get(position).increaseOrderCount();
        notifyOrderChanged();
    }

    public void decreaseCuisineCount(final int position) {
        mOrderFoodCache.get(position).decreaseOrderCount();
        if (mOrderFoodCache.get(position).getOrderedCount() == 0) {
            mOrderFoodCache.remove(position);
        }
        notifyOrderChanged();
    }

    public int getTotalCuisineCount() {
        if (mOrderFoodCache == null || mOrderFoodCache.isEmpty()) {
            return 0;
        }
        int totalCuisineCount = 0;
        for (OrderedCuisineImpl orderedCuisine : mOrderFoodCache) {
            totalCuisineCount += orderedCuisine.getOrderedCount();
        }
        return totalCuisineCount;
    }

    public int getTotalCuisineCost() {
        if (mOrderFoodCache == null || mOrderFoodCache.isEmpty()) {
            return 0;
        }
        int totalCuisineCost = 0;
        for (OrderedCuisineImpl orderedCuisine : mOrderFoodCache) {
            totalCuisineCost += orderedCuisine.getPrice() * orderedCuisine
                    .getOrderedCount();
        }
        return totalCuisineCost;
    }

    public int getTotalDiscount() {
        return 0;
    }

    public long getTableId() {
        return mTableId;
    }

    public void setTableId(long tableId) {
        mTableId = tableId;
    }

    public interface OnOrderListener {
        void onOrderFoodChanged();
    }
}