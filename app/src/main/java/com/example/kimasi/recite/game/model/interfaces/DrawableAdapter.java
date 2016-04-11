package com.example.kimasi.recite.game.model.interfaces;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Drawable
 * Created by kimasi on 2016/3/30.
 */
public abstract class DrawableAdapter implements Drawable {
 //   private Matrix matrix=new Matrix();

 //   public Matrix getMatrix(){
 //         return matrix;
 //  }

    public void onDraw(Canvas canvas, Paint paint){
        canvas.drawBitmap(this.getCurrentPic(),this.getPicMatrix(),paint);
    }
}
