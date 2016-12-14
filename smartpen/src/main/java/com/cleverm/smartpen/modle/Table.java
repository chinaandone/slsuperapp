package com.cleverm.smartpen.modle;


import com.cleverm.smartpen.database.TableTypeColumns;

/**
 * Created by Jimmy on 2015/9/17.
 */
public interface Table extends TableTypeColumns {

    long getId();

    long getTypeId();

    String getName();
}
