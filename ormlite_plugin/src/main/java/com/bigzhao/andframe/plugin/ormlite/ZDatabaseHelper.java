package com.bigzhao.andframe.plugin.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.bigzhao.andframe.util.ExceptionHandler;

import java.util.List;

/**
 * Created by Roy on 15-5-20.
 */
public class ZDatabaseHelper extends OrmLiteSqliteOpenHelper
{

    private static final String TABLE_NAME = "zaf_aa_default.db";
    private List<Class<?>> tables;

    ZDatabaseHelper(Context context,List<Class<?>> tables)
    {

        super(context, TABLE_NAME, null, 2);
        this.tables=tables;
        instance=this;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        try
        {
            for (Class<?> cls:tables){
                TableUtils.createTableIfNotExists(connectionSource,cls);
            }
        } catch (java.sql.SQLException e){
            ExceptionHandler.ThrowNoIgnore(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            for (Class<?> cls:tables){
                TableUtils.dropTable(connectionSource, cls, true);
            }
            onCreate(database, connectionSource);
        } catch (java.sql.SQLException e)
        {
            ExceptionHandler.Throw(e);
        }
    }

    private static ZDatabaseHelper instance;

    /**
     * 单例获取该Helper
     * @return
     */
    public static synchronized ZDatabaseHelper getInstance()
    {
        return instance;
    }

    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();
    }

    @SuppressWarnings("unchecked")
    public<D extends Dao<T,K>, T,K> Dao<T,K> getDao(Class<T> table, Class<K> keyType){
        try {
            return (D)super.getDao(table);
        } catch (java.sql.SQLException e) {
            ExceptionHandler.Throw(e);
            return null;
        }
    }

    @Override
    public <D extends Dao<T, ?>, T> D getDao(Class<T> table){
        try {
            return super.getDao(table);
        } catch (java.sql.SQLException e) {
            ExceptionHandler.Throw(e);
            return null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}