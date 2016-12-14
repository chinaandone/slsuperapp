package com.cleverm.smartpen.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

/**
 * Created by Jimmy on 2015/9/7.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    private Vector<String> mTableNames = new Vector<String>();
    private Vector<String> mCreateDBSqls = new Vector<String>();

    public SqliteHelper(Context context, String name, Vector<String> tableNames,
                        Vector<String> createDBs, CursorFactory factory, int
                            version) {
        super(context, name, factory, version);
        mTableNames = tableNames;
        mCreateDBSqls = createDBs;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < mCreateDBSqls.size(); i++) {
            db.execSQL(mCreateDBSqls.elementAt(i));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * 暂时将这里代码注释掉
         */
        /* for (int i = 0; i < mCreateDBSqls.size(); i++) {
            db.execSQL("DROP TABLE IF EXISTS " + mTableNames.elementAt(i));
        }
        onCreate(db);*/
    }
}
