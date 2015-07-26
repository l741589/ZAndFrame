package com.bigzhao.andframe.binder;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigzhao.andframe.view.interfaces.IViewHolder;
import com.bigzhao.andframe.binder.tools.ValueBundle;
import com.bigzhao.andframe.util.ExceptionHandler;
import com.bigzhao.andframe.util.Ref;
import com.bigzhao.andframe.util.StringUtil;
import com.bigzhao.andframe.util.T;
import com.bigzhao.andframe.util.ZUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Roy on 15-3-28.
 */
public class ZValueBinder extends ZBaseBinder {
    static {
        init();
    }

    private static T.Map3<Class<?>,Class<?>,T.Proc2<View,Object>> valuebinders;
    private static HashMap<String,T.Func1<Object,Object>> converters;

    public static void bindValue(IViewHolder holder,Object obj){
        if (obj==null) return;
        Object json=JSON.toJSON(obj);
        if (json instanceof JSONObject) bindValue(holder,(JSONObject)json);
    }

    public static void bindValue(IViewHolder holder,JSONObject json){
        ZOptimizedViewHolder zh=holder.getViewHolder();
        for (ZOptimizedViewHolder.Item e:zh){
            for (ValueBundle.Item f:e.value){
                Object obj=json.get(f.name);
                if (obj==null) continue;
                if (f.converter!=null){
                    T.Func1<Object,Object> call=converters.get(f.converter);
                    if (call==null) continue;
                    obj=call.f(obj);
                }
                bindValue(e.view,f.property,obj);
            }
        }
    }

    public static boolean bindValue(View v,String property,Object value){
        return bindValue(v,property,value,null);
    }

    public static boolean bindValue(View v,Class<?> viewType,Object value,Class<?> valueType){
        T.Proc2<View, Object> o = valuebinders.get(viewType, valueType);
        if (o != null) {
            o.f(v, value);
            return true;
        }
        return false;
    }

    public static boolean bindValue(View v,String property,Object value,Class<?> valueType){
        if (TextUtils.isEmpty(property)) {
            OUTER:
            for (Class<?> vc = v.getClass(); View.class.isAssignableFrom(vc); vc = vc.getSuperclass()) {
                if (valuebinders.get(vc)==null) continue;

                for (Class<?> xc = value.getClass(); Object.class.isAssignableFrom(xc); xc = xc.getSuperclass()) {
                    if (bindValue(v,vc,value,xc)) return true;

                    Class<?> pc= ZUtil.primitiveToBoxed(xc);
                    if (pc!=xc&&bindValue(v,vc,value,pc)) return true;

                    pc= ZUtil.boxedToPrimitive(xc);
                    if (pc!=xc&&bindValue(v,vc,value,pc)) return true;

                    if (Object.class.equals(xc)) break;

                    Class<?>[] is=xc.getInterfaces();
                    for (Class<?> i:is) if (bindValue(v,vc,value,i)) return true;
                }

            }
        }else{
            try {
                Class<?> vc=v.getClass();
                Method m=Ref.getConpatibleMethod(vc, "set" + StringUtil.toBigCamel(property), valueType == null ? value.getClass() : valueType);
                m.invoke(v,value);
                return true;
            } catch (InvocationTargetException|IllegalAccessException e) {
                ExceptionHandler.Throw(e);
            }
        }
        return false;
    }



    public static void registerValueBinder(Class<? extends View> viewType,Class<?> valueType,T.Proc2<View,Object> proc){
        valuebinders.put(viewType,valueType,proc);
    }

    public static void registerValueBinder(Class<? extends View> viewType, final Class<?> valueType,final String property){
        registerValueBinder(viewType,valueType,new T.Proc2<View, Object>() {
            @Override
            public void f(View view, Object o) {
               bindValue(view,property,o,valueType);
            }
        });
    }

    public static void registerConverter(String name,T.Func1<Object,Object> proc){
        converters.put(name,proc);
    }

    public static void init(){
        valuebinders=new T.Map3<>();
        registerValueBinder(TextView.class,CharSequence.class,"text");
        registerValueBinder(TextView.class,Integer.TYPE,"text");
        registerValueBinder(TextView.class, ColorStateList.class,"textColor");

        converters=new HashMap<>();
        registerConverter("toString",new T.Func1<Object, Object>() {
            @Override
            public Object f(Object o) {
                return o+"";
            }
        });
        registerConverter("toColor",new T.Func1<Object, Object>() {
            @Override
            public Object f(Object o) {
                if (o instanceof Integer){
                    int x=(Integer)o;
                    if ((x&0xff000000)>0) return x;
                    return x|0xff000000;
                }else{
                    String s=o.toString();
                    return Color.parseColor(s);
                }
            }
        });
    }
}
