package com.bigzhao.andframe.zorm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigzhao.andframe.binder.ZBinder;
import com.bigzhao.andframe.pluginmanager.ZBasePlugin;
import com.bigzhao.andframe.util.T;
import com.bigzhao.andframe.util.ZUtil;

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
            type=cur.getType(index);
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

    private JSONObject getObj(Cursor cur){
        JSONObject obj=new JSONObject();
        for (int i=0;i<cur.getColumnCount();++i){
            ColumnInfo c=new ColumnInfo(cur,i);
            obj.put(c.name,c.get());
        }

        return obj;
    }

    @Override
    public void onLoad() {
        ZBinder.registerInjector(Object.class, new T.Func3<Object, Object, Field, View>() {
            @Override
            public Object f(Object o, Field field, View view) {
                ZOrm cfg=field.getAnnotation(ZOrm.class);
                Log.e("lyz","table:"+cfg.table());
                Cursor cur=null;
                try {
                    String table=cfg.table();
                    if (table.startsWith("var://")) table= ZUtil.resolveVariale(Uri.parse(table)).toString();
                    if (table.startsWith("content://")) {
                        cur = getApplication().getContentResolver().query(
                                Uri.parse(table),
                                cfg.projection().length == 0 ? null : cfg.projection(),
                                cfg.selection().length() == 0 ? null : cfg.selection(),
                                cfg.selectionArgs().length == 0 ? null : cfg.selectionArgs(),
                                cfg.sortOrder().length() == 0 ? null : cfg.sortOrder());
                    } else {
                        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(cfg.database(), null);
                        if (cfg.raw().length() > 0) {
                            cur = db.rawQuery(cfg.raw(), cfg.selectionArgs().length == 0 ? null : cfg.selectionArgs());
                        } else {

                            cur = db.query(cfg.distinct(),
                                    table,
                                    cfg.projection().length == 0 ? null : cfg.projection(),
                                    cfg.selection().length() == 0 ? null : cfg.selection(),
                                    cfg.selectionArgs().length == 0 ? null : cfg.selectionArgs(),
                                    cfg.groupBy().length() == 0 ? null : cfg.groupBy(),
                                    cfg.having().length() == 0 ? null : cfg.having(),
                                    cfg.sortOrder().length() == 0 ? null : cfg.sortOrder(),
                                    cfg.limit().length() == 0 ? null : cfg.limit());
                        }
                    }
                    if (field.getType().isArray() || Iterable.class.isAssignableFrom(field.getType())) {
                        JSONArray arr = new JSONArray();
                        while (cur.moveToNext()) {
                            arr.add(getObj(cur));
                        }
                        return JSON.toJavaObject(arr, field.getType());
                    } else {
                        if (cur.moveToNext())
                            return JSON.toJavaObject(getObj(cur), field.getType());
                    }
                }finally {
                    if (cur!=null) cur.close();
                }
                return null;
            }
        },ZOrm.class);

    }
}
