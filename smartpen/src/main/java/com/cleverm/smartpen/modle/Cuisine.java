package com.cleverm.smartpen.modle;


import com.cleverm.smartpen.database.CuisineColumns;

/**
 * Created by Jimmy on 2015/9/7.
 */
public interface Cuisine extends CuisineColumns {

    long getId();

    String getName();

    String getNamePinyin();

    int getCategory();

    int getPungency();

    int getPrice();

    int getVipPrice();

    String getUnit();

    String getDescription();
}
