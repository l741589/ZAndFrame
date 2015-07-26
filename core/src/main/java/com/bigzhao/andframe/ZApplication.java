package com.bigzhao.andframe;

import android.app.Application;
import android.util.Log;

import com.bigzhao.andframe.binder.ZBinder;
import com.bigzhao.andframe.net.ZNet;
import com.bigzhao.andframe.pluginmanager.PluginManager;
import com.bigzhao.andframe.util.ExceptionHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Roy on 15-3-18.
 */
public class ZApplication extends Application{
    @Override
    public void onCreate() {
        init(this);
    }

    public static void init(Application app){
        ZBinder.init(app);
        ZNet.init(app);
        PluginManager.init(app);
    }

    public static void initPlugins(Application app){
        try {
            String[] ss=app.getAssets().list("ZAndFrame/Plugins");
            for (String s:ss){
                try {
                    Class<?> cls=Class.forName(s);
                    cls.getConstructor(Application.class).newInstance(app);
                    Log.i("ZAF Plugins", s+" loaded");
                } catch (ClassNotFoundException|InstantiationException|IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                    ExceptionHandler.Hide(e);
                }
            }
        } catch (IOException e) {
            ExceptionHandler.Throw(e);
        }
    }
}
