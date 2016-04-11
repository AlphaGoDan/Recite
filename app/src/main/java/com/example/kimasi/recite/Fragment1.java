package com.example.kimasi.recite;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kimasi.recite.ui.SearchActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Fragment1 extends Fragment implements View.OnClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;  //给外部的接口,可调用实现他的 方法

     @Bind(R.id.d1) TextView d1;
     @Bind(R.id.d2) TextView d2;
     @Bind(R.id.d3) TextView d3;
     @Bind(R.id.d4) TextView d4;
     @Bind(R.id.d5) TextView d5;
     @Bind(R.id.d6) TextView d6;

     @Bind(R.id.f1) TextView f1;
     @Bind(R.id.f2) TextView f2;
     @Bind(R.id.f3) TextView f3;
     @Bind(R.id.f4) TextView f4;
     @Bind(R.id.f5) TextView f5;
     @Bind(R.id.f6) TextView f6;

     @Bind(R.id.number) TextView number;

     @Bind(R.id.searchButton) ImageButton serach;
     @Bind(R.id.buttonUp) ImageButton up;
     @Bind(R.id.buttonDow) ImageButton dow;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main1, container, false);//fragment主布局
        ButterKnife.bind(this, view);//给绑定用,要传入跟布局

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

        serach.setOnClickListener(this);
        up.setOnClickListener(this);
        dow.setOnClickListener(this);

        setDanci();
        setFanyi();
        number.setText(ReciteInfo.getReciteInfo().getAccomplishNumber()+" 个单词");
        return view;
    }

    public void onClick(View v) {//隐藏按钮的监听
        switch (v.getId()) {
            case R.id.button1:
                achieve(ReciteInfo.pageNumber);
                break;
            case R.id.button2:
                achieve(ReciteInfo.pageNumber+1);
                break;
            case R.id.button3:
                achieve(ReciteInfo.pageNumber+2);
                break;
            case R.id.button4:
                achieve(ReciteInfo.pageNumber+3);
                break;
            case R.id.button5:
                achieve(ReciteInfo.pageNumber+4);
                break;
            case R.id.button6:
                achieve(ReciteInfo.pageNumber+5);
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
            case R.id.searchButton:
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
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

    public  void DownDF(){ //翻页,下
        if (ReciteInfo.taskWords.size()>60){  //列表完,!!要重新修改列表完的时候会出错
            ReciteInfo.pageNumber=(ReciteInfo.pageNumber+6)%60;
        }
        setDanci();
        setFanyi();
    }

    public  void UpDF(){//翻页,上
        if (ReciteInfo.pageNumber==0){//列表完,!!要重新修改列表完的时候会出错
            ReciteInfo.pageNumber=54;
        }else {
            ReciteInfo.pageNumber=ReciteInfo.pageNumber-6;
        }
        setDanci();
        setFanyi();
    }

    public static void setCC(){//销毁时保存进度

    }

    public  void setDanci(){//设置显示单词
        d1.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber).getEn());
   //     LogUtils.v2("展示的时候"+ReciteInfo.pageNumber+" "+ReciteInfo.taskWords.get(ReciteInfo.pageNumber).getEn());
        d2.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 1).getEn());
        d3.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 2).getEn());
        d4.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 3).getEn());
        d5.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 4).getEn());
        d6.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 5).getEn());
    }
    public  void setFanyi(){//设置显示翻译
        f1.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber).getCn());
        f2.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 1).getCn());
        f3.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 2).getCn());
        f4.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 3).getCn());
        f5.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 4).getCn());
        f6.setText(ReciteInfo.taskWords.get(ReciteInfo.pageNumber + 5).getCn());
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
        String s1=ReciteInfo.taskWords.get(k).getEn();
        ReciteInfo.getReciteInfo().getDataStorage().updateWord(s1);
        ReciteInfo.taskWords.remove(k);
        ReciteInfo.accomplishNumber++;

        number.setText(ReciteInfo.accomplishNumber+" 个单词");
        setDanci();
        setFanyi();
    }

    public void pronounce(TextView t_){
        String t=(String)t_.getText();
        ReciteInfo.getReciteInfo().getPronounce().pronounce(t);//发声
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
        ReciteInfo.getReciteInfo().setPageNumber(ReciteInfo.pageNumber);
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
