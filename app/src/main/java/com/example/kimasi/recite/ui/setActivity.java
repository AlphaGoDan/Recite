package com.example.kimasi.recite.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.kimasi.recite.R;
import com.example.kimasi.recite.ReciteInfo;
import com.example.kimasi.recite.manager.SysBrightness;

import butterknife.Bind;
import butterknife.ButterKnife;


public class setActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    @Bind(R.id.seekBar1)
    SeekBar seekBar1;
    @Bind(R.id.seekBar2)
    SeekBar seekBar2;
    @Bind(R.id.switch1)
    Switch aSwitch1;
    @Bind(R.id.timeGroup)
    RadioGroup timegroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        ButterKnife.bind(this);

        Window win = this.getWindow();             //返回当前的activity窗口( 设置当前窗口的属性
        win.getDecorView().setPadding(0, 0, 0, 0); //设置,窗口底色透明
        WindowManager.LayoutParams lp = win.getAttributes();
        //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;//设置对话框置顶显示
        // lp.gravity=Gravity.RIGHT;
        win.setAttributes(lp);

        seekBar1.setProgress(ReciteInfo.brightness);        //设置亮度条值
        seekBar2.setProgress(ReciteInfo.pronunciation);     //设置音量值

        if (ReciteInfo.brightnessMode == 1) {
            aSwitch1.setChecked(true);//自定义的亮度模式,
        } else {
            aSwitch1.setChecked(false);
        }
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        aSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SysBrightness.getSysBrightness(setActivity.this).setScreenMode(1);//设置亮度自动
                    ReciteInfo.brightnessMode = 1;
                    //setScreenBrightness(getScreenBrightness());
                } else {
                    SysBrightness.getSysBrightness(setActivity.this).setScreenMode(0);
                    SysBrightness.getSysBrightness(setActivity.this).saveScreenBrightness(ReciteInfo.brightness);
                    ReciteInfo.brightnessMode = 0;
                }
            }
        });

        timegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        SysBrightness.getSysBrightness(setActivity.this).setScreenOffTime(216000);
                        break;
                    case R.id.radioButton2:
                        SysBrightness.getSysBrightness(setActivity.this).setScreenOffTime(322000);
                        break;
                    case R.id.radioButton3:
                        SysBrightness.getSysBrightness(setActivity.this).setScreenOffTime(700000);
                        break;
                }
            }
        });
    }

    @Override  //重写SeekBar监听 滑块改变时触发
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar1:
                SysBrightness.getSysBrightness(this).saveScreenBrightness(progress);
                SysBrightness.getSysBrightness(this).setScreenBrightness(progress);
                aSwitch1.setChecked(false); //测试是否会调用监听
                ReciteInfo.brightness = progress;
                ReciteInfo.brightnessMode = 0;
                break;
            case R.id.seekBar2:
                ReciteInfo.pronunciation = progress;
                ReciteInfo.getReciteInfo().getPronounce().setLoudness(ReciteInfo.pronunciation.toString());
                break;
        }
        //ui另开线程         textView1.setText(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {  //首次点击进度条时调用
        //设置进度条外观  seekBar. setProgressDrawable (Drawable d);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {//停止点击进度条时调用

    }

    protected void onStop() {
        super.onStop();
        //       Intent intent=getIntent();
        //       intent.putExtra("liangdu",setLiangdu);
        //     intent.putExtra("liangduMo",setLiangduMo);
        //    setActivity.this.setResult(0,intent);
        //    setActivity.this.finish();
        //    System.out.println("===onStop===");//执行到这里进入后台,不可见
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
