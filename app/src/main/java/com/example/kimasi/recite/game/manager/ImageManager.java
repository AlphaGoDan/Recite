package com.example.kimasi.recite.game.manager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.kimasi.recite.R;
import com.example.kimasi.recite.game.GamingInfo;

/**
 * 图片资源管理
 * Created by kimasi on 2016/3/29.
 */
public class ImageManager {


    public ImageManager(){

    }

    public static Bitmap getBubbleImage( ){
        Resources res=GamingInfo.getGamingInfo().getActivity().getResources();
       return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.bubble3),200,200,false);
    }
    public static Bitmap getBubbleImage2( ){
        Resources res=GamingInfo.getGamingInfo().getActivity().getResources();
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.bubble4),200,200,false);
    }

    public static Bitmap getBackGroundImage(){
        Resources res=GamingInfo.getGamingInfo().getActivity().getResources();
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res,R.drawable.bbb),GamingInfo.getGamingInfo().getScreenWidth(),GamingInfo.getGamingInfo().getScreenHeight(),false);
    }

}
