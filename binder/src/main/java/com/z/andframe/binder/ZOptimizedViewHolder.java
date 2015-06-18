package com.z.andframe.binder;

import android.view.View;

import com.z.andframe.binder.annotions.ZBind;
import com.z.andframe.view.interfaces.IViewHolder;
import com.z.andframe.binder.tools.ValueBundle;

import java.util.LinkedList;

/**
 * Created by Roy on 15-3-18.
 */
public class ZOptimizedViewHolder extends  LinkedList<ZOptimizedViewHolder.Item> implements IViewHolder{

    @Override
    public ZOptimizedViewHolder getViewHolder() {
        return this;
    }

    public static class Item{
        public View view;
        public ZBind bind;
        public String name;
        public ValueBundle value;

        public Item(String name,View v, ZBind b,ValueBundle value){
            this.view=v;
            this.bind=b;
            this.name=name;
            this.value=value;
        }
    }

    public static ValueBundle getValue(String name,ZBind bind){
        if (!bind.value().isEmpty()) return new ValueBundle(bind.value());
        ValueBundle b=new ValueBundle(null);
        b.add(new ValueBundle.Item(ValueBundle.Item.TYPE_ALL,name,null,null));
        return b;
    }

    public void put(String name, View view, ZBind bind){
        ValueBundle b=getValue(name, bind);
        add(new Item(name, view, bind, b));
    }

}
