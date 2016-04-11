package com.example.kimasi.recite.game.tools;

import com.example.kimasi.recite.aaatools.LogUtils;

/**
 * 圆形碰撞检测
 * Created by kimasi on 2016/3/31.
 */
public class CircleIntersect {
    //判断是否相碰   返回第一个点被碰撞的 位置
    public static int towCircleDistance(int x1, int y1, int x2, int y2) {//圆大小不一的时候不适用,

        Integer distance;
        if (x1 == x2) {   //在同一直线
            if (y1 != y2) {
                distance = Math.abs(y2 - y1);//返回绝对值
            }
        } else if (y1 == y2) {
            distance = Math.abs(x2 - x1);
        } else {
            int a = Math.abs(x2 - x1);
            int b = Math.abs(y2 - y1);
            distance = (int) Math.sqrt((a * a + b * b)); //两个圆的距离
            if (distance < 200) {
                LogUtils.v2("碰撞测试'", "1=" + x1 + " " + y1 + " 2=" + x2 + " " + y2 + " distance=" + distance);
                if (x1 < x2) {
                    if (y1 < y2) {
                        return 4; //球2位于球1右下
                    } else {
                        return 2;
                    }
                } else {
                    if (y1 < y2) {
                        return 8; //球2位于球1左下
                    } else {
                        return 6;
                    }
                }
            }
        }
        return 11;
    }
}
