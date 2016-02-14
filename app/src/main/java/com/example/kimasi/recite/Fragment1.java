package com.example.kimasi.recite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;

import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment implements View.OnClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;  //给外部的接口,可调用实现他的 方法

    static TextView d1 = null;
    static TextView d2 = null;
    static TextView d3 = null;
    static TextView d4 = null;
    static TextView d5 = null;
    static TextView d6 = null;

    static TextView f1=null;
    static TextView f2=null;
    static TextView f3=null;
    static TextView f4=null;
    static TextView f5=null;
    static TextView f6=null;

    static TextView number=null;

    ImageButton up=null;
    ImageButton dow=null;

    static Integer i = 0;

    static Integer finish=0;


    String k="0";

    static List<String> list0 = new ArrayList<String>();
    static List<String> list1 = new ArrayList<String>();
    static List<String> list2 = new ArrayList<String>();
    static List<String> list3 = new ArrayList<String>();

    static  SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    // TextToSpeech spe=null;//发音,不兼容安卓4.4

    public static Fragment1 newInstance(int sectionNumber) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences=getActivity().getSharedPreferences("peizhi",getActivity().MODE_PRIVATE);//保存在本地 (配制,少部分数据)
        i=preferences.getInt("C",0);
        finish=preferences.getInt("N",0);

        editor=preferences.edit();

        SQLchaxun();//获取数据库,标记0的单词
    }

    private void SQLchaxun() {                        //like :匹配符合的字母, %为通配符
        Cursor cursor = MainActivity.mDb.rawQuery(    //查询k为0标记,未完成单词
                "select * from dict where k= ? ",     //<= _id and _id<= ?",// and k=?",//占位符查询
                new String[]{"0"});                   //总数1771
        while (cursor.moveToNext()) {
//            list0.add(cursor.getString(0));
            list1.add(cursor.getString(1));
            list2.add(cursor.getString(2));
//            list3.add(cursor.getString(3));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main1, container, false);//fragment主布局

        d1 = (TextView)view.findViewById(R.id.d1);
        d2 = (TextView)view.findViewById(R.id.d2);
        d3 = (TextView)view.findViewById(R.id.d3);
        d4 = (TextView)view.findViewById(R.id.d4);
        d5 = (TextView)view.findViewById(R.id.d5);
        d6 = (TextView)view.findViewById(R.id.d6);

        f1 = (TextView)view.findViewById(R.id.f1);
        f2 = (TextView)view.findViewById(R.id.f2);
        f3 = (TextView)view.findViewById(R.id.f3);
        f4 = (TextView)view.findViewById(R.id.f4);
        f5 = (TextView)view.findViewById(R.id.f5);
        f6 = (TextView)view.findViewById(R.id.f6);

        number=(TextView)view.findViewById(R.id.number);

        up=(ImageButton)view.findViewById(R.id.buttonUp);
        dow=(ImageButton)view.findViewById(R.id.buttonDow);

        d1.setOnClickListener(this);
        d2.setOnClickListener(this);
        d3.setOnClickListener(this);
        d4.setOnClickListener(this);
        d5.setOnClickListener(this);
        d6.setOnClickListener(this);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        view.findViewById(R.id.button3).setOnClickListener(this);
        view.findViewById(R.id.button4).setOnClickListener(this);
        view.findViewById(R.id.button5).setOnClickListener(this);
        view.findViewById(R.id.button6).setOnClickListener(this);

        up.setOnClickListener(this);
        dow.setOnClickListener(this);

        view.findViewById(R.id.la1).setOnClickListener(this);
        view.findViewById(R.id.la2).setOnClickListener(this);
        view.findViewById(R.id.la3).setOnClickListener(this);
        view.findViewById(R.id.la4).setOnClickListener(this);
        view.findViewById(R.id.la5).setOnClickListener(this);
        view.findViewById(R.id.la6).setOnClickListener(this);



        setDanci();
        setFanyi();

        number.setText(finish.toString()+" 个单词");
        return view;
    }

    public void onClick(View v) {//隐藏按钮的监听
        switch (v.getId()) {
            case R.id.button1:
                achieve(i);
                break;
            case R.id.button2:
                achieve(i+1);
                break;
            case R.id.button3:
                achieve(i+2);
                break;
            case R.id.button4:
                achieve(i+3);
                break;
            case R.id.button5:
                achieve(i+4);
                break;
            case R.id.button6:
                achieve(i+5);
                break;
            case R.id.d1:
                pronounce(d1);
                break;
            case R.id.d2:
                pronounce(d2);
                break;
            case R.id.d3:
                pronounce(d3);
                break;
            case R.id.d4:
                pronounce(d4);
                break;
            case R.id.d5:
                pronounce(d5);
                break;
            case R.id.d6:
                pronounce(d6);
                break;
            case R.id.buttonUp:
                UpDF();
                break;
            case R.id.buttonDow:
                DownDF();
                break;
            default:
                break;
        }
    }

    public static void DownDF(){ //翻页,下
        if (list1.size()>60){  //列表完,!!要重新修改列表完的时候会出错
            i=(i+6)%60;
        }
        setDanci();
        setFanyi();
    }

    public static void UpDF(){//翻页,上
        if (i==0){//列表完,!!要重新修改列表完的时候会出错
            i=54;
        }else {
            i=i-6;
        }
        setDanci();
        setFanyi();
    }

    public static void setCC(){//销毁时保存进度
        editor.putInt("C", i);
        editor.putInt("N",finish);
        editor.commit();
    }

    public static void setDanci(){//设置显示单词
        d1.setText(list1.get(i));
        d2.setText(list1.get(i + 1));
        d3.setText(list1.get(i + 2));
        d4.setText(list1.get(i + 3));
        d5.setText(list1.get(i + 4));
        d6.setText(list1.get(i + 5));
    }
    public static void setFanyi(){//设置显示翻译
        f1.setText(list2.get(i));
        f2.setText(list2.get(i + 1));
        f3.setText(list2.get(i + 2));
        f4.setText(list2.get(i + 3));
        f5.setText(list2.get(i + 4));
        f6.setText(list2.get(i + 5));
    }
    public  void setDanciConceal(){//单词隐藏
        d1.setVisibility(View.INVISIBLE);
        d2.setVisibility(View.INVISIBLE);
        d3.setVisibility(View.INVISIBLE);
        d4.setVisibility(View.INVISIBLE);
        d5.setVisibility(View.INVISIBLE);
        d6.setVisibility(View.INVISIBLE);

    }
    public void setDanciShow(){//单词显示
        d1.setVisibility(View.VISIBLE);
        d2.setVisibility(View.VISIBLE);
        d3.setVisibility(View.VISIBLE);
        d4.setVisibility(View.VISIBLE);
        d5.setVisibility(View.VISIBLE);
        d6.setVisibility(View.VISIBLE);
    }
    public void setFanyiConceal(){//翻译隐藏
        f1.setVisibility(View.INVISIBLE);
        f2.setVisibility(View.INVISIBLE);
        f3.setVisibility(View.INVISIBLE);
        f4.setVisibility(View.INVISIBLE);
        f5.setVisibility(View.INVISIBLE);
        f6.setVisibility(View.INVISIBLE);
    }
    public void setFanyiShow(){//翻译显示
        f1.setVisibility(View.VISIBLE);
        f2.setVisibility(View.VISIBLE);
        f3.setVisibility(View.VISIBLE);
        f4.setVisibility(View.VISIBLE);
        f5.setVisibility(View.VISIBLE);
        f6.setVisibility(View.VISIBLE);
    }

    public void achieve(int k){//移除已完成单词,移到已完成列表list11
        String s1=list1.get(k);
        ContentValues contentValues=new ContentValues();
        contentValues.put("k",1);
        MainActivity.mDb.update("dict",contentValues," word = ?",new String[]{s1});

        list1.remove(k);
        list2.remove(k);
        finish++;

        number.setText(finish.toString()+" 个单词");
        setDanci();
        setFanyi();
    }

    public void pronounce(TextView k){//发音
        MainActivity.mTts.startSpeaking(k.getText().toString(), new com.iflytek.cloud.SynthesizerListener() {
            @Override
            public void onSpeakBegin() {//开始
            }
            @Override
            public void onBufferProgress(int i, int i2, int i3, String s) {//缓存进度
            }
            @Override
            public void onSpeakPaused() {//
            }
            @Override
            public void onSpeakResumed() {
            }
            @Override
            public void onSpeakProgress(int i, int i2, int i3) {
            }
            @Override
            public void onCompleted(SpeechError speechError) {
            }
            @Override
            public void onEvent(int i, int i2, int i3, Bundle bundle) {
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {   //附上activity的时候调用
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(  //设置bar标题->传进参数指定了mTitle的值
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void onPause(){  ///进入后台提交持久性变化
        super.onPause();
        setCC();            //保存配制数据,翻页,和已完成数
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
