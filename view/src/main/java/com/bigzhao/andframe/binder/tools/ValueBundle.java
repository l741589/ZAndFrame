package com.bigzhao.andframe.binder.tools;

import android.text.TextUtils;

import java.util.LinkedList;

/**
 * Created by Roy on 15-3-28.
 */
public class ValueBundle extends LinkedList<ValueBundle.Item>{

    public static class Item{
        public static final int TYPE_ALL = 0;
        public static final int TYPE_IN = 1;
        public static final int TYPE_OUT = 2;
        public static final int TYPE_NONE = -1;
        public final int type;
        public final String name;
        public final String converter;
        public final String property;
        public Item(int type,String name,String converter,String property){
            this.type=type;
            this.name=name;
            this.property=property;
            this.converter=converter;
        }
    }

    public ValueBundle(String desc){
        if (TextUtils.isEmpty(desc)) return;
        String[] ss=desc.split(",");
        for (String s:ss){
            int type=0;
            switch(s.charAt(0)){
                case '>':type= Item.TYPE_IN;s=s.substring(1);break;
                case '<':type= Item.TYPE_OUT;s=s.substring(1);break;
                case '*':type= Item.TYPE_NONE;break;
                default:type= Item.TYPE_ALL;break;
            }
            if (type<0) continue;
            String[] p=s.split(":");
            String[] c=p[0].split("\\.");
            if (p.length==1) {
                if (c.length==1) add(new Item(type,c[0].trim(),null,null));
                else add(new Item(type,c[0].trim(),c[1].trim(),null));
            }else{
                if (c.length==1) add(new Item(type,c[0].trim(),null,p[1].trim()));
                else add(new Item(type,c[0].trim(),c[1].trim(),p[1].trim()));
            }
        }
    }
}
