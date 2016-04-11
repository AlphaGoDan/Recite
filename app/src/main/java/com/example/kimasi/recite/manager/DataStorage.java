package com.example.kimasi.recite.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kimasi.recite.ReciteInfo;
import com.example.kimasi.recite.module.Word;

import java.util.ArrayList;

/**
 * 一些应用数据本地获取和存储方法
 * Created by kimasi on 2016/4/7.
 */
public class DataStorage {

    private static DataStorage dataStorage;
    private static SQLiteDatabase mDb;
    private MyDatabaseHelper dbHelper=null;

    private DataStorage(Context context){
         dbHelper=MyDatabaseHelper.getInstance(context);
         mDb=dbHelper.getReadableDatabase();
    }
    public static synchronized DataStorage getDataStorage(Context context){
        if (dataStorage==null){
            dataStorage=new DataStorage(context);
        }
        return dataStorage;
    }

    /**
     * :匹配符合的字母, %为通配符  查询k为0标记,未完成单词
     *  <= _id and _id<= ?",// and k=?",//占位符查询  总数1771
     * @
     * @return 得到数据库任务内的单词,优先级高的
     */
    public ArrayList<Word>  getTaskWords(){
        mDb=dbHelper.getReadableDatabase();
        ArrayList<Word> taskWords =new ArrayList<>();
        Cursor cursor=mDb.rawQuery("select * from dict where plan= ? ", new String[]{"0"});//获取计划内的单词
        while (cursor.moveToNext()){
            Integer index=cursor.getInt(0);
            String en = cursor.getString(1);
            String cn = cursor.getString(2);
            Integer proficiency = cursor.getInt(3);

         //   LogUtils.v2("初始化的时候"+" "+index+" "+en+" ");

            taskWords.add(new Word(en, cn, proficiency, null, null,null,index));
        }
        cursor.close();
        mDb.close();
        return taskWords;
    }

    public ArrayList<Word> getAllWorld(){
        mDb=dbHelper.getReadableDatabase();
        ArrayList<Word> allWords =new ArrayList<>();
        /**
         * 参数1要查询的表名2想要显示的列，若为空则返回所有列3where子句，
         * 声明要返回的行的要求，如果为空则返回表的所有行
         *4( where子句对应的条件值)5分组方式，若为空则不分组6having条件，
         * 若为空则返回全部7排序方式，为空则为默认排序方式（DESC，ASC）
         */
        Cursor cursor=mDb.query("dict",null," ? <= plan",new String[]{"0"},null,null, ReciteInfo.order);//_id 排序,降
        while (cursor.moveToNext()){
            Integer index=cursor.getInt(0);
            String en = cursor.getString(1);
            String cn = cursor.getString(2);
            Integer proficiency = cursor.getInt(3);
            allWords.add(new Word(en, cn, proficiency, null, null,null,index));
        }
        cursor.close();
        mDb.close();
        return allWords;
    }

    public ArrayList<Word> getPassWords(){
        mDb=dbHelper.getReadableDatabase();
        ArrayList<Word> passWords =new ArrayList<>();
        /**
         * 参数1要查询的表名2想要显示的列，若为空则返回所有列3where子句，声明要返回的行的要求，如果为空则返回表的所有行
         *4( where子句对应的条件值)5分组方式，若为空则不分组6having条件，若为空则返回全部7排序方式，为空则为默认排序方式（DESC，ASC）
         */
        Cursor cursor=mDb.query("dict",null," ? <= plan",new String[]{"0"},null,null, ReciteInfo.order);//_id 排序,降
        while (cursor.moveToNext()){
            Integer index=cursor.getInt(0);  //自增索引从1开始的
            String en = cursor.getString(0);
            String cn = cursor.getString(1);
            Integer proficiency = cursor.getInt(2);
            passWords.add(new Word(en, cn, proficiency, null, null,null,index));
        }
        cursor.close();
        mDb.close();
        return passWords;
    }
    public void updateWord(String s){
        mDb=dbHelper.getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("plan","1");
        mDb.update("dict",contentValues," en = ?",new String[]{s});
        mDb.close();
    }

    public void addWord(ContentValues contentValues){
        mDb=dbHelper.getReadableDatabase();
        mDb.insert("dict",null,contentValues);
    }
    public Boolean queryWorld(String s){
        mDb=dbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(    //查询k为0标记,未完成单词
                "select * from dict where en= ? ",     //<= _id and _id<= ?",// and k=?",//占位符查询
                new String[]{s});                   //总数1771
        int i=cursor.getCount();
        cursor.close();
        mDb.close();

        return i<=0;
    }
    public void SQLclose() {
       // mDb.close();  //?
    }
}
