package com.example.kimasi.recite.game.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.kimasi.recite.game.GamingInfo;
import com.example.kimasi.recite.game.model.interfaces.DrawableAdapter;

/**
 * 气泡
 * Created by kimasi on 2016/3/25.
 */
public class Bubble extends DrawableAdapter {

    private Bitmap bBitmap;
    private Bitmap bBitmap1;
    private Bitmap bBitmap2;
    private BubbleText string;
    private Matrix matrix = new Matrix();
    private Paint paint=null;  //本来是用来改变气球的颜色的
    private float bx=0; //坐标,这是外切矩形的左上顶点, 现在Bubble的像素大小是200
    private float by=0;
    private int speed_y=10;
    private int speed_y2=speed_y+2;

    public boolean contactRIGHT=false;
    public boolean contactLEFT=false;

    public Bubble(Bitmap bBitmap_,Bitmap bBitmap_2,BubbleText string){
        bBitmap1= bBitmap_;
        bBitmap2= bBitmap_2;
        bBitmap=bBitmap1;
    //    Paint paint2=new Paint();
    //    paint2.setColor(Color.WHITE);
    //    paint2.setAlpha(200);
    //    paint=paint2;
        this.string=string;
    }

    public int getBx(){
        return (int)bx;
    }
    public int getBy(){
        return (int)by;
    }
    public void setBx(float x){
        bx=x;
    }
    public void setBy(float y){
        by=y;
    }
    public void setXY(float x,float y){
        bx=x;by=y;
    }
    public Matrix getPicMatrix(){
        return matrix;
    }
    public Bitmap getCurrentPic(){
        return bBitmap;
    }
    public int getPicWidth(){
        return bBitmap.getWidth();
    }
    public int getPicHeight(){
        return bBitmap.getHeight();
    }

    private Boolean isBubbleColor=false;
    public void setBubbleColor(){
        bBitmap=bBitmap2;
        isBubbleColor=true;

    }
    public void clearBubbleColor(){
        bBitmap=bBitmap1;
        isBubbleColor=false;
    }

    public void onDraw(Canvas canvas, Paint paint_){
        matrix.reset();
        matrix.postTranslate(bx,by);
        Paint paint1=new Paint();
        paint1.setColor(Color.RED);
        paint1.setAlpha(40);
        paint1.setTextSize(28);
        canvas.drawText(string.getString()[0],bx+10,by+100,string.getPaint());
        canvas.drawText(string.getString()[1],bx+10,by+140,string.getPaint()); //第二行
        canvas.drawText(string.getInteger().toString(),bx+40,by+170,string.getPaint()); //第二行
        if (isBubbleColor){
        /*    Paint paint4=new Paint();
            paint4.setColor(Color.WHITE);
            paint4.setAlpha(20);
            paint=paint4;
            paint=paint4;*/
            paint=null;
        }else {
            paint=null;
        }
        canvas.drawBitmap(bBitmap, matrix, paint);
        move();
        try{
            Thread.sleep(0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void move(){
        if (bx<0){
            contactLEFT=true;
        }
        if (bx>GamingInfo.getGamingInfo().getScreenWidth()-200){
            contactRIGHT=true;
        }
        if (by>GamingInfo.getGamingInfo().getScreenHeight()-210){
            contactLEFT=true;contactRIGHT=true;
        }
        if (contactRIGHT||contactLEFT){//有一个为真(或两个都真),值真
            if (contactRIGHT&&contactLEFT){//下面有两个支撑,停
                contactLEFT=false;contactRIGHT=false;
            }else {
                if (contactRIGHT){//有一个右支撑
                        bx-=speed_y2;
                        by+=((float)Math.sqrt(speed_y2*speed_y2-100));
                        contactRIGHT=false;
                }else { //有一个左支撑
                        bx+=speed_y2;
                        by+=((float)Math.sqrt(speed_y2*speed_y2-100));
                        contactLEFT=false;
                }
            }
        }else{//表示下面没有支撑可以下落
            by+=speed_y;
            int count =speed_y;
            for (int i=0;i<count;i++){
                speed_y+=0.2;
            }
        }
    }

    public BubbleText getBubbleText() {
        return string;
    }

    private Bubble contactRightBubble=null;
    private Bubble contactLeftBubble=null;

    public void setContactRIGHT(Bubble bubble){
            contactRIGHT=true;
    }
    public void setContactLEFT(Bubble bubble) {
            contactLEFT = true;
    }
}
