package com.z.zandframedemo.request;

import com.z.andframe.net.Methods;
import com.z.andframe.net.request.JsonRequest;
import com.z.andframe.net.request.Request;

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
