package com.example.kimasi.recite.game.threads;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.kimasi.recite.game.GameSurface;
import com.example.kimasi.recite.game.GamingInfo;
import com.example.kimasi.recite.game.manager.GameManager;

/**
 * 游戏线程,
 * Created by kimasi on 2016/3/31.
 */
public class GameThread extends Thread {

    private GameSurface gameSurface;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;

    static boolean needStop=false;
    private GameManager gameManager;

    public GameThread(SurfaceHolder holder){

        this.surfaceHolder=holder;
        gameManager=GameManager.getGameManager();
    }
    public void run(){
        super.run();


        while (GamingInfo.getGamingInfo().isGaming()){
            canvas=surfaceHolder.lockCanvas();
            gameManager.bubbleDow(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);  //
        }



    }
    private void odLogi(){

    }
    private void onDraw(){

    }
}
