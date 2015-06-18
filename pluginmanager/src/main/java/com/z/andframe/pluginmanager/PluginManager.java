package com.z.andframe.pluginmanager;

import android.app.Application;

import com.z.andframe.util.Ref;
import com.z.andframe.util.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roy on 15-5-21.
 */
public class PluginManager {

    private static List<ZBasePlugin> plugins=new ArrayList<>();

    public static void init(final Application app){
        Ref.scanClasses(new T.Proc1<Class<?>>() {
            @Override
            public void f(Class<?> cls) {
                if (ZBasePlugin.class.isAssignableFrom(cls)){
                    if (cls.equals(ZBasePlugin.class))return;
                    try {
                        ZBasePlugin plugin=(ZBasePlugin)cls.newInstance();
                        plugin.setApplication(app);
                        plugins.add(plugin);
                    } catch (InstantiationException|IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Ref.scanClasses(new T.Proc1<Class<?>>() {
            @Override
            public void f(Class<?> aClass) {
                for (ZBasePlugin p:plugins){
                    p.onScan(aClass);
                }
            }
        });
        for (ZBasePlugin p:plugins){
            p.onLoad();
        }
    }
}
