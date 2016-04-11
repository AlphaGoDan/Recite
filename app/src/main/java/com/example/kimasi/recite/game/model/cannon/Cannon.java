package com.example.kimasi.recite.game.model.cannon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.kimasi.recite.game.model.interfaces.DrawableAdapter;

/**
 * 大炮
 * Created by kimasi on 2016/3/29.
 */
public class Cannon  extends DrawableAdapter{

    private Bitmap[] cannonImage;
    private int currentId;

    private Matrix matrix;
    //大炮的旋转点设置
    private int gun_rotate_point_x;
    private int gun_rotate_point_y;
    //大炮的位置
    private float x;
    private float y;

    public Cannon(Bitmap[] cannonImage){
        this.cannonImage = cannonImage;
    }
    public void init(){
        gun_rotate_point_x = this.getPicWidth()/2;
        gun_rotate_point_y = this.getPicHeight()/2;
     //   x = GamingInfo.getGamingInfo().getCannonLayoutX()-gun_rotate_point_x;
 //       y = GamingInfo.getGamingInfo().getCannonLayoutY()-gun_rotate_point_y;
    //    this.getPicMatrix().setTranslate(x, y);
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public int getGun_rotate_point_x() {
        return gun_rotate_point_x;
    }

    public int getGun_rotate_point_y() {
        return gun_rotate_point_y;
    }

    /**
     * 播放发射炮弹的动作
     */
    public void shot(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 因为默认就是第一张图，所以从第二张图开始播放
                for(int i =1;i<cannonImage.length;i++){
                    try {
                        currentId = i;
                        Thread.sleep(300);
                    } catch (Exception e) {
                        Log.e("Cannon", e.toString());
                    }
                }
                //最后还要恢复第一张图的样子
                currentId = 0;
            }
        }).start();
    }


    public Matrix getPicMatrix() {
        // TODO Auto-generated method stub
        return matrix;
    }

    public void onDraw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(this.getCurrentPic(),
                this.getPicMatrix(), paint);
    }

    @Override
    public Bitmap getCurrentPic() {
        // TODO Auto-generated method stub
        return cannonImage[currentId];
    }
    @Override
    public int getPicWidth() {
        // TODO Auto-generated method stub
        return cannonImage[currentId].getWidth();
    }
    @Override
    public int getPicHeight() {
        // TODO Auto-generated method stub
        return cannonImage[currentId].getHeight();
    }
}
