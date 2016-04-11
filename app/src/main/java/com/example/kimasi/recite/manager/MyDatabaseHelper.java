package com.example.kimasi.recite.manager;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static MyDatabaseHelper mInstance = null;
    private XmlResourceParser xmlResourceParser;
    private final Context mContext;

    public static final String DATABASE_NAME = "Recite.db";    /** 数据库名称 **/
    public final String CREATE_TABLE_SQL =
			"create table dict(_id integer primary " +    //自动增长的主键   //index这个名称好像会跟数据库关键字冲突,改成ix;
					"key autoincrement , en , cn, proficiency ,plan ,ix)";//用来创建 ,,表名称dict,括号里是列名,ix是单词的索引,不一定用到
	private MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);//因为单例模式所以要这样
        mContext=context;
	}

    public static synchronized MyDatabaseHelper getInstance(Context context) { //单例模式
        if (mInstance == null) {
            mInstance = new MyDatabaseHelper(context);
        }
        return mInstance;
    }

    public void setXmlResourceParser(XmlResourceParser xmlResourceParser) {
        this.xmlResourceParser = xmlResourceParser;
    }

    /**
     * 一个回调,应该是当数据库不存在的时候会自动创建
     * 当数据库存在的时候不会再创建,可用来初始化数据库里的一些数据
     *  第一次使用数据库时自动建表
     * @param db 数据库
     */
    @Override
	public void onCreate(SQLiteDatabase db)
	{
         String en=null,cn=null,proficiency="0",plan="0",index="0";
        int j=0,h=0;
        db.execSQL(CREATE_TABLE_SQL);//建表
        if (xmlResourceParser!=null){ //初始化一些表数据
            try {
                while (xmlResourceParser.getEventType() != XmlResourceParser.END_DOCUMENT) {
                    if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG) {
                        String tagName = xmlResourceParser.getName();
                        if (tagName.equals("word")) {
                            en = xmlResourceParser.nextText();
                            j = 1;
                        } else if (tagName.equals("trans")) {
                            cn = xmlResourceParser.nextText();
                            h = 1;
                        }
                        if (j == 1 && h == 1) {
                            db.execSQL("insert into dict values(null , ? , ?,?,?,?)" //每个项都要有初始化
                                    , new String[]{en, cn, proficiency,plan,index});
                            j = 0;
                            h = 0;
                        }
                    }
                    xmlResourceParser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

	}

    /**
     * 更新数据库版本是时候调用
     * @param db
     * @param oldVersion
     * @param newVersion
     */
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
