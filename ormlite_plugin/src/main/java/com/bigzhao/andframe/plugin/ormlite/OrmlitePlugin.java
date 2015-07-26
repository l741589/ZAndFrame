package com.bigzhao.andframe.plugin.ormlite;

import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.DatabaseTable;
import com.bigzhao.andframe.binder.ZBinder;
import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.pluginmanager.ZBasePlugin;
import com.bigzhao.andframe.util.ExceptionHandler;
import com.bigzhao.andframe.util.T;
import com.bigzhao.andframe.view.interfaces.IViewHolder;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roy on 15-5-20.
 */
@SuppressWarnings("unchecked")
public class OrmlitePlugin extends ZBasePlugin{

    private List<Class<?>> tables=new ArrayList<>();

    private List<?> query(Field field){
        try {
            ZDataBase d = field.getAnnotation(ZDataBase.class);
            if (d == null) return null;
            String raw=d.raw();
            Class<?> table = d.table().equals(Object.class) ? field.getType() : d.table();
            if (raw.equals("")) {
                return ZDatabaseHelper.getInstance().getDao(table).queryBuilder().where().raw(d.where()).query();
            }else{
                return ZDatabaseHelper.getInstance().getDao(table).queryRaw(raw).getResults();
            }
            //return (cols == null ? new Select() : new Select(cols)).from(table).where(d.where()).executeSingle();
        } catch (java.sql.SQLException e) {
            ExceptionHandler.Hide(e);
            return null;
        }
    }

    @Override
    public void onLoad() {
        new ZDatabaseHelper(getApplication(),tables);
        ZBinder.registerInjector(ZDatabaseHelper.class,new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object o, Field field, View view) {
                return ZDatabaseHelper.getInstance();
            }
        }, ZBind.class,ZDataBase.class);
        ZBinder.registerInjector(Dao.class,new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object o, Field field, View view) {
                ZDataBase d = field.getAnnotation(ZDataBase.class);
                if (d == null) return null;
                return ZDatabaseHelper.getInstance().getDao(d.table());
            }
        },ZDataBase.class);
        ZBinder.registerInjector(Object.class,new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object o, Field field, View view) {
                List<?> r=query(field);
                if (Iterable.class.isAssignableFrom(field.getType())) return r;
                if (field.getType().isArray()) return r.toArray((Object[])Array.newInstance(field.getType().getComponentType(),0));
                if (r==null||r.size()==0) return null;
                return r.get(0);
            }
        },ZDataBase.class);
    }

    @Override
    public boolean onScan(Class<?> cls) {
        if (cls.isAnnotationPresent(DatabaseTable.class)) {
            tables.add(cls);
            return true;
        }
        return super.onScan(cls);
    }
}
