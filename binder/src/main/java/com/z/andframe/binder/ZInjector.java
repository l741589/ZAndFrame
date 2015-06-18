package com.z.andframe.binder;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.BaseBundle;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.z.andframe.binder.annotions.ZBind;
import com.z.andframe.util.ExceptionHandler;
import com.z.andframe.util.ZStream;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Roy on 15-3-18.
 */
public class ZInjector extends ZBaseBinder{

    public static class ResourceName{
        public String pkg=context.getPackageName();
        public String type;
        public String name;
        public int id;

        public ResourceName(String name,String type){
            if (name.isEmpty()){
                pkg=this.type=this.name=null;
                id=0;
                return;
            }
            if (ZBinder.Config.resourceNamePrefix.contains(name.substring(0,1))) name=name.substring(1);
            if (name.contains(":")) {
                int i=name.indexOf(':');
                pkg=name.substring(0,i);
                name=name.substring(i+1);
            }
            this.type=type;
            if (type==null&&name.contains("/")) {
                int i=name.indexOf('/');
                this.type=name.substring(0,i);
                name=name.substring(i+1);
            }
            this.name=name;
            this.id=resources.getIdentifier(this.name,this.type,this.pkg);
        }

        public ResourceName(int id){
            this.id=id;
            if (id==0){
                pkg=type=name=null;
                return;
            }
            pkg=resources.getResourcePackageName(id);
            type=resources.getResourceTypeName(id);
            name=resources.getResourceEntryName(id);
        }

    }

    static{
        initValueGetter();
    }


    /** inject resource*/
    public static Object inject(Object holder,Field field,View root){
        if (field.getType().isAssignableFrom(Context.class)){
            if (root.getContext()!=null) return root.getContext();
            if (holder instanceof Context) return (Context)holder;
            return context;
        }
        ResourceName rn=getName(field.getAnnotation(ZBind.class),field);
        if (rn.id==0) return null;
        if (rn.type.equals("id")){
            View v=root.findViewById(rn.id);
            return v;
        }
        return getValue(field.getType(),rn.type,rn.id);
    }

    /**inject other tyoe*/

     public static ResourceName getName(ZBind bind, Field field){
        if (bind.id()!=0) return new ResourceName(bind.id());
        String name;
        if (bind.name().isEmpty()) name=ZOperators.fieldNameOperator.f(field);
        else name=bind.name();
        ResourceName rn=new ResourceName(name,getType(field.getType()));
        return rn;
    }


    public static ResourceName getName(ZBind bind, Class<?> cls){
        if (bind.id()!=0) return new ResourceName(bind.id());
        String name;
        if (bind.name().isEmpty()) name=ZOperators.classNameOperator.f(cls);
        else name=bind.name();
        ResourceName rn=new ResourceName(name,"layout");

        return rn;
    }

    public static ResourceName getMenuName(ZBind bind, Class<?> cls){
        if (bind.menuId()!=0) return new ResourceName(bind.menuId());
        String name;
        if (bind.menu().isEmpty()) name=ZOperators.optionsMenuNameOperator.f(cls);
        else name=bind.menu();
        ResourceName rn=new ResourceName(name,"menu");
        return rn;
    }

    public static ResourceName getMenuName(ZBind bind, Field field){
        if (bind.menuId()!=0) return new ResourceName(bind.menuId());
        String name;
        if (bind.menu().isEmpty()) name=ZOperators.contextMenuNameOptator.f(field);
        else name=bind.menu();
        ResourceName rn=new ResourceName(name,"menu");
        return rn;
    }

    public static String getType(Class<?> cls){
        if (View.class.isAssignableFrom(cls)) return "id";
        if (CharSequence.class.isAssignableFrom(cls)) return "string";
        if (Drawable.class.isAssignableFrom(cls)) return "drawable";
        if (ColorStateList.class.isAssignableFrom(cls)) return "color";
        if (Float.class.isAssignableFrom(cls)) return "dimen";
        if (Animation.class.isAssignableFrom(cls)||Interpolator.class.isAssignableFrom(cls)) return "anim";
        if (Animator.class.isAssignableFrom(cls)) return "animator";
        if (Boolean.class.isAssignableFrom(cls)||Boolean.TYPE.isAssignableFrom(cls)) return "bool";
        if (cls.equals(Integer.class)||cls.isAssignableFrom(Integer.TYPE)) return "integer";
        if (XmlPullParser.class.isAssignableFrom(cls)) return "xml";
        if (Transition.class.isAssignableFrom(cls)) return "transition";
        if (cls.isArray()) return "array";
        return "raw";
    }

