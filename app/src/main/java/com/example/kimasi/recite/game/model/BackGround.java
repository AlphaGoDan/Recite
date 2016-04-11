package com.example.kimasi.recite.game.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.kimasi.recite.game.model.interfaces.DrawableAdapter;

public class BackGround extends DrawableAdapter {
	private Bitmap background;
	private Matrix matrix;

	public void setCurrentPic(Bitmap background){
		this.background = background;
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
		return background;
	}

	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return background.getWidth();
	}

	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return background.getHeight();
	}

}
