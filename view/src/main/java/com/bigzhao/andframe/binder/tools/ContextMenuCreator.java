package com.bigzhao.andframe.binder.tools;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.View;

import com.bigzhao.andframe.view.interfaces.IMenuInjectable;
import com.bigzhao.andframe.util.Ref;

/**
 * Created by Roy on 15-3-19.
 */
public class ContextMenuCreator implements View.OnCreateContextMenuListener{

    private int resId;

    public ContextMenuCreator(int resId){
        this.resId=resId;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Context c=v.getContext();
        if (c instanceof Activity){
            ((Activity)c).getMenuInflater().inflate(resId,menu);
            try {
                IMenuInjectable mi = (IMenuInjectable) Ref.call(c, "getTemplate");
                mi.injectMenuEvent(menu);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}