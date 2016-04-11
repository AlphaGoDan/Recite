package com.example.kimasi.recite.game.model.interfaces;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Drawable接口
 * Created by kimasi on 2016/3/30.
 */
public interface Drawable {
      Matrix getPicMatrix();//获取图片旋转的矩阵
      Bitmap getCurrentPic();//当前动作的图片资源
      int getPicWidth();
      int getPicHeight();
      void onDraw(Canvas canvas, Paint paint);//会绘制的回调
}
