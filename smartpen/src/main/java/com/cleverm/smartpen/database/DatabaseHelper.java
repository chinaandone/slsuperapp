package com.cleverm.smartpen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.cleverm.smartpen.modle.Table;
import com.cleverm.smartpen.modle.TableType;
import com.cleverm.smartpen.modle.impl.TableImpl;
import com.cleverm.smartpen.modle.impl.TableTypeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Jimmy on 2015/9/7.
 */
public class DatabaseHelper {

    @SuppressWarnings("unused")
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "cleverm_order.db";


    private static final String CREATE_TABLE_TABLE_TYPE =
        "CREATE TABLE IF NOT EXISTS " + TableTypeColumns.TABLE_NAME + "("
            + TableTypeColumns._ID + " integer primary key, "
            + TableTypeColumns.NAME + " varchar, "
            + TableTypeColumns.MIN + " integer, "
            + TableTypeColumns.MAX + " integer, "
            + TableTypeColumns.DESCRIPTION + " varchar" + ")";

    private static final String CREATE_TABLE_TABLE =
        "CREATE TABLE IF NOT EXISTS " + TableColumns.TABLE_NAME + "("
            + TableColumns._ID + " integer primary key, "
            + TableColumns.TYPE_ID + " integer, "
            + TableColumns.NAME + " varchar" + ")";

    private static final Object mLock = new Object();
    private static DatabaseHelper sInstance;
    private static Vector<String> mTableNames = new Vector<String>();
    private static Vector<String> mCreateTables = new Vector<String>();
    private static int DB_VERSION = 2;

    static {
        mTableNames.add(TableTypeColumns.TABLE_NAME);
        mCreateTables.add(CREATE_TABLE_TABLE_TYPE);

        mTableNames.add(TableColumns.TABLE_NAME);
        mCreateTables.add(CREATE_TABLE_TABLE);
    }

    private SQLiteDatabase mSQLiteDatabase;
    private SqliteHelper mSqliteHelper;

    public DatabaseHelper(Context context) {
        mSqliteHelper = new SqliteHelper(context, DB_NAME, mTableNames,
            mCreateTables, null, DB_VERSION);
        mSQLiteDatabase = mSqliteHelper.getWritableDatabase();
    }

    public static DatabaseHelper getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new DatabaseHelper(context);
                }
            }
        }
        return sInstance;
    }

    public void close() {
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
        }
        if (mSqliteHelper != null) {
            mSqliteHelper.close();
        }
    }

    public void deleteAll(final String tableName) {
        mSQLiteDatabase.delete(tableName, null, null);
    }



    public boolean insertTableType(TableType tableType) {
        ContentValues cv = new ContentValues();
        cv.put(TableTypeColumns._ID, tableType.getId());
        cv.put(TableTypeColumns.NAME, tableType.getName());
        cv.put(TableTypeColumns.MIN, tableType.getMinCapacity());
        cv.put(TableTypeColumns.MAX, tableType.getMaxCapacity());
        cv.put(TableTypeColumns.DESCRIPTION, tableType.getDescription());
        long ret = mSQLiteDatabase.insert(TableTypeColumns.TABLE_NAME, null,
            cv);
        if (ret < 0) {
            return false;
        }
        return true;
    }

    public TableTypeImpl queryTableType(long typeId) {
        Cursor cursor = mSQLiteDatabase.query(TableTypeImpl.TABLE_NAME,
            new String[]{
                TableTypeColumns._ID,
                TableTypeColumns.NAME,
                TableTypeColumns.MIN,
                TableTypeColumns.MAX,
                CuisineColumns.DESCRIPTION},
            TableTypeColumns._ID + "=?", new String[]{String.valueOf(typeId)}, null, null, null);

        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            TableTypeImpl tableType = new TableTypeImpl(
                cursor.getLong(cursor.getColumnIndexOrThrow(TableTypeColumns
                    ._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow
                    (TableTypeColumns.NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(TableTypeColumns
                    .MIN)),
                cursor.getInt(cursor.getColumnIndexOrThrow(TableTypeColumns
                    .MAX)),
                cursor.getString(cursor.getColumnIndexOrThrow(CuisineColumns
                    .DESCRIPTION)));
            return tableType;
        } else {
            return null;
        }
    }

    public List<TableType> obtainAllTableTypes() {
        Cursor cursor = mSQLiteDatabase.query(TableTypeImpl.TABLE_NAME,
            new String[]{
                TableTypeColumns._ID,
                TableTypeColumns.NAME,
                TableTypeColumns.MIN,
                TableTypeColumns.MAX,
                CuisineColumns.DESCRIPTION},
            null, null, null, null, TableTypeColumns.MAX + " ASC");

        if (cursor == null) {
            return null;
        }
        List<TableType> tableTypes = new ArrayList<TableType>();
        while (cursor.moveToNext()) {
            tableTypes.add(new TableTypeImpl(
                cursor.getLong(cursor.getColumnIndexOrThrow(TableTypeColumns
                    ._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow
                    (TableTypeColumns.NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(TableTypeColumns
                    .MIN)),
                cursor.getInt(cursor.getColumnIndexOrThrow(TableTypeColumns
                    .MAX)),
                cursor.getString(cursor.getColumnIndexOrThrow
                    (TableTypeColumns.DESCRIPTION))
            ));
        }
        return tableTypes;
    }

    public boolean insertTable(Table table) {
        ContentValues cv = new ContentValues();
        cv.put(TableColumns._ID, table.getId());
        cv.put(TableColumns.TYPE_ID, table.getTypeId());
        cv.put(TableColumns.NAME, table.getName());
        long ret = mSQLiteDatabase.insert(TableColumns.TABLE_NAME, null, cv);
        if (ret < 0) {
            return false;
        }
        return true;
    }

    public TableImpl queryTable(long tableId) {
        Cursor cursor = mSQLiteDatabase.query(TableColumns.TABLE_NAME,
            new String[]{
                TableColumns._ID,
                TableColumns.TYPE_ID,
                TableColumns.NAME,
            },
            TableColumns._ID + "=?", new String[]{String.valueOf(tableId)},
            null, null, null);

        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            TableImpl table = new TableImpl(
                cursor.getLong(cursor.getColumnIndexOrThrow(TableColumns._ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(TableColumns
                    .TYPE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableColumns
                    .NAME)));
            return table;
        } else {
            return null;
        }
    }

    public List<Table> obtainTablesByType(long tableTypeId) {
        Cursor cursor = mSQLiteDatabase.query(TableColumns.TABLE_NAME,
            new String[]{
                TableColumns._ID,
                TableColumns.TYPE_ID,
                TableColumns.NAME,
            },
            TableColumns.TYPE_ID + "=?", new String[]{String.valueOf
                (tableTypeId)}, null, null,
            TableColumns.NAME + " ASC");

        if (cursor == null) {
            return null;
        }
        List<Table> tables = new ArrayList<Table>();
        while (cursor.moveToNext()) {
            tables.add(new TableImpl(
                cursor.getLong(cursor.getColumnIndexOrThrow(TableColumns._ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(TableColumns
                    .TYPE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableColumns.NAME))
            ));
        }
        return tables;
    }
}
