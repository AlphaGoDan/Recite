package com.example.kimasi.recite.game.model;

import android.graphics.Paint;

import java.util.Arrays;

/**
 * 气泡上的文本,跟word适配
 * Created by kimasi on 2016/4/9.
 */
public class BubbleText {
    private String[] string;
    private Integer integer;  //用于匹配是否是同一个单词的
    private Paint paint;
    public BubbleText(Integer integer, String[] string,Paint paint) {
        this.integer = integer;
        this.string = string;
        this.paint=paint;
    }

    public String[] getString() {  //在这里处理字符要显示的
        return string;
    }

    public Integer getInteger() {
        return integer;
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public String toString() {
        return "BubbleText{" +
                "string=" + Arrays.toString(string) +
                ", integer=" + integer +
                '}';
    }
}
