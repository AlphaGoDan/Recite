package com.example.kimasi.recite;

import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

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


    Integer sql=0;

    Integer finish = 0;



    List<String> list1 = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();

    List<String> list000 = new ArrayList<String>();
    List<String> list111 = new ArrayList<String>();
    List<String> list222 = new ArrayList<String>();
    List<String> list333 = new ArrayList<String>();

    List<String> list11 = new ArrayList<String>();
    List<String> list22 = new ArrayList<String>();

    SharedPreferences shujukupreferences;
    SharedPreferences.Editor shujukueditor;

    public static XmlResourceParser xrp = null;//加载数据资源xml
    String danci = null;
    String fanyi = null;

    String k = "0";
    Integer shujuku_i = 0;
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

        xrp = getResources().getXml(R.xml.youdao);//加载数据资源xml

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        mNavigationDrawerFragment.setUp(  // 启动抽屉
                R.id.navigation_drawer,   //被处理显示隐藏的组件(被控制的抽屉)
                (DrawerLayout) findViewById(R.id.drawer_layout));//drawer-layout是DrawerLayout组件控制抽屉


        dbHelper = MyDatabaseHelper.getInstance(this);//单例
        mDb = dbHelper.getReadableDatabase();//数据库   不存在 则创建  如果存在则打开

        shujukupreferences=getSharedPreferences("shujuku",MODE_PRIVATE);
        shujuku_i=shujukupreferences.getInt("shujuku",0);

    //  dbHelper.deleteDatabase(this); //删除数据库,记得放后面,不然冲突

   //     System.out.println("===onCreate===");
    }

    protected void onStart(){
        super.onStart();
    //    System.out.println("===onStart===");
    }

    protected void onRestart(){
        super.onRestart();
    //    System.out.println("===onRestart===");//从后台恢复,从这里开始,然后onStart,再onResume
    }

    protected void onResume()
    {   super.onResume();
    //    System.out.println("===onResume===");//测试,启动碎片执行到这里,(activity可见界面)
    }

    protected void onPause(){
        super.onPause();
    //    System.out.println("===onPause===");
     }
    protected void onStop(){
        super.onStop();
    //    System.out.println("===onStop===");//执行到这里进入后台,不可见
    }

    protected void onDestroy(){
        super.onDestroy();
        mDb.close();
    //    System.out.println("===onDestroy===");
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
    public boolean onKeyDown(int keyCode,KeyEvent event){//音量键监听,调用碎片的静态函数,翻页
        getCurrentFocus();  //去除音量进度条
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

    @Override  //侧边栏的碎片的列表的点击监听的->对activity的回调
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, p1)//参数1容器的碎片将被取代,  ?这个容器如果多个碎片是否一起取代?
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();//应该是返回这个activity的ActionBar,,
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);  //显示bar的标题
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //初始化菜单
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();//等于返回设置好的这个activity的ActionBar,
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //处理菜单项的点击

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
