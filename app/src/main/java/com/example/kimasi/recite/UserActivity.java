package com.example.kimasi.recite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cn.bmob.v3.BmobUser;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //初始化菜单
            getMenuInflater().inflate(R.menu.user, menu);
            //ActionBar actionBar = getSupportActionBar();//应该是返回这个activity的ActionBar,,
       //     actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
          //  actionBar.setDisplayShowTitleEnabled(true);
        //    actionBar.setTitle(mTitle);  //显示bar的标题
      //      Resources resources=getResources();
            //    ColorDrawable drawable=new ColorDrawable(R.color.bar2);
      //      Drawable drawable=resources.getDrawable(R.drawable.baraa);
      //      actionBar.setBackgroundDrawable(drawable);
//等于返回设置好的这个activity的ActionBar,

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //处理菜单项的点击

        int id = item.getItemId();

        if (id == R.id.action_example2) {//设置

            BmobUser.logOut(this);   //清除缓存用户对象
            BmobUser currentUser = BmobUser.getCurrentUser(this); // 现在的currentUser是null了
            MainActivity.zhuce=false;
            MainActivity.userName="点击登陆";
            NavigationDrawerFragment.userNameView.setText("点击登陆");//显示用户信息

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
