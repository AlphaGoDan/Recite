package com.example.kimasi.recite.game.manager;

import com.example.kimasi.recite.game.model.Bubble;

import java.util.ArrayList;

/**
 * Bubble管理
 * Created by kimasi on 2016/3/30.
 */
public class BubbleManager {

    private ArrayList<Bubble> allBubble = new ArrayList<>();

    private static BubbleManager bubbleManager=null;

    public static BubbleManager getBubbleManager(){

        if (bubbleManager==null) {
            bubbleManager = new BubbleManager();
        }
        return bubbleManager;
    }

    public void produceBubble(){
        for (int i=0;i<20;i++){
  //          Bubble b =new Bubble(ImageManager.getBubbleImage());
   //         allBubble.add(b);
        }

    }

}
