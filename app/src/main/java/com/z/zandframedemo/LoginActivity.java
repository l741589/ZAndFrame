package com.z.zandframedemo;

import android.view.View;

import com.z.andframe.binder.annotions.ZBind;
import com.z.andframe.binder.annotions.ZEvent;
import com.z.andframe.view.ZBaseActivity;

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
