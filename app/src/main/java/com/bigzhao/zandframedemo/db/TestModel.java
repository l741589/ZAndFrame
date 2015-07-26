package com.bigzhao.zandframedemo.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Roy on 15-5-20.
 */
@DatabaseTable
public class TestModel{
    @DatabaseField(id = true)
    public String name;
}
