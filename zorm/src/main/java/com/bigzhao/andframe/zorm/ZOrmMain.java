package com.bigzhao.andframe.zorm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigzhao.andframe.binder.ZBinder;
import com.bigzhao.andframe.pluginmanager.ZBasePlugin;
import com.bigzhao.andframe.util.T;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Created by Roy on 15-7-25.
 */
public class ZOrmMain extends ZBasePlugin{

    private static class ColumnInfo{
        public int index;
        public String name;
        public int type;
        private Cursor cur;

        public ColumnInfo(Cursor cur,int i){
            this.cur=cur;
            index=i;
            name=cur.getColumnName(i);
            type=cur.getType(i);
        }

        public Object get(){
            switch(type){
                case Cursor.FIELD_TYPE_BLOB:return cur.getBlob(index);
                case Cursor.FIELD_TYPE_FLOAT:return cur.getDouble(index);
                case Cursor.FIELD_TYPE_INTEGER:return cur.getLong(index);
                case Cursor.FIELD_TYPE_STRING:return cur.getString(index);
                case Cursor.FIELD_TYPE_NULL:return null;
            }
            return null;
        }
    }

    private JSONObject getObj(ColumnInfo[] cis){
        JSONObject obj=new JSONObject();
        for (ColumnInfo c:cis)
                obj.put(c.name,c.get());
        return obj;
    }

    @Override
    public void onLoad() {
        ZBinder.registerInjector(Object.class, new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object o, Field field, View view) {
                ZOrm cfg=field.getAnnotation(ZOrm.class);
                Cursor cur;
                if (cfg.table().startsWith("content://")){
                    cur=getApplication().getContentResolver().query(
                        Uri.parse(cfg.table()),
                        cfg.projection().length==0?null:cfg.projection(),
                        cfg.selection().length()==0?null:cfg.selection(),
                        cfg.selectionArgs().length==0?null:cfg.selectionArgs(),
                        cfg.sortOrder().length()==0?null:cfg.sortOrder());
                }else{
                    SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(cfg.database(),null);
                    if (cfg.raw().length()>0){
                        cur=db.rawQuery(cfg.raw(),cfg.selectionArgs().length == 0 ? null : cfg.selectionArgs());
                    }else {
                        cur=db.query(cfg.distinct(),
                                cfg.table(),
                                cfg.projection().length == 0 ? null : cfg.projection(),
                                cfg.selection().length() == 0 ? null : cfg.selection(),
                                cfg.selectionArgs().length == 0 ? null : cfg.selectionArgs(),
                                cfg.groupBy().length() == 0 ? null : cfg.groupBy(),
                                cfg.having().length() == 0 ? null : cfg.having(),
                                cfg.sortOrder().length() == 0 ? null : cfg.sortOrder(),
                                cfg.limit().length() == 0 ? null : cfg.limit());
                    }
                }
                ColumnInfo[] cis=new ColumnInfo[cur.getColumnCount()];
                for(int i=0;i<cis.length;++i) cis[i]=new ColumnInfo(cur,i);
                if (field.getType().isArray()||Iterable.class.isAssignableFrom(field.getType())){
                    JSONArray arr=new JSONArray();
                    while(cur.moveToNext()){
                        arr.add(getObj(cis));
                    }
                    return JSON.toJavaObject(arr,field.getType());
                }else{
                    if (cur.moveToNext())
                        return JSON.toJavaObject(getObj(cis),field.getType());
                }
                return null;
            }
        },ZOrm.class);

    }
}
