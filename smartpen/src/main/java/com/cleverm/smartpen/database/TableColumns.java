package com.cleverm.smartpen.database;

/**
 * Created by Levy on 2015/5/30.
 */
public interface TableColumns extends BaseColumns {

    /**
     * database table name
     */
    String TABLE_NAME = "restaurant_table";

    /**
     * Table type id column field
     * {@code Integer}
     */
    String TYPE_ID = "type_id";

    /**
     * Table display name column field
     * {@code String}
     */
    String NAME = "name";
}
