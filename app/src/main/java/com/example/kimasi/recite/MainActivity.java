package com.example.kimasi.recite;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    public static XmlResourceParser xrp = null;//加载数据资源xml

    static SpeechSynthesizer mTts; //讯飞语音模块,在线解析
    Toast mToast;

    //数据库对象
    MyDatabaseHelper dbHelper = null;
    static SQLiteDatabase mDb = null;

    Fragment p1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=56b0437d");//初始化id

        mTts= SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);//!!不设置监听可能会失败
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");           //设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");          //设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端

        View view=findViewById(R.id.drawer_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //系统版本,容错
            setTranslucentStatus(true);


        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.statusbar_bg);//通知栏所需颜色
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
 //       view.setPadding(0,150,0,0);//(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
        }
        xrp = getResources().getXml(R.xml.youdao);//加载数据资源xml

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        mNavigationDrawerFragment.setUp(  // 启动抽屉
                R.id.navigation_drawer,   //被处理显示隐藏的组件(被控制的抽屉)
                (DrawerLayout) findViewById(R.id.drawer_layout));//drawer-layout是DrawerLayout组件控制抽屉


        dbHelper = MyDatabaseHelper.getInstance(this);//单例
        mDb = dbHelper.getReadableDatabase();//数据库   不存在 则创建  如果存在则打开


    //  dbHelper.deleteDatabase(this); //删除数据库,记得放后面,不然冲突

   //     System.out.println("===onCreate===");
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags=bits; //&= ~bits;
       }

       win.setAttributes(winParams);//定义窗口属性
    }
    private InitListener mTtsInitListener = new InitListener() {
        @Override  //讯飞发音初始化监听
        public void onInit(int code) {
            Log.d("类初始化失败", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                mToast.setText("初始化失败,错误码："+code);
                mToast.show();
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

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
        setScreenMode(1);
        mDb.close();
    //    System.out.println("===onDestroy===");
    }

    /**
     * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private void setScreenMode(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
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
        //       actionBar.setBackgroundDrawable(drawable);
                 p1 = Fragment1.newInstance(position);
                break;
            case 1:

                 p1 = Fragment2.newInstance(position);
                break;
            case 2:
            //    drawable=new ColorDrawable(R.color.bar3);
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
        Resources resources=getResources();
    //    ColorDrawable drawable=new ColorDrawable(R.color.bar2);
        Drawable drawable=resources.getDrawable(R.drawable.baraa);
        actionBar.setBackgroundDrawable(drawable);

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

        if (id == R.id.action_example) {//设置
            Intent intent=new Intent(this,setActivity.class);
           startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
