package com.example.kimasi.recite;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MyDatabaseHelper extends SQLiteOpenHelper
{
    static MyDatabaseHelper mInstance = null;

    public static final String DATABASE_NAME = "xys.db";    /** 数据库名称 **/


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
    private String danci,fanyi,k="0";private int j=0,h=0;

    @Override
	public void onCreate(SQLiteDatabase db)//一个回调,应该是当数据库不存在的时候会自动创建
	{  //当数据库存在的时候不会再创建,可用来初始化数据库里的一些数据
		// 第一次使用数据库时自动建表
		db.execSQL(CREATE_TABLE_SQL);// 执行一条SQL语句
//        System.out.println("导入数据库");
        try {
            while (MainActivity.xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (MainActivity.xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = MainActivity.xrp.getName();

                    if (tagName.equals("word")) {
                        danci = MainActivity.xrp.nextText();
                        j = 1;
                    } else if (tagName.equals("trans")) {
                        fanyi = MainActivity.xrp.nextText();
                        h = 1;
                    }
                    if (j == 1 && h == 1) {
                        db.execSQL("insert into dict values(null , ? , ?,?)"
                                , new String[]{danci, fanyi, k});
                        j = 0;
                        h = 0;
                    }

                }
                MainActivity.xrp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		System.out.println("--------onUpdate Called--------"
				+ oldVersion + "--->" + newVersion);
	}
    public boolean deleteDatabase(Context context){ //删除数据库
        System.out.println("删除数据库");
        return context.deleteDatabase(DATABASE_NAME);
    }
}
