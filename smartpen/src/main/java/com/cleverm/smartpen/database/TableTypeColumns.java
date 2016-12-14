package com.cleverm.smartpen.database;

/**
 * Created by Levy on 2015/5/30.
 */
public interface TableTypeColumns extends BaseColumns {

    /**
     * table name
     */
    String TABLE_NAME = "table_type";

    /**
     * indicate table type display name
     * {@code String}
     */
    String NAME = "name";

    /**
     * min capacity
     * {@code Integer}
     */
    String MIN = "minimum";

    /**
     * max capacity
     * {@code Integer}
     */
    String MAX = "maximum";

    /**
     * description of the specific table type
     */
    String DESCRIPTION = "description";
}