    private static interface ValueGetter{
        Object get(Class<?> cls,int id);
    }

    private static HashMap<String,ValueGetter> valueGetters;

    public static void putValueGetter(ValueGetter getter,String...names){
        for (String name:names) {
            valueGetters.put(name,getter);
        }
    }

    private static void initValueGetter(){
        valueGetters=new HashMap<>();
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {  return resources.getDrawable(id);  }
        }, "drawable", "mipmap", "com.z.andframe.");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {
                if (String.class.isAssignableFrom(cls)) return resources.getString(id);
                return resources.getText(id);
            }
        },"string");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {
                if (ColorStateList.class.isAssignableFrom(cls)) return resources.getColorStateList(id);
                return resources.getColor(id);
            }
        },"color");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) { return resources.getDimension(id); }
        },"dimen");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {
                if (Interpolator.class.isAssignableFrom(cls)) AnimationUtils.loadInterpolator(context,id);
                return resources.getAnimation(id);
            }
        },"anim");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) { return AnimatorInflater.loadAnimator(context,id); }
        },"animator");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) { return resources.getBoolean(id); }
        },"bool","boolean");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {return resources.getInteger(id);  }
        },"integer","int");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {
                if (XmlPullParser.class.isAssignableFrom(cls)) return resources.getXml(id);
                if (Document.class.isAssignableFrom(cls)) {
                    try {
                        InputStream is=resources.openRawResource(id);
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例
                        DocumentBuilder builder= factory.newDocumentBuilder();
                        return builder.parse(is);
                    } catch (Exception e) {
                        ExceptionHandler.Throw(e);
                    }
                }
                if (InputStream.class.isAssignableFrom(cls)){
                    return resources.openRawResource(id);
                }
                if (CharSequence.class.isAssignableFrom(cls)){
                    try {
                        return ZStream.readAllText(resources.openRawResource(id));
                    } catch (IOException e) {
                        ExceptionHandler.Throw(e);
                    }
                }
                return null;
            }
        },"xml");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {
                if (!cls.isArray()) return null;
                Class<?> ccls=cls.getComponentType();
                if (Integer.class.isAssignableFrom(ccls)||Integer.TYPE.isAssignableFrom(ccls)) return resources.getIntArray(id);
                if (String.class.isAssignableFrom(ccls)) return resources.getStringArray(id);
                if (CharSequence.class.isAssignableFrom(ccls)) return resources.getTextArray(id);
                return null;
            }
        },"array");
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {
                try {
                    if (Movie.class.isAssignableFrom(cls)) return resources.getMovie(id);
                    if (cls.equals(byte[].class))
                        return ZStream.readAllBytes(resources.openRawResource(id));
                    if (InputStream.class.isAssignableFrom(cls)) return resources.openRawResource(id);
                }catch (IOException e){
                    ExceptionHandler.Throw(e);
                }
                return null;
            }
        });
        putValueGetter(new ValueGetter() {
            @Override
            public Object get(Class<?> cls, int id) {
                return LayoutInflater.from(context).inflate(id,null);
            }
        },"layout");
        android.view.animation.Interpolator d;
        putValueGetter(new ValueGetter() {
            @Override
            @SuppressLint("NewApi")
            public Object get(Class<?> cls, int id) {
                if (Build.VERSION.SDK_INT>=19)
                    return TransitionInflater.from(context).inflateTransition(id);
                return null;
            }
        },"transition");
    }

    public static Object getValue(Class<?> cls,String type,int id){
        Object ret=null;
        try {
            ValueGetter vg=valueGetters.get(type);
            if (vg!=null){
                ret=vg.get(cls,id);
            }
        }catch(Resources.NotFoundException e){
        }catch (Exception e){
            ExceptionHandler.Throw(e);
        }
        if (ret==null){
            if (Integer.class.isAssignableFrom(cls)||Integer.TYPE.isAssignableFrom(cls)){
                return id;
            }
        }
        return ret;
    }
}
