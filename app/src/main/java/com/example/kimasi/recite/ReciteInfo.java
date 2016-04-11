package com.example.kimasi.recite;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;

import com.example.kimasi.recite.manager.DataStorage;
import com.example.kimasi.recite.manager.MyDatabaseHelper;
import com.example.kimasi.recite.manager.Pronounce;
import com.example.kimasi.recite.module.Word;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;

/**
 * 本应用的一些共享信息,保存应用要处理的数据,和初始化信息
 * Created by kimasi on 2016/4/7.
 */
public class ReciteInfo {

    private static ReciteInfo reciteInfo;
    private DataStorage dataStorage;              //数据库处理
    private XmlResourceParser xmlResourceParser;   //xml解析

    public static ArrayList<Word> taskWords;       //任务内单词  先从数据库拿出,统一处理好再放回去
    public static ArrayList<Word> passWords;       //通过的单词
    public static ArrayList<Word> allWords;        //所有单词

    private SharedPreferences preferences;         //一些配制本地保存
    private SharedPreferences.Editor editor;

    public static Integer DefaultBrightnessMode = 1; //系统原本的亮度模式
    public static Integer brightness;              //自定义的亮度值
    public static Integer brightnessMode;          //自定义的亮度模式
    public static Integer pronunciation;           //讯飞发音音量值

    public static Integer pageNumber;              //退出前的页码
    public static Integer accomplishNumber = 0;      //完成数

    public Pronounce pronounce;                   //讯飞发音

    public static final String ASC = "_id ASC";       //数据库返回数据时,用来排序
    public static final String DESC = "_id DESC";
    public static String order = ASC;

    public static Boolean register = false;            //注册
    public static String userName;

    private Context context;

    public static String path = "/sdcard/myHead/";     //说是使用->更合适 Context.getFilesDir().getPath()
    public static String headPictureFile = path + "head.jpg";

    private ReciteInfo() {

    }

    public static ReciteInfo getReciteInfo() {
        if (reciteInfo == null) {
            reciteInfo = new ReciteInfo();
        }
        return reciteInfo;
    }

    public void init(Context context) { //放在主启动Activity,初始化 获取之前的配制数据

        this.context = context;
        Bmob.initialize(context, "8c90ef0312bfa9752d4d8ca72ef9ae03");//测试？是否只在主activity放一条key就行？

        xmlResourceParser = context.getResources().getXml(R.xml.youdao);
        MyDatabaseHelper.getInstance(context).setXmlResourceParser(xmlResourceParser);
        dataStorage = DataStorage.getDataStorage(context);
        taskWords = dataStorage.getTaskWords();
        allWords = dataStorage.getAllWorld();   //

        preferences = context.getSharedPreferences("RPreparation", context.MODE_PRIVATE);//保存配制到本地
        brightness = preferences.getInt("brightness", 150);
        brightnessMode = preferences.getInt("brightnessMode", 1);
        pronunciation = preferences.getInt("pronunciation", 80);
        register = preferences.getBoolean("register", false);
        userName = preferences.getString("user_name", "点击登陆");

        pageNumber = preferences.getInt("pageNumber", 0);
        accomplishNumber = preferences.getInt("accomplishNumber", 0);

        pronounce = Pronounce.getPronounce(context);
    }

    public void quitSave() {  //Activity退出时调用,一些配制数据本地化
        editor = preferences.edit();
        editor.putInt("pageNumber", pageNumber);
        editor.putInt("accomplishNumber", accomplishNumber);
        editor.putInt("brightness", brightness);
        editor.putInt("brightnessMode", brightnessMode);
        editor.putInt("pronunciation", pronunciation);
        editor.putBoolean("register", register);
        editor.putString("user_name", userName);
        editor.commit();
        dataStorage.SQLclose();//这个在主Activity关了,在其他Activity处理数据库会报错
    }

    public Context getContext() {
        return context;
    }

    public Pronounce getPronounce() {
        return pronounce;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public void upAllWords() {
        // dataStorage.getAllWorld();
    }

    public ArrayList<Word> getTaskWords() {
        return taskWords;
    }

    public void setTaskWords(ArrayList<Word> taskWords) {
        this.taskWords = taskWords;
    }

    public XmlResourceParser getXmlResourceParser() {
        return xmlResourceParser;
    }

    public void setXmlResourceParser(XmlResourceParser xmlResourceParser) {
        this.xmlResourceParser = xmlResourceParser;
    }

    public Integer getFayin() {
        return pronunciation;
    }

    public void setFayin(Integer pronunciation) {
        this.pronunciation = pronunciation;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getAccomplishNumber() {
        return accomplishNumber;
    }

    public void setAccomplishNumber(Integer accomplishNumber) {
        this.accomplishNumber = accomplishNumber;
    }
}