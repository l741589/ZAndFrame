package com.z.andframe.net;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Roy on 15-3-25.
 */
public abstract class TypeReference<T> extends com.alibaba.fastjson.TypeReference<T>{

    private final Type type;

    protected TypeReference(Type cls){
        type=cls;
    }

    protected TypeReference(){
        Type superClass = getClass().getGenericSuperclass();

        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    @Override
    public Type getType() {
        return type;
    }
}