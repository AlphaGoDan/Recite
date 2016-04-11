package com.example.kimasi.recite.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimasi.recite.R;
import com.example.kimasi.recite.ReciteInfo;
import com.example.kimasi.recite.manager.HttpR;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    Handler handler;

    @Bind(R.id.search_aa) SearchView searchView;
    @Bind(R.id.jieguo111) TextView jieguo111;
    @Bind(R.id.sousuo111) TextView sousuo111;
    @Bind(R.id.check_sou) CheckBox check ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        handler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what==0x1233) {
                    Bundle bundle=msg.getData();
                    String en=bundle.getString("en");
                    String cn=bundle.getString("cn");
                    sousuo111.setText(en);
                    jieguo111.setText(cn);
                }
                super.handleMessage(msg);
            }
        };

        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("在线搜索");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(final String newText) {

                if (TextUtils.isEmpty(newText)){//搜索框空
                    Message message=new Message();
                    message.what=0x1233;
                    Bundle bundle=new Bundle();
                    bundle.putString("en"," ");
                    bundle.putString("cn","请输入 ...");
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                else{
                    new Thread() {
                        @Override
                        public void run() {
                            String result=new HttpR().getYoudaoTranslate(newText);//get有点翻译的结果
                            Message message=new Message();
                            message.what=0x1233;
                            Bundle bundle=new Bundle();
                            bundle.putString("en",newText);
                            bundle.putString("cn",result);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }.start();
                    check.setChecked(false);
                }
                return true;
            }
        });

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    String ss1 = sousuo111.getText().toString();
                    String ss2 = jieguo111.getText().toString();
                    if(ReciteInfo.getReciteInfo().getDataStorage().queryWorld(ss1)){   //如果数据库没有，则插入
                        ContentValues values=new ContentValues();
                        values.put("word",ss1);
                        values.put("detail",ss2);
                        values.put("k","0");
                        ReciteInfo.getReciteInfo().getDataStorage().addWord(values);
                        Toast.makeText(getApplicationContext(), "添加成功",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "单词已存在",
                                Toast.LENGTH_SHORT).show();
                        check.setChecked(false);
                        Log.v("=== ","数据库已有单词");}
                        Log.v("=== ","选中"+ss1+" "+ss2);
                }else{
                        Log.v("=== ","取消");
                }
            }
        });
    }






}
