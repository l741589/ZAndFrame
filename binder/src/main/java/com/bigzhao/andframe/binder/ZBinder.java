package com.bigzhao.andframe.binder;

import android.app.Activity;
import android.view.View;

import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.view.interfaces.IMenuInjectable;
import com.bigzhao.andframe.binder.tools.ContextMenuCreator;
import com.bigzhao.andframe.util.ExceptionHandler;
import com.bigzhao.andframe.util.Ref;
import com.bigzhao.andframe.util.ZUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Roy on 15-3-18.
 */
public class ZBinder extends ZValueBinder{

    public static class Config{
        public static String resourceNamePrefix="_$@#";
    }


    public static ZOptimizedViewHolder inject(Object holder, View root){
        Class<?> cls=holder.getClass();
        List<Field> fields= Ref.getAllFields(cls, "android.", "java.", "com.bigzhao.andframe.");
        ZOptimizedViewHolder vh=new ZOptimizedViewHolder();
        for(Field f:fields){
            if (!f.isAnnotationPresent(ZBind.class)) continue;
            Object obj= ZInjector.inject(holder,f, root);
            if (obj==null) continue;
            try {
                f.setAccessible(true);
                f.set(holder,obj);
            } catch (IllegalAccessException e) {
                ExceptionHandler.Throw(e);
            }
            if (obj instanceof View) {
                ZBind bind=f.getAnnotation(ZBind.class);
                View v=(View)obj;
                vh.put(f.getName(), v, bind);
                final ZInjector.ResourceName rn= ZInjector.getMenuName(bind, f);
                if (rn!=null&&rn.id!=0) v.setOnCreateContextMenuListener(new ContextMenuCreator(rn.id));
            }
        }
        return vh;
    }



    public static ZOptimizedViewHolder inject(Activity activity,IMenuInjectable mi){
        Class<?> cls=activity.getClass();
        if (cls.isAnnotationPresent(ZBind.class)){
            ZBind bind=cls.getAnnotation(ZBind.class);
            ZInjector.ResourceName rn= ZInjector.getName(bind, cls);
            if (rn.id!=0) activity.setContentView(rn.id);
            if (mi!=null){
                rn= ZInjector.getMenuName(bind, cls);
                mi.setOptionsMenu(rn.id);
            }
        }
        return inject(activity,ZUtil.getContentView(activity));
    }



}
