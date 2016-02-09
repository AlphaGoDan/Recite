package com.example.kimasi.recite;

import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    TextView d1 = null;
    TextView d2 = null;
    TextView d3 = null;
    TextView d4 = null;
    TextView d5 = null;
    TextView d6 = null;

    TextView f1 = null;
    TextView f2 = null;
    TextView f3 = null;
    TextView f4 = null;
    TextView f5 = null;
    TextView f6 = null;

    TextView number = null;

    ImageButton up = null;
    ImageButton dow = null;
    private Toast mToast;
    Integer i = 0;

    Integer sql=0;

    Integer finish = 0;

    String danci = null;
    String fanyi = null;
    String k = "0";

    List<String> list1 = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();

    List<String> list000 = new ArrayList<String>();
    List<String> list111 = new ArrayList<String>();
    List<String> list222 = new ArrayList<String>();
    List<String> list333 = new ArrayList<String>();

    List<String> list11 = new ArrayList<String>();
    List<String> list22 = new ArrayList<String>();

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //数据库对象
    MyDatabaseHelper dbHelper = null;
    static SQLiteDatabase mDb = null;

    int j = 0;
    int h = 0;//确定数据的读取,不加标记xml解析时候会重复存入数据库


    Fragment p1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,   //被处理显示隐藏的组件(被控制的抽屉)
                (DrawerLayout) findViewById(R.id.drawer_layout));//drawer-layout是DrawerLayout组件控制抽屉

        //数据库   不存在 则创建  如果存在则打开
        dbHelper = MyDatabaseHelper.getInstance(this);
        mDb = dbHelper.getReadableDatabase();

//



           if(sql==0){  //只导入一次数据
               SQLdaoru();//导入数据
               sql++;
          }

        //    SQLchaxun();
//           dbHelper.deleteDatabase(this); //删除数据库,记得放后面,不然冲突
        System.out.println("===onCreate===");
    }

    protected void onStart(){
        super.onStart();
        System.out.println("===onStart===");    }

    protected void onRestart(){
        super.onRestart();
        System.out.println("===onRestart===");//从后台恢复,从这里开始,然后onStart,再onResume
    }

    protected void onResume()
    {   super.onResume();
        System.out.println("===onResume===");//测试,启动碎片执行到这里,(activity可见界面)
    }

    protected void onPause(){
        super.onPause();
        System.out.println("===onPause===");  }
    protected void onStop(){
        super.onStop();
        System.out.println("===onStop===");//执行到这里进入后台,不可见
    }

    protected void onDestroy(){
        super.onDestroy();
        mDb.close();
        System.out.println("===onDestroy===");
    }


    public boolean onKeyUp(int keyCode,KeyEvent event){//加这个可以没有按键音
        getCurrentFocus();
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
        }
        return super.onKeyUp(keyCode,event);
    }
    public boolean onKeyDown(int keyCode,KeyEvent event){//音量键监听,调用碎片的静态函数,判断是碎片1
        getCurrentFocus();
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Fragment1.DownDF();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                Fragment1.UpDF();
                return true;
        }
        return super.onKeyDown(keyCode,event);
    }


    private void SQLdaoru() {  //导入xml资源 ,注意不可重复执行,会重复导入
        //       dbHelper.getReadableDatabase().close();

        XmlResourceParser xrp = getResources().getXml(R.xml.youdao);//加载数据资源xml

        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();

                    if (tagName.equals("word")) {
                        danci = xrp.nextText();
                        j = 1;
                    } else if (tagName.equals("trans")) {
                        fanyi = xrp.nextText();
                        h = 1;
                    }
                    if (j == 1 && h == 1) {
                        mDb.execSQL("insert into dict values(null , ? , ?,?)"
                                , new String[]{danci, fanyi, k});
                        j = 0;
                        h = 0;
                    }

                }
                xrp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void SQLchaxun() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "select * from dict where k like ?",//占位符查询  ,%是适配符,查询符合?的结果集
                new String[]{"%" + 0 + "%"});//查询key为int标记(区分已完成,和未完成)
        while (cursor.moveToNext()) {
            list000.add(cursor.getString(0));
            list111.add(cursor.getString(1));
            list222.add(cursor.getString(2));
            list333.add(cursor.getString(3));
        }
        if (!list111.isEmpty()) {

        }
        for (i = 0; i < list111.size(); i++) {
            System.out.println("数据库 " + list000.get(i) + " 单词:" + list111.get(i) + "  翻译:" + list222.get(i) +
                    "  标记: " + list333.get(i));
        }
        //再加一个导入已完成的
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {//根据position启动新碎片
        switch (position){
            case 0:
                 p1 = Fragment1.newInstance(position);
                break;
            case 1:
                 p1 = Fragment2.newInstance(position);
                break;
            case 2:
                 p1 = Fragment3.newInstance(position);
                break;
        }

   //     Fragment3 p1=Fragment3.newInstance(position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, p1)//第二个参数产生新的碎片,参数一是碎片的布局
                .commit();
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
