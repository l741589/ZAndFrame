package com.bigzhao.zandframedemo.request;

import com.bigzhao.andframe.net.Methods;
import com.bigzhao.andframe.net.request.JsonRequest;
import com.bigzhao.andframe.net.request.Request;

/**
 * Created by Roy on 15-3-25.
 */
public class Test_Get extends Request<TestEntity> {
    @Override
    protected void init() {
        //setMethod(Methods.GET);
    }

    private int number = 1000;
    private String text = "ABCDEFG";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
