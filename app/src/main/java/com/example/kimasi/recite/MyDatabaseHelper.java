package com.example.kimasi.recite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper
{
    static MyDatabaseHelper mInstance = null;

    /** 数据库名称 **/
    public static final String DATABASE_NAME = "xys.db";

    final String CREATE_TABLE_SQL =
			"create table dict(_id integer primary " +    //自动增长的主键
					"key autoincrement , word , detail,k)";//用来创建 ,,表名称dict,括号里是列名
	public MyDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, 1);//因为单例模式所以要这样
	}

    static synchronized MyDatabaseHelper getInstance(Context context) { //单例模式
        if (mInstance == null) {
            mInstance = new MyDatabaseHelper(context);
        }
        return mInstance;
    }
    @Override
	public void onCreate(SQLiteDatabase db)//一个回调,应该是当数据库不存在的时候会自动创建
	{
		// 第一次使用数据库时自动建表
		db.execSQL(CREATE_TABLE_SQL);// 执行一条SQL语句
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		System.out.println("--------onUpdate Called--------"
				+ oldVersion + "--->" + newVersion);
	}
    public boolean deleteDatabase(Context context){ //删除数据库
        return context.deleteDatabase(DATABASE_NAME);
    }
}
