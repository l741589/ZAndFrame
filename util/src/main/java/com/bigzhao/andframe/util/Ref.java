package com.bigzhao.andframe.util;

import android.util.Log;

import com.bigzhao.andframe.util.zquery.ZProjection;
import com.bigzhao.andframe.util.zquery.ZQuery;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * Created by Roy on 15-3-19.
 */
public class Ref {

    public static List<Field> getAllFields(Class<?> cls, String... stopPrefix){
        if (stopPrefix==null) stopPrefix=new String[0];
        ArrayList<Field> fs=new ArrayList<>();
        WHILE:while(true){
            if (cls==null) break;
            for (String s:stopPrefix) {
                if (cls.getName().startsWith(s)) break WHILE;
            }
            Field[] ff=cls.getDeclaredFields();
            for (Field f:ff) fs.add(f);
            cls=cls.getSuperclass();
        }
        return fs;
    }

    public static List<Method> getAllMethods(Class<?> cls, String... stopPrefix){
        if (stopPrefix==null) stopPrefix=new String[0];
        ArrayList<Method> fs=new ArrayList<>();
        WHILE:while(true){
            if (cls==null) break;
            for (String s:stopPrefix) {
                if (cls.getName().startsWith(s)) break WHILE;
            }
            Method[] ff=cls.getDeclaredMethods();
            for (Method f:ff) fs.add(f);
            cls=cls.getSuperclass();
        }
        return fs;
    }

    public static Field getField(Class<?> cls, String name, String... stopPrefix){
        if (stopPrefix==null) stopPrefix=new String[0];
        ArrayList<Field> fs=new ArrayList<>();
        WHILE:while(true){
            if (cls==null) break;
            for (String s:stopPrefix) {
                if (cls.getName().startsWith(s)) break WHILE;
            }
            Field[] ff=cls.getDeclaredFields();
            for (Field f:ff) if (f.getName().equals(name)) return f;
            cls=cls.getSuperclass();
        }
        return null;
    }




    static public Object getValue(Object target,String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f=target.getClass().getField(fieldName);
        return f.get(target);
    }

    static public void setValue(Object target,String fieldName,Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f=target.getClass().getField(fieldName);
        f.set(target,value);
    }

    static public Object getSuperValue(Object target,String fieldName, String... stopPrefix) throws NoSuchFieldException, IllegalAccessException {
        Field f=getField(target.getClass(),fieldName,stopPrefix);
        if (f==null) throw new NoSuchFieldException(fieldName);
        f.setAccessible(true);
        return f.get(target);
    }

    static public void setSuperValue(Object target,String fieldName,Object value, String... stopPrefix) throws NoSuchFieldException, IllegalAccessException {
        Field f=getField(target.getClass(),fieldName,stopPrefix);
        if (f==null) throw new NoSuchFieldException(fieldName);
        f.setAccessible(true);
        f.set(target,value);
    }

    static public Object call(Object target,String method,Object...args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m=target.getClass().getMethod(method, ZQuery.create(args).select(new ZProjection<Object, Class<?>>() {
            @Override
            public Class<?> proj(Object input) { return input.getClass();}
        }).toArray(Class.class));
        return m.invoke(target,args);
    }

    static public Object call(Object target,String method,Class<?>[] types,Object...args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m=target.getClass().getMethod(method,types);
        return m.invoke(target,args);
    }

   static public Method getConpatibleMethod(Class<?> cls,String method,Class<?>...args){
       Method[] ms=cls.getMethods();
       for (Method m:ms){
           if (m.getName().equals(method)){
               Class<?>[] pts=m.getParameterTypes();
               if (pts.length!=args.length) continue;
               boolean ok=true;
               for (int i=0;i<args.length;++i){
                   Class<?> p=pts[i];
                   Class<?> a=args[i];
                   if (p.isPrimitive()||a.isPrimitive()){
                       if (!p.equals(a)&&!ZUtil.boxedToPrimitive(p).equals(ZUtil.boxedToPrimitive(a))){
                           ok=false;
                           break;
                       }
                   }else if (!p.isAssignableFrom(a)) {
                       ok=false;
                       break;
                   }
               }
               if (ok) return m;
           }
       }
       return null;
   }

    private static Field dexField;
    private static DexFile[] getDexFiles(){
        Field dexField = null;
        PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();
        try {
            dexField = PathClassLoader.class.getDeclaredField("mDexs");
            dexField.setAccessible(true);

            return (DexFile[]) dexField.get(classLoader);
        } catch (NoSuchFieldException|IllegalAccessException e) {

        }
        try {
            dexField=BaseDexClassLoader.class.getDeclaredFields()[1];
            dexField.setAccessible(true);
            Object x=dexField.get(classLoader);
            Object elements=Ref.getSuperValue(x,"dexElements");
            int l=Array.getLength(elements);
            ArrayList<DexFile> files=new ArrayList<>();
            for (int i=0;i<l;++i){
                Object e=Array.get(elements,i);
                Object dexFile=getSuperValue(e,"dexFile");
                files.add((DexFile)dexFile);
            }

            Log.d("", "");
            return files.toArray(new DexFile[0]);
        } catch (IllegalAccessException|NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static void scanClasses(T.Proc1<Class<?>> classHandler) {
        try {
            PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();
            DexFile[] dexs = getDexFiles();
            for (DexFile dex : dexs) {
                Enumeration<String> entries = dex.entries();
                while (entries.hasMoreElements()) {
                    String entry = entries.nextElement();

                    Class<?> entryClass = dex.loadClass(entry, classLoader);
                    if (entryClass != null) {
                        classHandler.f(entryClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
