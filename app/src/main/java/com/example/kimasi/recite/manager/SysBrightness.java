package com.example.kimasi.recite.manager;

import android.app.Activity;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * 系统亮度设置
 * Created by kimasi on 2016/4/8.
 */
public class SysBrightness {

    private static SysBrightness sysBrightness;
    private Activity context;
    private SysBrightness(Activity context){
        this.context=context;
    }
    public static SysBrightness getSysBrightness(Activity context){
        if (sysBrightness==null){
            sysBrightness=new SysBrightness(context);
        }
        return sysBrightness;
    }

    /**
     * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public int getScreenMode() {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenMode;
    }

    /**
     * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public void setScreenMode(int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    public int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenBrightness;
    }

    /**
     * 设置当前屏幕亮度值 0--255
     */
    public void saveScreenBrightness(int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 保存当前的屏幕亮度值，并使之生效
     */
    public void setScreenBrightness(int paramInt) {
        Window localWindow = context.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();//应该是当前窗口的属性
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;//设置一个屏幕亮度值
        localWindow.setAttributes(localLayoutParams);//设置属性
    }

    /**
     * 获得锁屏时间  毫秒
     */
    public int getScreenOffTime() {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenOffTime;
    }

    /**
     * 设置背光时间  毫秒
     */
    public void setScreenOffTime(int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

}
