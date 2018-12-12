package com.ichsy.libs.core.comm.logwatch.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ichsy.libs.core.comm.logwatch.InfoBean;

import java.util.ArrayList;

public class DAO {

    private static SQLiteDatabase db;
    private DBHelper dbHelper;

    public DAO(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
            if (db == null) {
                db = dbHelper.getWritableDatabase();
                db = dbHelper.getReadableDatabase();
            }
        }
    }

    /**
     * insert
     *
     * @param name    类型名称
     * @param content 详细数据
     * @return
     */
    public long insert(String name, String content) {
        ContentValues initValues = new ContentValues();
        initValues.put(DBHelper.CLASSES_NAME, name);
        initValues.put(DBHelper.CLASSES_CONTENT, content);
        return db.insert(DBHelper.TESTER_TABLE_NAME, null, initValues);
    }

    /**
     * delete
     */
    public int delete() {
        String sql = DBHelper.TESTER_TABLE_NAME;
        return db.delete(sql, null, null);
    }

    /**
     * delete (on the base of classesName)
     *
     * @param classesName 类型名称
     * @return
     */
    public int delete(String classesName) {
        String sql = DBHelper.TESTER_TABLE_NAME;
        return db.delete(sql, DBHelper.CLASSES_NAME + " = ?", new String[]{classesName});
    }


    public ArrayList<InfoBean> queryCateGory() {
        Cursor cursor;
        ArrayList<InfoBean> mArrayList = new ArrayList<>();

        InfoBean bean = new InfoBean();
        bean.setClassesName("全部");
        mArrayList.add(bean);
        try {
            String sql = "select * from " + DBHelper.TESTER_TABLE_NAME + " GROUP BY " + DBHelper.CLASSES_NAME;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    bean = new InfoBean();
                    bean.setClassesName(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_NAME)));
                    bean.setClassesContent(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_CONTENT)));
                    mArrayList.add(bean);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mArrayList;
    }

    /**
     * query
     * 查询全部
     *
     * @return
     */
    public ArrayList<InfoBean> query() {
        Cursor cursor;
        ArrayList<InfoBean> mArrayList = new ArrayList<InfoBean>();
        try {
            String sql = "select _id," + DBHelper.CLASSES_NAME + "," + DBHelper.CLASSES_CONTENT + " from " + DBHelper.TESTER_TABLE_NAME;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    InfoBean bean = new InfoBean();
                    bean.setClassesName(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_NAME)));
                    bean.setClassesContent(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_CONTENT)));
                    mArrayList.add(bean);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mArrayList;
    }

    public ArrayList<InfoBean> query(int page, int size) {
        Cursor cursor;
        ArrayList<InfoBean> mArrayList = new ArrayList<>();
        try {
            String sql = "select _id," + DBHelper.CLASSES_NAME + "," + DBHelper.CLASSES_CONTENT + " from " + DBHelper.TESTER_TABLE_NAME + " ORDER BY _id DESC" + " limit " + size + " offset " + page;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    InfoBean bean = new InfoBean();
                    bean.setClassesName(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_NAME)));
                    bean.setClassesContent(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_CONTENT)));
                    mArrayList.add(bean);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mArrayList;
    }

    /**
     * 根据分类查询
     *
     * @param className 分类名称
     * @return
     */
    public ArrayList<InfoBean> query(String className, int page, int size) {
        Cursor cursor;
        ArrayList<InfoBean> mArrayList = new ArrayList<>();
        try {
            cursor = db.query(DBHelper.TESTER_TABLE_NAME, new String[]{DBHelper.CLASSES_NAME, DBHelper.CLASSES_CONTENT}, DBHelper.CLASSES_NAME + "=? " + " ORDER BY _id DESC limit " + size + " offset " + page, new String[]{className}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    InfoBean bean = new InfoBean();
                    bean.setClassesName(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_NAME)));
                    bean.setClassesContent(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_CONTENT)));
                    mArrayList.add(bean);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mArrayList;
    }


    /**
     * 根据分类查询
     *
     * @param className 分类名称
     * @return
     */
    public ArrayList<InfoBean> query(String className) {
        Cursor cursor;
        ArrayList<InfoBean> mArrayList = new ArrayList<>();
        try {
            cursor = db.query(DBHelper.TESTER_TABLE_NAME, new String[]{DBHelper.CLASSES_NAME, DBHelper.CLASSES_CONTENT}, DBHelper.CLASSES_NAME + "=?", new String[]{className}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    InfoBean bean = new InfoBean();
                    bean.setClassesName(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_NAME)));
                    bean.setClassesContent(cursor.getString(cursor.getColumnIndex(DBHelper.CLASSES_CONTENT)));
                    mArrayList.add(bean);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mArrayList;
    }

}
