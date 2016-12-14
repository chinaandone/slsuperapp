package com.cleverm.smartpen.database;

/**
 * Created by Jimmy on 2015/9/8.
 */
public interface CuisineColumns extends BaseColumns {

    /**
     * database table name
     */
    String TABLE_NAME = "cuisine";

    /**
     * cuisine name column field
     * {@code String}
     */
    String NAME = "name";

    /**
     * cuisine pinyin name column field
     * {@code String}
     */
    String NAME_PINYIN = "name_pinyin";

    /**
     * cuisine category column field
     * {@code Integer}
     */
    String CATEGORY = "category";

    /**
     * cuisine pungency column field
     * {@code Integer}
     */
    String PUNGENCY = "pungency";

    /**
     * cuisine price column field
     * {@code Float}
     */
    String PRICE = "price";

    /**
     * cuisine VIP price column field
     * {@code Float}
     */
    String VIP_PRICE = "vip_price";

    /**
     * cuisine unit column field
     * {@code String}
     */
    String UNIT = "unit";

    /**
     * cuisine description column field
     * {@code String}
     */
    String DESCRIPTION = "description";
}