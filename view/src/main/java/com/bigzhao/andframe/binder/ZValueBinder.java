package com.bigzhao.andframe.binder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.view.interfaces.IViewHolder;
import com.bigzhao.andframe.binder.tools.ValueBundle;
import com.bigzhao.andframe.util.ExceptionHandler;
import com.bigzhao.andframe.util.Ref;
import com.bigzhao.andframe.util.StringUtil;
import com.bigzhao.andframe.util.T;
import com.bigzhao.andframe.util.ZUtil;
import com.bigzhao.andframe.view.adapter.ZAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roy on 15-3-28.
 */
public class ZValueBinder extends ZBaseBinder {
    static {
        init();
    }

    private static T.Map3<Class<?>,Class<?>,T.Proc2<View,Object>> viewValueBinders;
    private static HashMap<Class<?>,T.Func1<Object,View>> viewValueGetters;
    private static HashMap<String,T.Func1<Object,Object>> converters;
    private static ArrayList<T.Proc2<IViewHolder,JSONObject>> valueBinders;
    private static ArrayList<T.Proc2<Map<String,Object>,IViewHolder>> valueGetters;


    public static void bindValue(IViewHolder holder,Object obj){
        if (obj==null) return;
        Object json=JSON.toJSON(obj);
        if (json instanceof JSONObject) bindValue(holder,(JSONObject)json);
    }

    public static void bindValue(IViewHolder holder,JSONObject json){
        for (T.Proc2<IViewHolder,JSONObject> e:valueBinders){
            e.f(holder,json);
        }
    }

    public static boolean bindValue(View v,String property,Object value){
        return bindValue(v,property,value,null);
    }

    public static<T extends Adapter> boolean bindValue(AdapterView<T> v,ZBind bind,Object value){
        try {
            Constructor<? extends ZAdapter> ctor=bind.adapter().getConstructor(Context.class,Class.class,Object.class);
            ZAdapter a=ctor.newInstance(v.getContext(), bind.viewHolder(),value);
            v.setAdapter((T)a);
        } catch (NoSuchMethodException|InvocationTargetException|InstantiationException|IllegalAccessException  e) {
            ExceptionHandler.ThrowNoIgnore(e);
        }
        return false;
    }

    public static boolean bindValue(View v,Class<?> viewType,Object value,Class<?> valueType){
        T.Proc2<View, Object> o = viewValueBinders.get(viewType, valueType);
        if (o != null) {
            o.f(v, value);
            return true;
        }
        return false;
    }

