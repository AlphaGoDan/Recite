package com.example.kimasi.recite.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.example.kimasi.recite.aaatools.LogUtils;
import com.example.kimasi.recite.game.manager.GameManager;


public class GameActivity extends Activity {

    private GameSurface surface;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
    @Override
    protected void onResume() {
        super.onResume();
        init();		//初始化
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){

        }
    }

    private void init(){

        GamingInfo.clearGameInfo();
        GamingInfo.getGamingInfo().setGaming(true);
        GamingInfo.getGamingInfo().setActivity(this);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        GamingInfo.getGamingInfo().setScreenWidth(dm.widthPixels);//屏幕宽高
        GamingInfo.getGamingInfo().setScreenHeight(dm.heightPixels);

        surface = new GameSurface(this);
        GamingInfo.getGamingInfo().setSurface(surface);
        this.setContentView(surface);

        GameManager.getGameManager().init();
        GameManager.getGameManager().getAllBubble1().clear();
    }
    @Override
    protected void onPause() {
        //
        GameManager.getGameManager().stop();
        //
        GamingInfo.clearGameInfo();
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(GameManager.getGameManager().isIniting()){
            return super.onTouchEvent(event);
        }
        //
        if(event.getAction()==MotionEvent.ACTION_DOWN){

            TouchBubble.touch(event.getX(),event.getY());
            LogUtils.v2("触摸"+event.getX()+"++++ "+event.getY());
            //
 //           if(LayoutManager.getLayoutManager().onClick(event.getRawX(), event.getRawY())){
       //         return true;
        //    }
            //
      //      CannonManager.getCannonManager().shot(event.getRawX(), event.getRawY());
            return true;
        }
        return super.onTouchEvent(event);
    }
}









/*    // 定义主布局内的容器：FrameLayout
    public static FrameLayout mainLayout = null;
    // 主布局的布局参数
    public static FrameLayout.LayoutParams mainLP = null;
    // 定义资源管理的核心类
    public static Resources res = null;
    public static GameActivity mainActivity = null;
    public static FishView fishView=null;
    // 定义成员变量记录游戏窗口的宽度、高度
    public static int windowWidth;
    public static int windowHeight;
    // 游戏窗口的主游戏界面
    public static GameView mainView = null;
    // 播放背景音乐的MediaPlayer
    private MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game);

        mainActivity = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metric = new DisplayMetrics();
        // 获取屏幕高度、宽度
        getWindowManager().getDefaultDisplay().getMetrics(metric);//metric应该是用来接收窗口的密度参数
        windowHeight = metric.heightPixels+getStatusBarHeight();  // 屏幕高度
        windowWidth = metric.widthPixels;  // 屏幕宽度
        LogUtils.v2("屏幕像素","高"+windowHeight+"宽"+windowWidth);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//屏幕输入法键盘显示相关的
        res = getResources();
        // 加载main.xml界面设计文件
        setContentView(R.layout.activity_game);
        // 获取main.xml界面设计文件中ID为mainLayout的组件
        mainLayout = (FrameLayout) findViewById(R.id.Main_Game);//这个主Activity的布局
        // 创建GameView组件
   //     mainView = new GameView(this.getApplicationContext(), GameView.STAGE_INIT);  //表示初始化
        fishView=new FishView(this.getApplicationContext(),null);
        mainLP = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainLayout.addView(fishView, mainLP);
        // 播放背景音乐
        player = MediaPlayer.create(this, R.raw.background);
        player.setLooping(true);
        player.start();
    }

    private int getStatusBarHeight() {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
}
*/