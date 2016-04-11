package com.example.kimasi.recite.game;

import android.app.Activity;

import com.example.kimasi.recite.aaatools.LogUtils;
import com.example.kimasi.recite.game.manager.BubbleManager;
import com.example.kimasi.recite.game.manager.SoundManager;

/**
 * 游戏信息
 * Created by kimasi on 2016/3/29.
 */
public class GamingInfo {

    private int screenWidth;
    private int screenHeight;
    private static GamingInfo gameInfo;  // 单例模式需要
    private boolean isGaming;            // 是否处于游戏状态
    private boolean isPause;             //是否处于暂停状态
    private GameSurface surface;         // 主屏幕
    private Activity activity;
 //   private ArrayList<Bubble> fish = new ArrayList<Bubble>(); // 所有的
    private BubbleManager shoalManager;           // 管理器
    private SoundManager soundManager;            //音效管理器
    private float cannonLayoutX;			//大炮旋转X坐标
    private float cannonLayoutY;			//大炮旋转Y坐标
    private int score = 100;				//当前的分

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * 清除GamingInfo实例
     */
    public static void clearGameInfo() {
        gameInfo = null;
    }

    private GamingInfo() {
    }

    public static GamingInfo getGamingInfo() {
        if (gameInfo == null) {
            gameInfo = new GamingInfo();
        }
        return gameInfo;
    }

    public boolean isGaming() {
        return isGaming;
    }

    public void setGaming(boolean isGaming) {
        this.isGaming = isGaming;
    }



    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
        LogUtils.v2("屏幕宽高","wight="+screenWidth+" height="+screenHeight);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setSurface(GameSurface surface_){
        surface=surface_;
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public float getCannonLayoutX() {
        return cannonLayoutX;
    }

    public void setCannonLayoutX(float cannonLayoutX) {
        this.cannonLayoutX = cannonLayoutX;
    }

    public float getCannonLayoutY() {
        return cannonLayoutY;
    }

    public void setCannonLayoutY(float cannonLayoutY) {
        this.cannonLayoutY = cannonLayoutY;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }
}
