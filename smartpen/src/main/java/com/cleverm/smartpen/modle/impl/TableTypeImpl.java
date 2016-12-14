package com.cleverm.smartpen.modle.impl;


import com.cleverm.smartpen.modle.TableType;

/**
 * Created by levy on 2015/6/22.
 */
public class TableTypeImpl implements TableType {

    @SuppressWarnings("unused")
    private static final String TAG = TableTypeImpl.class.getSimpleName();

    private long mId;
    private String mName;
    private int mMaxCapacity;
    private int mMinCapacity;
    private String mDescription;

    public TableTypeImpl(long id, String name, int minCapacity, int maxCapacity,
                         String description) {
        mId = id;
        mName = name;
        mMinCapacity = minCapacity;
        mMaxCapacity = maxCapacity;
        mDescription = description;
    }

    @Override
    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int getMinCapacity() {
        return mMinCapacity;
    }

    public void setMinCapacity(int minCapacity) {
        mMinCapacity = minCapacity;
    }

    @Override
    public int getMaxCapacity() {
        return mMaxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        mMaxCapacity = maxCapacity;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
