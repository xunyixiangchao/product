package com.ichsy.libs.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.DataBaseHelper;
import com.ichsy.libs.core.comm.utils.GsonHelper;

import java.util.HashMap;

/**
 * SQL 数据库的provider
 * Created by liuyuhang on 2016/11/8.
 */

public class SqlProvider extends BaseProvider {
    private String tableName = "default_table";

    private final String
            ITEM_KEY = "cache_key",
            ITEM_VALUE = "cache_content",
            ITEM_UID = "user_id";

    /**
     * 选择一个表，如果没有会创建，如果不选择会存储在默认表中
     *
     * @param tableName
     * @return
     */
    public SqlProvider selectTable(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * 判断表是否存在
     *
     * @param tableName
     * @return
     */
    private boolean isTableExists(String tableName) {
        Cursor cursor = getDataBaseHelper().rawQuery("select name from sqlite_master where type='table';", null);
        while (cursor.moveToNext()) {
            //遍历出表名
            String name = cursor.getString(0);
            if (name.equals(tableName)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    /**
     * 获取或者创建当前使用的表
     *
     * @return
     */
    private String getTableName() {
        if (!isTableExists(tableName)) {
            //创建表SQL语句
            String stu_table = "create table " + tableName + "" +
                    "(_id integer primary key autoincrement, " +
                    ITEM_KEY + " text, " +
                    ITEM_VALUE + " text, " +
                    ITEM_UID + " text)";
            //执行SQL语句
            getDataBaseHelper().execSQL(stu_table);
        }
        return tableName;
    }

    private SQLiteDatabase getDataBaseHelper() {
        return DataBaseHelper.getInstance().getDataBaseHelper(getContext());
    }

    /**
     * 增
     *
     * @param key
     * @param value
     * @return 是否成功
     */
    @Override
    protected boolean add(String key, String value) {
        ContentValues cValue = new ContentValues();
        cValue.put(ITEM_KEY, key);
        cValue.put(ITEM_VALUE, value);
        cValue.put(ITEM_UID, getUid());
        getDataBaseHelper().insert(getTableName(), null, cValue);
        return false;
    }

    /**
     * 删
     *
     * @param key
     * @return 是否成功
     */
    @Override
    protected boolean delete(String key) {
        return false;
    }

    /**
     * 改
     *
     * @param key
     * @param value
     * @return 是否成功
     */
    @Override
    protected boolean update(String key, String value) {
        ContentValues cValue = new ContentValues();
//        cValue.put("cache_key", key);
        cValue.put(ITEM_VALUE, value);
//        cValue.put("user_id", getUid());
        getDataBaseHelper().update(getTableName(), cValue, ITEM_KEY + "=? and " + ITEM_UID + "=?", new String[]{key, getUid()});

        return false;
    }

    /**
     * 查
     *
     * @param key
     * @return 获取的数据
     */
    @Override
    protected String find(String key) {
        Cursor cacheCursor;
        if (TextUtils.isEmpty(key)) {
            //如果key为空，表示查询所有内容
            cacheCursor = getDataBaseHelper().query(getTableName(), new String[]{ITEM_KEY, ITEM_VALUE}, ITEM_UID + "=?", new String[]{getUid()}, null, null, null);

            if (cacheCursor.getCount() > 0) {
                HashMap<String, String> map = new HashMap<>();

                while (cacheCursor.moveToNext()) {
                    String cacheKey = cacheCursor.getString(0);
                    String cacheContent = cacheCursor.getString(1);
                    map.put(cacheKey, cacheContent);
                }
                cacheCursor.close();
                return GsonHelper.build().toJson(map);
            }
        } else {
            cacheCursor = getDataBaseHelper().query(getTableName(), new String[]{ITEM_KEY, ITEM_VALUE}, ITEM_KEY + "=? and " + ITEM_UID + "=?", new String[]{key, getUid()}, null, null, null);

            if (cacheCursor.getCount() > 0) {
                cacheCursor.moveToFirst();
                String result = cacheCursor.getString(1);
                cacheCursor.close();
                return result;
            }


        }
        cacheCursor.close();

        return null;
    }


//    public void close() {
//        SQLiteDatabase dataBaseHelper = getDataBaseHelper();
//        if (null != dataBaseHelper) {
//            dataBaseHelper.close();
//        }
//    }
}
