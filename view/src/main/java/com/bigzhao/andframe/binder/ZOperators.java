package com.bigzhao.andframe.binder;

import android.app.Activity;
import android.app.Fragment;

import com.bigzhao.andframe.util.T;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Roy on 15-3-19.
 */
public class ZOperators {

    public static T.Func1<String,Class<?>> classNameOperator=new T.Func1<String, Class<?>>() {
        @Override
        public String f(Class<?> cls) {
            String name=cls.getSimpleName();
            if (Activity.class.isAssignableFrom(cls)){
                if (name.endsWith("Activity")) {
                    if (name.startsWith("Activity")) name=name.substring(0,name.length()-8);
                    else name="Activity"+name.substring(0,name.length()-8);
                } else {
                    if (!name.startsWith("Activity")) name="Activity"+name;
                }
            }else if (Fragment.class.isAssignableFrom(cls)|| android.support.v4.app.Fragment.class.isAssignableFrom(cls)){
                if (name.endsWith("Fragment")) {
                    if (name.startsWith("Fragment")) name=name.substring(0,name.length()-8);
                    else name="Fragment"+name.substring(0,name.length()-8);
                } else {
                    if (!name.startsWith("Fragment")) name="Fragment"+name;
                }
            }
            return name.replaceAll("([a-z0-9])([A-Z])","$1_$2").toLowerCase();
        }
    };

    public static T.Func1<String,Class<?>> optionsMenuNameOperator=new T.Func1<String, Class<?>>() {
        @Override
        public String f(Class<?> cls) {
            String name=cls.getSimpleName();
            if (Activity.class.isAssignableFrom(cls)||Fragment.class.isAssignableFrom(cls)|| android.support.v4.app.Fragment.class.isAssignableFrom(cls)){
                if (name.endsWith("Activity")||name.endsWith("Fragment")){
                    if (name.startsWith("Menu")) name=name.substring(0,name.length()-8);
                    else name="Menu"+name.substring(0,name.length()-8);
                }else{
                    if (!name.startsWith("Menu")) name="Menu"+name;
                }
            }
            return name.replaceAll("([a-z0-9])([A-Z])","$1_$2").toLowerCase();
        }
    };

    public static T.Func1<String,Field> contextMenuNameOperator =new T.Func1<String, Field>() {
        @Override
        public String f(Field field) {
            Class<?> cls=field.getDeclaringClass();
            String name=cls.getSimpleName();
            if (Activity.class.isAssignableFrom(cls)||Fragment.class.isAssignableFrom(cls)|| android.support.v4.app.Fragment.class.isAssignableFrom(cls)){
                if (name.endsWith("Activity")||name.endsWith("Fragment")){
                    if (name.startsWith("Menu")) name=name.substring(0,name.length()-8);
                    else name="Menu"+name.substring(0,name.length()-8);
                }else{
                    if (!name.startsWith("Menu")) name="Menu"+name;
                }
            }
            name+="_"+field.getName();
            return name.replaceAll("([a-z0-9])([A-UW-Z]|V(?!iew))","$1_$2").toLowerCase();
        }
    };

    public static T.Func1<String,Field> fieldNameOperator=new T.Func1<String,Field>() {
        @Override
        public String f(Field s) {
            return s.getName();
        }
    };

    public static T.Func1<T.Tuple2<String,Integer>,Method> methodNameOperator=new T.Func1<T.Tuple2<String,Integer>,Method>() {
        @Override
        public T.Tuple2<String,Integer> f(Method m) {
            String name=m.getName();
            int sp=0;
            for (int i=name.length()-2;i>0;--i){
                char c=name.charAt(i);
                if (c=='$'||c=='_') {
                    sp = i;
                    break;
                }
            }
            if (sp==0)  return T.tuple(name,0);
            return T.tuple(name.substring(0,sp),ZEventBinder.getEventId(name.substring(sp+1)));
        }
    };

}
