package com.example.kimasi.recite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import com.iflytek.cloud.SpeechConstant;


public class setActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener{

    SeekBar seekBar1;
    SeekBar seekBar2;

    Integer aaa;
    Integer integer=80;
    Integer integer2;
    Boolean aBoolean;

    Switch aSwitch1;

    RadioGroup timegroup;

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        Window win = this.getWindow(); //返回当前的activity窗口( 应该是用来设置当前窗口的属性
        win.getDecorView().setPadding(0, 0, 0, 0);//应该是设置,窗口底色透明
        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;//设置对话框置顶显示
    //    lp.gravity=Gravity.RIGHT;
        win.setAttributes(lp);

        timegroup=(RadioGroup)findViewById(R.id.timeGroup);

        preferences=getSharedPreferences("fpeizhi", MODE_PRIVATE);//保存在本地 (配制,少部分数据)
        integer=preferences.getInt("fayin", 0);
        integer2=preferences.getInt("liangdu",100);
        aBoolean=preferences.getBoolean("zidong",true);

        Log.v("进度条值",integer2.toString());
        editor=preferences.edit();


        seekBar1=(SeekBar)findViewById(R.id.seekBar1);
        seekBar2=(SeekBar)findViewById(R.id.seekBar2);
        aSwitch1=(Switch)findViewById(R.id.switch1);

        seekBar1.setProgress(integer2);
        seekBar2.setProgress(integer);//设置音量值
        aSwitch1.setChecked(aBoolean);

        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);

        aSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setScreenMode(1);//设置亮度自动
                    setScreenBrightness(getScreenBrightness());

                    editor.putBoolean("zidong",true);
                    editor.commit();
                }
                else {
                    setScreenMode(0);
                    saveScreenBrightness(integer2);
                    setScreenBrightness(integer2);

                    editor.putBoolean("zidong",false);
                    editor.commit();
                }
            }
        });

        timegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        setScreenOffTime(216000);
                        break;
                    case R.id.radioButton2:
                        setScreenOffTime(322000);
                        break;
                    case R.id.radioButton3:
                        setScreenOffTime(700000);
                        break;
                }
            }
        });


    }
    @Override  //重写SeekBar监听 滑块改变时触发
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seekBar1:
                saveScreenBrightness(progress);
                setScreenBrightness(progress);
                aSwitch1.setChecked(false); //测试是否会调用监听
                editor.putInt("liangdu", progress);
                editor.putBoolean("zidong",false);
                editor.commit();
            break;
            case R.id.seekBar2:
                integer=new Integer(progress);
                MainActivity.mTts.setParameter(SpeechConstant.VOLUME,integer.toString() );//设置音量，范围 0~100
                editor.putInt("fayin", integer);
                editor.commit();
                break;
        }
        //ui另开线程         textView1.setText(progress);
    }

    @Override  //重写SeekBar监听
    public void onStartTrackingTouch(SeekBar seekBar) {
//应该是设置进度条外观        seekBar. setProgressDrawable (Drawable d);
        Log.v("onTrackong=","start");
    }

    @Override  //重写SeekBar监听
    public void onStopTrackingTouch(SeekBar seekBar) {

        Log.v("onTrackong=","stop");

    }
    /**
     * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private int getScreenMode() {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }
    /**
     * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private void setScreenMode(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
    /**
     * 获得当前屏幕亮度值 0--255
     */
    private int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        return screenBrightness;
    }

    /**
     * 设置当前屏幕亮度值 0--255
     */
    private void saveScreenBrightness(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 保存当前的屏幕亮度值，并使之生效
     */
    private void setScreenBrightness(int paramInt) {
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();//应该是当前窗口的属性
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;//设置一个屏幕亮度值
        localWindow.setAttributes(localLayoutParams);//设置属性
    }

    /**
     * 获得锁屏时间  毫秒
     */
    private int getScreenOffTime(){
        int screenOffTime=0;
        try{
            screenOffTime = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        }
        catch (Exception localException){

        }
        return screenOffTime;
    }
    /**
     * 设置背光时间  毫秒
     */
    private void setScreenOffTime(int paramInt){
        try{
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, paramInt);
        }catch (Exception localException){
            localException.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuset, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
