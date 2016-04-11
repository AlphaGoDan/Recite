package com.example.kimasi.recite.module;

/**
 * 单词
 * Created by kimasi on 2016/4/6.
 */
public class Word {

    private String en;
    private String cn;
    private Integer proficiency;  //熟悉值,通过这个值区分完成跟通过
    private Boolean accomplish;   //完成
    private String pass;          //通过
    private String plan;         //标记单词是计划内的
    private Integer index ;

    public Word(String en, String cn, Integer proficiency, Boolean accomplish, String pass,String plan,Integer index) {
        this.en = en;
        this.cn = cn;
        this.accomplish = accomplish;
        this.proficiency = proficiency;
        this.pass = pass;
        this.plan = plan;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Word{" +
                "en='" + en + '\'' +
                ", cn='" + cn + '\'' +
                ", proficiency=" + proficiency +
                ", accomplish=" + accomplish +
                ", pass='" + pass + '\'' +
                ", plan='" + plan + '\'' +
                ", index=" + index +
                '}';
    }

    public void setEn(String en) {
        this.en = en;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public void setAccomplish(Boolean accomplish) {
        this.accomplish = accomplish;
    }

    public void setFamilar(int proficiency) {
        this.proficiency = proficiency;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEn() {
        return en;
    }

    public String getCn() {
        return cn;
    }

    public int getFamilar() {
        return proficiency;
    }

    public String getPass() {
        return pass;
    }

    public Boolean getAccomplish() {
        return accomplish;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
