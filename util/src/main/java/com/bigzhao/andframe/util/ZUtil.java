package com.bigzhao.andframe.util;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Roy on 15-3-18.
 */
public class ZUtil {
    public static View getContentView(Activity context)  {
        return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
    }


    public static View getContentView(Object context)  {
        if (context instanceof Activity){
            return getContentView((Activity)context);
        }else if (context instanceof Fragment){
            return ((Fragment)context).getView();
        }else if (context instanceof android.support.v4.app.Fragment){
            return ((android.support.v4.app.Fragment)context).getView();
        }else if (context instanceof View){
            return (View)context;
        }
        return null;
    }

    public static Class<?> boxedToPrimitive(Class<?> cls){
        if (Integer.class.equals(cls)) return Integer.TYPE;
        if (Float.class.equals(cls)) return Float.TYPE;
        if (Double.class.equals(cls)) return Double.TYPE;
        if (Boolean.class.equals(cls)) return Boolean.TYPE;
        if (Long.class.equals(cls)) return Long.TYPE;
        if (Character.class.equals(cls)) return Character.TYPE;
        if (Short.class.equals(cls)) return Short.TYPE;
        if (Byte.class.equals(cls)) return Byte.TYPE;
        return cls;
    }

    public static Class<?> primitiveToBoxed(Class<?> cls){
        if (Integer.TYPE.equals(cls)) return Integer.class;
        if (Float.TYPE.equals(cls)) return Float.class;
        if (Double.TYPE.equals(cls)) return Double.class;
        if (Boolean.TYPE.equals(cls)) return Boolean.class;
        if (Long.TYPE.equals(cls)) return Long.class;
        if (Character.TYPE.equals(cls)) return Character.class;
        if (Short.TYPE.equals(cls)) return Short.class;
        if (Byte.TYPE.equals(cls)) return Byte.class;
        return cls;
    }

    public static void postDelay(final int time,final Runnable run){
        new AsyncTask<Object,Object,Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                run.run();
            }
        }.execute();
    }



}
