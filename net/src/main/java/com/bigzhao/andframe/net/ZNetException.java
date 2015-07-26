package com.bigzhao.andframe.net;

/**
 * Created by Roy on 15-3-25.
 */
public class ZNetException extends Exception{

    public static final int CODE_NULL_RESPONSE = -100;

    private int code;

    public ZNetException(String desc,int code){
        super(code+" "+desc);
        this.code=code;
    }

    public int getStatusCode() {
        return code;
    }
}
