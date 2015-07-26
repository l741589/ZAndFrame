package com.bigzhao.zandframedemo;

import android.view.View;

import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.binder.annotions.ZEvent;
import com.bigzhao.andframe.view.ZBaseActivity;

/**
 * Created by Roy on 15-4-20.
 */
@ZBind
public class LoginActivity extends ZBaseActivity{

    @ZEvent
    void _button3_Click(View v){
        setResult(RESULT_OK);
        getTemplate().setLoginState(true);
        finish();

    }
}
