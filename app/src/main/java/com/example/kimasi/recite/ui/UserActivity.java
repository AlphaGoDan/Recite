package com.example.kimasi.recite.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kimasi.recite.R;
import com.example.kimasi.recite.ReciteInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;

public class UserActivity extends AppCompatActivity {

    @Bind(R.id.ttt1)
    TextView tt1;
    @Bind(R.id.ttt2)
    TextView tt2;
    @Bind(R.id.touxiang111)
    ImageView touxiang1;

    Bitmap head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        head = BitmapFactory.decodeFile(ReciteInfo.headPictureFile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //系统版本,容错
            setTranslucentStatus(true);  //处理状态栏

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.statusbar_bg2);//通知栏所需颜色
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            // view.setPadding(0,150,0,0);//(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());//处理侧边栏被遮,
        }

        touxiang1.setImageBitmap(head);//??会报错,另外处理
        tt1.setText(ReciteInfo.userName);

        touxiang1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);//启动相册
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//返回一个图片
                startActivityForResult(intent1, 1);
            }
        });
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        t});*/
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {//处理状态栏透明
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);//定义窗口属性
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//启动其他activity的回调
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) { //相册的，得到图片后裁剪
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);//保存在SD卡中
                        touxiang1.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(ReciteInfo.path);
        file.mkdirs();// 创建文件夹
        String fileName = ReciteInfo.headPictureFile;//图片地址和名字
        try {
            b = new FileOutputStream(fileName);//包装一个本地文件保存
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cropPhoto(Uri uri) {  //裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 140);
        intent.putExtra("outputY", 140);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //初始化菜单
        getMenuInflater().inflate(R.menu.user, menu);
        //     ActionBar actionBar = getSupportActionBar();//应该是返回这个activity的ActionBar,,
        //     actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //     actionBar.setDisplayShowTitleEnabled(true);
        //     actionBar.setTitle(mTitle);  //显示bar的标题
        //     Resources resources=getResources();
        //     ColorDrawable drawable=new ColorDrawable(R.color.bar2);
        //     Drawable drawable=resources.getDrawable(R.drawable.baraa);
        //     actionBar.setBackgroundDrawable(drawable);
        //等于返回设置好的这个activity的ActionBar,

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //处理菜单项的点击

        int id = item.getItemId();

        if (id == R.id.action_example2) {//设置

            BmobUser.logOut(this);   //清除缓存用户对象
            BmobUser currentUser = BmobUser.getCurrentUser(this); // 现在的currentUser是null了
            ReciteInfo.register = false;
            ReciteInfo.userName = "点击登陆";
            NavigationDrawerFragment.userNameView.setText("点击登陆");//显示用户信息

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
