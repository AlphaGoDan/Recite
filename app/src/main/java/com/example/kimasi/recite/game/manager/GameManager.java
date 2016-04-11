package com.example.kimasi.recite.game.manager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.kimasi.recite.R;
import com.example.kimasi.recite.ReciteInfo;
import com.example.kimasi.recite.aaatools.LogUtils;
import com.example.kimasi.recite.game.GamingInfo;
import com.example.kimasi.recite.game.model.Bubble;
import com.example.kimasi.recite.game.model.BubbleText;
import com.example.kimasi.recite.module.Word;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * 游戏管理
 * Created by kimasi on 2016/3/29.
 */
public class GameManager {

    private static GameManager manager;
    private boolean initing = true; //是否正在初始化

    private Bitmap bubbleImage;
    private Bitmap bubbleImage2;
    private Bitmap backGroudImage;

    public static ArrayList<Bubble> deleteSomeBubble = new ArrayList<>();
    public Vector<Bubble> allBubble1 = new Vector<>();//线程安全的

    private GameManager() {
    }

    public static GameManager getGameManager() {
        if (manager == null) {
            manager = new GameManager();
        }
        return manager;
    }

    public boolean isIniting() {
        return initing;
    }

    public Vector<Bubble> getAllBubble1() {
        return allBubble1;
    }

    public void init() {
        bubbleImage = ImageManager.getBubbleImage();
        bubbleImage2 = ImageManager.getBubbleImage2();
        backGroudImage = ImageManager.getBackGroundImage();

        int color1 = ReciteInfo.getReciteInfo().getContext().getResources().getColor(R.color.en_paincolor);
        int color2 = ReciteInfo.getReciteInfo().getContext().getResources().getColor(R.color.cn_paincolor);
        paintEn=new Paint();
        paintEn.setColor(color1);
        paintEn.setAlpha(140);
        paintEn.setTextSize(28);
        paintCn=new Paint();
        paintCn.setColor(color2);
        paintCn.setAlpha(140);
        paintCn.setTextSize(28);
        initGame();//初始化游戏

        beginGame();

        makeBubble();

        initing = false;
    }

    private Boolean productionBubble=true;  //可以生产Bubble
    private Boolean stall=true;  //生成,停顿
    private ArrayList<Bubble> newBubbles=new ArrayList<>();
    public void makeBubble() {  //生存的时候做停顿
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (GamingInfo.getGamingInfo().isGaming()) {
                    if (!stall){
                        try {
                            Thread.sleep(800);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        stall=true;
                    }
                }

            }

        }.start();
    }
    public void makeBubble1() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (GamingInfo.getGamingInfo().isGaming() && (allBubble1.size() < 18)) {
                    Bubble bubble = new Bubble(bubbleImage,bubbleImage2,productionBubbleText());
                    bubble.setBx(new Random().nextInt(GamingInfo.getGamingInfo().getScreenWidth() - 200));
                    bubble.setBy(0);
                    allBubble1.add(bubble);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }.start();
    }

    public void bubbleDow(Canvas canvas) {//修改,当前一个下落某些距离后,第二个加随机数下落,?增加重力下落,用树处理!!!!!!!!
        canvas.drawBitmap(backGroudImage, 0, 0, null);//可能是图片被回收,后台后可能会挂
        for (int i = 0; i < allBubble1.size(); i++) {
            for (int k = i - 1; k >= 0; k--) {
                bubbleCrash(allBubble1.get(i), allBubble1.get(k), i, k);//k球是i球下落之前的球
            }
            for (int j = i + 1; j < allBubble1.size(); j++) {
                bubbleCrash(allBubble1.get(i), allBubble1.get(j), i, j);
            }
            allBubble1.get(i).onDraw(canvas, null);
        }
        if (!deleteSomeBubble.isEmpty()) {
            for (Bubble b : deleteSomeBubble) {
                allBubble1.remove(b);
            }
            deleteSomeBubble.clear();
        }
        if(canDow()&&stall){
            Bubble bubble = new Bubble(bubbleImage,bubbleImage2,productionBubbleText());
            bubble.setBx(new Random().nextInt(GamingInfo.getGamingInfo().getScreenWidth() - 200));
            bubble.setBy(0);
            allBubble1.add(bubble);
            stall=false;
        }
    }

    public Boolean canDow(){
        return allBubble1.size()<18;
    }
    public void bubbleCrash(Bubble bubble1, Bubble bubble2, int i, int j) {
        float x2 = bubble2.getBx();
        float y2 = bubble2.getBy();
        float x1 = bubble1.getBx();
        float y1 = bubble1.getBy();
        float a = Math.abs(x2 - x1);
        float b = Math.abs(y2 - y1);
        float distance = (float) Math.sqrt((a * a + b * b)); //两个圆的距离
        if (distance < 200) {
            if (y1 - 20 < y2) {
                if (x1 < x2) {//上面的球右下有碰撞
                    bubble1.setContactRIGHT(bubble2);
                } else {
                    bubble1.setContactLEFT(bubble2);
                }
            }
        }
    }

    private Paint paintEn;
    private Paint paintCn;
    private ArrayList<BubbleText> strs =new ArrayList<>();
    int  i=0;
    private void proproductionText(){//获得单词的文本
        for (int j=i;j< i+6;j++){
            Word word= ReciteInfo.taskWords.get(j);
            LogUtils.v2("第i="+j+" "+word.getEn());
            strs.add(new BubbleText(j,inciseEnStr(word.getEn()),paintEn));
            strs.add(new BubbleText(j,inciseCnStr(word.getCn()),paintCn));
        }i=i+6;
    }

    public String[] inciseEnStr(String s) {
        String s1=s.split(" ")[0];
        String[] sss1 =new String[2];
        if (s1.length()>11){
            sss1[0]=s1.substring(0,12);
            if (s1.length()>22){
                sss1[1]=s1.substring(12,20)+"..";
            }else {
                sss1[1]=" "+s1.substring(12,s1.length());
            }

        }else {
            sss1[0]=" "+s1;
            sss1[1]=" ";
        }
        return sss1;
    }
    public String[] inciseCnStr(String s) {
        String s1=s.split("；|，")[0];
        String[] sss1 =new String[2];
        if (s1.length()>9){
            sss1[0]=" "+s1.substring(0,6);
            if (s1.length()>12){
                sss1[1]=s1.substring(6,12);
            }else {
                sss1[1]="  "+s1.substring(6,s1.length());
            }

        }else {
            sss1[0]=" "+s1;
            sss1[1]=" ";
        }
        return sss1;
    }
    public BubbleText  productionBubbleText(){//传单词文本给bubble
        BubbleText string;
        if (strs.isEmpty()){
            proproductionText();
        }
        int i2=new Random().nextInt(12)%strs.size();
        string=strs.get(i2);

        strs.remove(i2);
        LogUtils.v2("传BUbble的时候"+i2+" "+string.toString() +"剩 "+strs.size());
        return string;
    }
    public void setDeleteSomeBubble(Bubble a, Bubble b) {
        deleteSomeBubble.clear();
        deleteSomeBubble.add(a);
        deleteSomeBubble.add(b);
    }

    /**
     * 初始化游戏
     */
    private void initGame() {

    }

    /**
     * 停止游戏
     */
    public void stop() {


    }

    /**
     * 开始游戏
     */
    private void beginGame() {
        //开始

    }

    /**
     * 初始化所有音效
     */
    private void initSound() {

    }

    /**
     * 初始化进度条画面
     */
    private void initProgress() {
    }

    /**
     * 关闭进度条画面
     */
    private void closeProgress() {

    }
}
