package com.bigzhao.andframe.binder;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.binder.tools.ContextMenuCreator;
import com.bigzhao.andframe.util.ExceptionHandler;
import com.bigzhao.andframe.util.Ref;
import com.bigzhao.andframe.util.T;
import com.bigzhao.andframe.util.ZStream;
import com.bigzhao.andframe.view.interfaces.ITemplateViewHolder;
import com.bigzhao.andframe.view.interfaces.IViewHolder;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private static HashMap<String,T.Func2<Object,Class<?>,Integer>> valueGetters=new HashMap<>();
    private static ArrayList<T.Tuple3<Class<?>,T.Func3<Object,Object,Field,View>,Class<? extends Annotation>[]>> injectors=new ArrayList<>();
    private static boolean injectorsDirty=true;
    private static HashMap<Class<?>,String> resourceNames=new HashMap<>();

    static{
        initResourceGetter();
        initInjectors();
    }


    /** inject resource*/
    public static Object inject(Object holder,Field field,View root){
        if (injectorsDirty){
            Collections.sort(injectors,new Comparator<T.Tuple3<Class<?>, T.Func3<Object, Object, Field, View>, Class<? extends Annotation>[]>>() {
                @Override
                public int compare(T.Tuple3<Class<?>, T.Func3<Object, Object, Field, View>, Class<? extends Annotation>[]> lhs, T.Tuple3<Class<?>, T.Func3<Object, Object, Field, View>, Class<? extends Annotation>[]> rhs) {
                    if (lhs.getItem1().isAssignableFrom(rhs.getItem1())) return 1;
                    if (rhs.getItem1().isAssignableFrom(lhs.getItem1())) return -1;
                    if (lhs.getItem1()==rhs.getItem1()){
                        if (lhs.getItem3().length>rhs.getItem3().length) return 1;
                        if (lhs.getItem3().length<rhs.getItem3().length) return -1;
                    }
                    return 0;
                }
            });
            injectorsDirty=false;
        }
        for (T.Tuple3<Class<?>, T.Func3<Object, Object, Field, View>, Class<? extends Annotation>[]> e:injectors){
            boolean doFilter=true;
            for (Class<? extends Annotation> ee:e.getItem3()){
                if (field.isAnnotationPresent(ee)) {
                    doFilter=false;
                    break;
                }
            }
            if (doFilter) continue;
            if (e.getItem1().isAssignableFrom(field.getType())){
                Object o=e.getItem2().f(holder, field, root);
                if (o!=null) return o;
            }
        }
        return null;
    }

    /**inject other type*/

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
        if (bind.menu().isEmpty()) name=ZOperators.contextMenuNameOperator.f(field);
        else name=bind.menu();
        ResourceName rn=new ResourceName(name,"menu");
        return rn;
    }

    public static String getType(Class<?> cls){
        try {
            if (View.class.isAssignableFrom(cls)) return "id";
            if (CharSequence.class.isAssignableFrom(cls)) return "string";
            if (Drawable.class.isAssignableFrom(cls)) return "drawable";
            if (ColorStateList.class.isAssignableFrom(cls)) return "color";
            if (Float.class.isAssignableFrom(cls)) return "dimen";
            if (Animation.class.isAssignableFrom(cls) || Interpolator.class.isAssignableFrom(cls))
                return "anim";
            if (Animator.class.isAssignableFrom(cls)) return "animator";
            if (Boolean.class.isAssignableFrom(cls) || Boolean.TYPE.isAssignableFrom(cls))
                return "bool";
            if (cls.equals(Integer.class) || cls.isAssignableFrom(Integer.TYPE)) return "integer";
            if (XmlPullParser.class.isAssignableFrom(cls)) return "xml";
            if (Transition.class.isAssignableFrom(cls)) return "transition";
            if (cls.isArray()) return "array";
            if (resourceNames.get(cls) != null) return resourceNames.get(cls);
        }catch (NoClassDefFoundError e){
            ExceptionHandler.Hide(e);
        }
        return "raw";
    }




    public static void registerResourceName(Class<?> cls,String name){
        resourceNames.put(cls,name);
    }

    public static void registerResourceGetter(T.Func2<Object,Class<?>,Integer> getter, String... names){
        for (String name:names) {
            valueGetters.put(name,getter);
        }
    }

    public static void registerInjector(Class<?> type,T.Func3<Object,Object,Field,View> injector,Class<? extends Annotation>...filters){
        injectors.add(new T.Tuple3<Class<?>, T.Func3<Object, Object, Field, View>, Class<? extends Annotation>[]>(type,injector,filters));
        injectorsDirty=true;

    }

    private static void initResourceGetter(){
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                return resources.getDrawable(id);
            }
        }, "drawable", "mipmap", "com.bigzhao.andframe.");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                if (String.class.isAssignableFrom(cls)) return resources.getString(id);
                return resources.getText(id);
            }
        }, "string");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                if (ColorStateList.class.isAssignableFrom(cls))
                    return resources.getColorStateList(id);
                return resources.getColor(id);
            }
        }, "color");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                return resources.getDimension(id);
            }
        }, "dimen");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                if (Interpolator.class.isAssignableFrom(cls))
                    AnimationUtils.loadInterpolator(context, id);
                return resources.getAnimation(id);
            }
        }, "anim");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                return AnimatorInflater.loadAnimator(context, id);
            }
        }, "animator");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                return resources.getBoolean(id);
            }
        }, "bool", "boolean");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                return resources.getInteger(id);
            }
        }, "integer", "int");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                if (XmlPullParser.class.isAssignableFrom(cls)) return resources.getXml(id);
                if (Document.class.isAssignableFrom(cls)) {
                    try {
                        InputStream is = resources.openRawResource(id);
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        return builder.parse(is);
                    } catch (Exception e) {
                        ExceptionHandler.Throw(e);
                    }
                }
                if (InputStream.class.isAssignableFrom(cls)) {
                    return resources.openRawResource(id);
                }
                if (CharSequence.class.isAssignableFrom(cls)) {
                    try {
                        return ZStream.readAllText(resources.openRawResource(id));
                    } catch (IOException e) {
                        ExceptionHandler.Throw(e);
                    }
                }
                return null;
            }
        }, "xml");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                if (!cls.isArray()) return null;
                Class<?> ccls = cls.getComponentType();
                if (Integer.class.isAssignableFrom(ccls) || Integer.TYPE.isAssignableFrom(ccls))
                    return resources.getIntArray(id);
                if (String.class.isAssignableFrom(ccls)) return resources.getStringArray(id);
                if (CharSequence.class.isAssignableFrom(ccls)) return resources.getTextArray(id);
                return null;
            }
        }, "array");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                try {
                    if (Movie.class.isAssignableFrom(cls)) return resources.getMovie(id);
                    if (cls.equals(byte[].class))
                        return ZStream.readAllBytes(resources.openRawResource(id));
                    if (InputStream.class.isAssignableFrom(cls))
                        return resources.openRawResource(id);
                } catch (IOException e) {
                    ExceptionHandler.Throw(e);
                }
                return null;
            }
        },"raw");
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            public Object f(Class<?> cls, Integer id) {
                return LayoutInflater.from(context).inflate(id, null);
            }
        }, "layout");
        Interpolator d;
        registerResourceGetter(new T.Func2<Object,Class<?>,Integer>() {
            @Override
            @SuppressLint("NewApi")
            public Object f(Class<?> cls, Integer id) {
                if (Build.VERSION.SDK_INT >= 19)
                    return TransitionInflater.from(context).inflateTransition(id);
                return null;
            }
        }, "transition");
    }

    public static void initInjectors(){
        registerInjector(Object.class,new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object o, Field field, View root) {
                if (!field.isAnnotationPresent(ZBind.class)) return null;
                ResourceName rn=getName(field.getAnnotation(ZBind.class),field);
                if (rn.id==0) return null;
                Object obj=null;
                if (rn.type.equals("id")){
                    obj=root.findViewById(rn.id);
                }
                if (obj==null) obj=getValue(field.getType(),rn.type,rn.id);
                if (obj instanceof View&&o instanceof IViewHolder) {
                    ZBind bind=field.getAnnotation(ZBind.class);
                    View v=(View)obj;
                    ((IViewHolder)o).getViewHolder().put(field.getName(), v, bind);
                    rn= ZInjector.getMenuName(bind, field);
                    if (rn!=null&&rn.id!=0) v.setOnCreateContextMenuListener(new ContextMenuCreator(rn.id));
                }
                return obj;
            }
        },ZBind.class);
        registerInjector(Context.class, new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object holder, Field field, View root) {
                if (root.getContext()!=null) return root.getContext();
                if (holder instanceof Context) return holder;
                return context;
            }
        },ZBind.class);
        registerInjector(Annotation.class, new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object holder, Field field, View root) {
                ZBind b=field.getAnnotation(ZBind.class);
                String f=b.name();
                if (TextUtils.isEmpty(f)) f=b.value();
                if (TextUtils.isEmpty(f)) f=field.getName();
                Field ff=Ref.getField(holder.getClass(),f,"android.","com.bigzhao.andframe.","java.","javax.");
                if (ff!=null) return ff.getAnnotation((Class<? extends Annotation>)field.getType());
                return null;
            }
        },ZBind.class);
        registerInjector(Intent.class,new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object o, Field field, View view) {
                if (o instanceof ITemplateViewHolder<?>){
                    return ((ITemplateViewHolder<?>)o).getTemplate().getActivity().getIntent();
                }
                if (o instanceof Activity) return ((Activity)o).getIntent();
                if (o instanceof Fragment) return ((Fragment)o).getActivity().getIntent();
                if (o instanceof android.support.v4.app.Fragment) return ((android.support.v4.app.Fragment)o).getActivity().getIntent();
                return null;
            }
        },ZBind.class);
    }

    public static Object getValue(Class<?> cls,String type,int id){
        Object ret=null;
        try {
            T.Func2<Object,Class<?>,Integer> vg=valueGetters.get(type);
            if (vg!=null){
                ret=vg.f(cls,id);
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
