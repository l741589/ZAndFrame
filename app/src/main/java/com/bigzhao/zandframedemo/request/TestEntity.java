package com.bigzhao.zandframedemo.request;

import java.util.List;

/**
 * Created by Roy on 15-3-25.
 */
public class TestEntity {

    private int num = 2000;
    private String text = "SFGFRE";
    private List<TestEntity> obj;

    public  List<TestEntity> getObj() {
        return obj;
    }

    public void setObj( List<TestEntity> obj) {
        this.obj = obj;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
