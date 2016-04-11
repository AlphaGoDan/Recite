package com.example.kimasi.recite.game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.kimasi.recite.game.threads.GameThread;

/**
 * "画"游戏,可在另一线程画ui
 * Created by kimasi on 2016/3/29.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    GameThread gameThread;

    public GameSurface(Context ctx) {
        super(ctx);

        this.getHolder().addCallback(this);//caonimeide ,!!!
    }

    public void surfaceCreated(SurfaceHolder holder){
        // 启动主线程执行部分
        if (gameThread == null)
        {
            gameThread = new GameThread(holder);//用来作为线程锁和画界面
            gameThread.start();
        }


    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void surfaceDestroyed(SurfaceHolder holder){

    }

    public void onDraw(Canvas canvas){


    }
}
