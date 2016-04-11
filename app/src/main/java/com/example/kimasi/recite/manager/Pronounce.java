package com.example.kimasi.recite.manager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kimasi.recite.ReciteInfo;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * 发音
 * Created by kimasi on 2016/4/7.
 */
public class Pronounce {

    private static Pronounce pronounce;
    private Toast mToast;
    private SpeechSynthesizer mTts; //讯飞语音模块,在线解析

    public Pronounce(Context context) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=56b0437d");//初始化id

        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);//!!不设置监听可能会失败
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");           //设置语速
        mTts.setParameter(SpeechConstant.VOLUME, ReciteInfo.getReciteInfo().getFayin().toString());          //设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
    }

    public static Pronounce getPronounce(Context context) {
        if (pronounce == null) {
            pronounce = new Pronounce(context);
        }
        return pronounce;
    }

    private InitListener mTtsInitListener = new InitListener() {
        @Override  //讯飞发音初始化监听
        public void onInit(int code) {
            Log.d("类初始化失败", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                mToast.setText("初始化失败,错误码：" + code);
                mToast.show();
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    public void pronounce(String k) {//发音
        mTts.startSpeaking(k, new com.iflytek.cloud.SynthesizerListener() {
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
    public void setLoudness(String loudness) {
        mTts.setParameter(SpeechConstant.VOLUME, loudness);//设置音量，范围 0~100
    }
}
