package com.bigzhao.andframe.binder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.binder.tools.ValueBundle;
import com.bigzhao.andframe.util.T;
import com.bigzhao.andframe.view.interfaces.IMenuInjectable;
import com.bigzhao.andframe.view.interfaces.ITemplateViewHolder;
import com.bigzhao.andframe.view.interfaces.IViewHolder;
import com.bigzhao.andframe.binder.tools.ContextMenuCreator;
import com.bigzhao.andframe.binder.tools.EventListenerStub;
import com.bigzhao.andframe.util.ExceptionHandler;
import com.bigzhao.andframe.util.Ref;
import com.bigzhao.andframe.util.ZUtil;
import com.bigzhao.andframe.view.template.ViewHolderTemplate;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by Roy on 15-3-18.
 */
public class ZBinder extends ZValueBinder{

    public static class Config{
        public static String resourceNamePrefix="_$@#";
    }

    public static void bindEvent(Object holder,EventListenerStub stb){
        ZEventBinder.bindEvent(holder,stb);
    }

    public static ZOptimizedViewHolder inject(IViewHolder holder, IMenuInjectable mi){
        return inject(ZBinder.getContext(holder),holder,mi);
    }


    private static Context getContext(Object object){
        if (object instanceof Context) return (Context)object;
        if (object instanceof Fragment) return ((Fragment)object).getActivity();
        if (object instanceof android.support.v4.app.Fragment) return ((android.support.v4.app.Fragment)object).getActivity();
        if (object instanceof ViewHolderTemplate) return ((ViewHolderTemplate)object).getActivity();
        if (object instanceof ITemplateViewHolder) return ((ITemplateViewHolder)object).getTemplate().getActivity();
        return context;
    }

    public static ZOptimizedViewHolder inject(Context context, IViewHolder holder, IMenuInjectable mi){
        Class<?> cls=holder.getClass();
        if (cls.isAnnotationPresent(ZBind.class)){
            ZBind bind=cls.getAnnotation(ZBind.class);
            ZInjector.ResourceName rn= ZInjector.getName(bind, cls);
            if (rn.id!=0) holder.setContentView(LayoutInflater.from(context).inflate(rn.id,null));
            if (mi!=null){
                rn= ZInjector.getMenuName(bind, cls);
                mi.setOptionsMenu(rn.id);
            }
        }
        List<Field> fields= Ref.getAllFields(cls, "android.", "java.", "com.bigzhao.andframe.");
        ZOptimizedViewHolder vh=new ZOptimizedViewHolder();
        holder.setViewHolder(vh);
        View root=holder.getContentView();
        for(Field f:fields){
            //if (!f.isAnnotationPresent(ZBind.class)) continue;
            Object obj= ZInjector.inject(holder,f, root);
            if (obj==null) continue;
            try {
                f.setAccessible(true);
                f.set(holder,obj);
            } catch (IllegalAccessException e) {
                ExceptionHandler.Throw(e);
            }
        }

        return vh;
    }


    public static void registerResourceName(Class<?> cls,String name){
       ZInjector.registerResourceName(cls,name);
    }

    public static void registerResourceGetter(T.Func2<Object,Class<?>,Integer> getter, String... names) {
        ZInjector.registerResourceGetter(getter,names);
    }

    public static void registerInjector(Class<?> type,T.Func3<Object,Object,Field,View> injector,Class<? extends Annotation>...filters){
        ZInjector.registerInjector(type,injector,filters);
    }

}
