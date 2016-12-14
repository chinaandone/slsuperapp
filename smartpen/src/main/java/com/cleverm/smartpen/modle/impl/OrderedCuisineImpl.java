package com.cleverm.smartpen.modle.impl;


import com.cleverm.smartpen.modle.OrderedCuisine;

/**
 * Created by Jimmy on 2015/9/8.
 */
public class OrderedCuisineImpl extends CuisineImpl implements OrderedCuisine {

    @SuppressWarnings("unused")
    public static final String TAG = OrderedCuisineImpl.class.getSimpleName();

    private int orderedCount;
    private String remarks;

    public OrderedCuisineImpl() {
    }

    public OrderedCuisineImpl(long id, String name, String namePinyin, int category, int pungency, int price, int
        vipPrice, String unit, String description, int orderedCount, String remarks) {
        super(id, name, namePinyin, category, pungency, price, vipPrice, unit, description);
        this.orderedCount = orderedCount;
        this.remarks = remarks;
    }

    @Override
    public int getOrderedCount() {
        return orderedCount;
    }

    public void setOrderedCount(final int orderedCount) {
        this.orderedCount = orderedCount;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public void increaseOrderCount() {
        ++orderedCount;
    }

    public void decreaseOrderCount() {
        if (orderedCount > 0) {
            --orderedCount;
        }
    }
}