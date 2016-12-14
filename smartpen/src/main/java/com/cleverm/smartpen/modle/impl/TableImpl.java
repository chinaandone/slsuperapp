package com.cleverm.smartpen.modle.impl;


import com.cleverm.smartpen.modle.Table;

/**
 * Created by Jimmy on 2015/9/17.
 */
public class TableImpl implements Table {

    @SuppressWarnings("unused")
    private static final String TAG = TableImpl.class.getSimpleName();

    private long mId;
    private long mTypeId;
    private String mName;

    public TableImpl(long id, long typeId, String name) {
        mId = id;
        mTypeId = typeId;
        mName = name;
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public long getTypeId() {
        return mTypeId;
    }

    @Override
    public String getName() {
        return mName;
    }
}
