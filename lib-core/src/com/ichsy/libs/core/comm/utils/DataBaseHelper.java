package com.ichsy.libs.core.comm.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库helper
 * Created by liuyuhang on 2016/11/8.
 */

public class DataBaseHelper {
    private static DataBaseHelper instance;

    private final String SQL_FILE_NAME = "dgb_database.db";

    private SQLiteDatabase databaseHelper;

    public static DataBaseHelper getInstance() {
        if (null == instance) {
            instance = new DataBaseHelper();
        }
        return instance;
    }

    /**
     * 获取DataBseHelper
     *
     * @param context
     * @return
     */
    public SQLiteDatabase getDataBaseHelper(Context context) {
        if (null == databaseHelper) {
            databaseHelper = context.openOrCreateDatabase(SQL_FILE_NAME, Context.MODE_PRIVATE, null);
        }
        return databaseHelper;
    }

}
