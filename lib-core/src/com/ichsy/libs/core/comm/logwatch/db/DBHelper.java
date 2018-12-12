package com.ichsy.libs.core.comm.logwatch.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	static final String DB_NAME =  "Tester.db";

    static final String TESTER_TABLE_NAME = "info_tester";
	
	static final String _ID = "_id";
	static final String CLASSES_NAME = "classes_name";//分类名称
	static final String CLASSES_CONTENT = "content";//具体内容
	
	public DBHelper(Context context) {
		super(context,DB_NAME, null, 4);
	}
	
	public DBHelper(Context context,int version) {
		super(context,DB_NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TESTER_TABLE_NAME + " (" 
				+ _ID + " integer primary key AUTOINCREMENT,"
				+ CLASSES_NAME + " text, " 
				+ CLASSES_CONTENT + " text ) ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion > oldVersion){
			db.execSQL("drop table if exists "+TESTER_TABLE_NAME);
			onCreate(db);
		}
	}

}