    public static boolean bindValue(View v,String property,Object value,Class<?> valueType){
        if (TextUtils.isEmpty(property)) {
            for (Class<?> vc = v.getClass(); View.class.isAssignableFrom(vc); vc = vc.getSuperclass()) {
                if (viewValueBinders.get(vc)==null) continue;

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



    public static void registerViewValueBinder(Class<? extends View> viewType, Class<?> valueType, T.Proc2<View, Object> proc){
        viewValueBinders.put(viewType, valueType, proc);
    }

    public static void registerViewValueGetter(Class<? extends View> viewType, T.Func1<Object, View> proc){
        viewValueGetters.put(viewType, proc);
    }


    public static void registerViewValueBinder(Class<? extends View> viewType, final Class<?> valueType, final String property){
        registerViewValueBinder(viewType, valueType, new T.Proc2<View, Object>() {
            @Override
            public void f(View view, Object o) {
                bindValue(view, property, o, valueType);
            }
        });;
    }

    public static void registerViewValueGetter(Class<? extends View> viewType, final String property){
        registerViewValueGetter(viewType, new T.Func1<Object, View>() {
            @Override
            public Object f(View view) {
                try {
                    Method f = view.getClass().getMethod("get" + StringUtil.toBigCamel(property));
                    return f.invoke(view);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    ExceptionHandler.ThrowNoIgnore(e);
                }
                return null;
            }
        });;
    }

    public static void registerValueBinder(T.Proc2<IViewHolder,JSONObject> binder){
        valueBinders.add(binder);
    }
    public static void registerValueGetter(T.Proc2<Map<String,Object>,IViewHolder> getter){
        valueGetters.add(getter);
    }

    public static void registerConverter(String name,T.Func1<Object,Object> proc){
        converters.put(name,proc);
    }

    public static Map<String,Object> createForm(IViewHolder vh){
        Map<String,Object> map=new HashMap<>();
        for (T.Proc2<Map<String,Object>,IViewHolder> e:valueGetters){
            e.f(map,vh);
        }
        return map;
    }

    public static void init(){
        viewValueBinders =new T.Map3<>();
        viewValueGetters =new HashMap<>();
        valueBinders=new ArrayList<>();
        valueGetters=new ArrayList<>();

        registerViewValueBinder(TextView.class, CharSequence.class, "text");
        registerViewValueBinder(TextView.class, Integer.TYPE, "text");
        registerViewValueBinder(TextView.class, Integer.class, "text");
        registerViewValueBinder(TextView.class, ColorStateList.class, "textColor");
        registerViewValueBinder(ImageView.class, Drawable.class, "ImageDrawable");
        registerViewValueBinder(ImageView.class, Integer.TYPE, "ImageResource");
        registerViewValueBinder(ImageView.class, Integer.class, "ImageResource");
        registerViewValueBinder(ImageView.class, Bitmap.class, "ImageBitmap");

        registerViewValueGetter(TextView.class, "text");
        registerViewValueGetter(ImageView.class, "drawable");

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

        registerValueBinder(new T.Proc2<IViewHolder, JSONObject>() {
            @Override
            public void f(IViewHolder holder, JSONObject json) {
                ZOptimizedViewHolder zh=holder.getViewHolder();
                for (ZOptimizedViewHolder.Item e:zh){
                    for (ValueBundle.Item f:e.value){
                        if (f.type!=ValueBundle.Item.TYPE_ALL&&f.type!=ValueBundle.Item.TYPE_IN) continue;
                        Object obj=json.get(f.name);
                        if (obj==null) continue;
                        if (f.converter!=null){
                            T.Func1<Object,Object> call=converters.get(f.converter);
                            if (call==null) continue;
                            obj=call.f(obj);
                        }
                        if (obj==null) continue;
                        if (TextUtils.isEmpty(f.property)&&e.view instanceof AdapterView&&(obj instanceof Iterable||obj.getClass().isArray())){
                            bindValue((AdapterView<Adapter>)e.view,e.bind,obj);
                        }else {
                            bindValue(e.view, f.property, obj);
                        }
                    }
                }
            }
        });

        registerValueGetter(new T.Proc2<Map<String, Object>, IViewHolder>() {
            @Override
            public void f(Map<String, Object> map, IViewHolder vh) {
                for (ZOptimizedViewHolder.Item e:vh.getViewHolder()){
                    for (ValueBundle.Item v:e.value){
                        if (v.type!= ValueBundle.Item.TYPE_OUT&&v.type!= ValueBundle.Item.TYPE_ALL) continue;
                        if (TextUtils.isEmpty(v.property)){
                            Class<?> c=e.view.getClass();
                            T.Func1<Object,View> g= viewValueGetters.get(c);
                            while(g==null&&View.class.isAssignableFrom(c)) {
                                c=c.getSuperclass();
                                g= viewValueGetters.get(c);
                            }
                            if (g==null) continue;
                            Object value=g.f(e.view);
                            map.put(v.name,value);
                        }else{
                            try {
                                Class<?> c=e.view.getClass();
                                Method f=c.getMethod("get"+StringUtil.toBigCamel(v.property));
                                Object value=f.invoke(e.view);
                                map.put(v.name,value);
                            } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e1) {
                                ExceptionHandler.Throw(e1);
                            }
                        }
                    }
                }
            }
        });
    }
}
