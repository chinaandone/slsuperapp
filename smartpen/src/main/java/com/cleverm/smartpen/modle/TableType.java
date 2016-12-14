package com.cleverm.smartpen.modle;


import com.cleverm.smartpen.database.TableTypeColumns;

/**
 * Created by Levy on 2015/5/29.
 */
public interface TableType extends TableTypeColumns {

    long getId();

    String getName();

    int getMinCapacity();

    int getMaxCapacity();

    String getDescription();
}
