package com.bigzhao.andframe.net;

/**
 * Created by Roy on 15-3-24.
 */
public class EntityTypes {
    public static final EntityTypes HTML=new EntityTypes("text/html");
    public static final EntityTypes FORM=new EntityTypes("application/x-www-form-urlencoded");
    public static final EntityTypes JSON=new EntityTypes("application/json");
    public static final EntityTypes FILE=new EntityTypes("application/octet-stream");
    public static final EntityTypes STRING=new EntityTypes("text/plain");
    public static final EntityTypes DATA=new EntityTypes("application/oct*et-stream");
    public static final EntityTypes OTHER=new EntityTypes(null);

    private String contentType;

    private EntityTypes(String contentType){

    }

}
