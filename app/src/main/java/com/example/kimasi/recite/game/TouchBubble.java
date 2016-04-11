package com.example.kimasi.recite.game;

import com.example.kimasi.recite.game.manager.GameManager;
import com.example.kimasi.recite.game.model.Bubble;

import java.util.Vector;

/**
 * 气泡的点击处理
 * Created by kimasi on 2016/4/5.
 */
public class TouchBubble {

    public static Bubble bubble1;
    public static Bubble bubble2;

    public static void touch(float x, float y) {
        Vector<Bubble> vector = GameManager.getGameManager().getAllBubble1();
        for (Bubble bubble : vector) {
            if (bubble.getBx() < x && x < bubble.getBx() + 200) {
                if (bubble.getBy() < y && y < bubble.getBy() + 200) {
                    bubble.setBubbleColor();
                    if (bubble1 == null) {
                        bubble1 = bubble;
                    } else {
                        if (bubble != bubble1) {//防止同一个被点击两次,
                            bubble2 = bubble;
                        }
                    }
                    removeTowBubble();
                }
            }
        }
    }

    public static void removeTowBubble() {
        if ((bubble1 != null) && (bubble2 != null)) {
            if (bubble1.getBubbleText().getInteger() .equals( bubble2.getBubbleText().getInteger())) {
                GameManager.getGameManager().setDeleteSomeBubble(bubble1, bubble2);
            }else{
                bubble1.clearBubbleColor();
                bubble2.clearBubbleColor();
            }
            bubble1=null;
            bubble2=null;
        }
    }

}
