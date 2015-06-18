package com.z.andframe.net.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.z.andframe.net.Methods;
import com.z.andframe.net.TypeReference;
import com.z.andframe.util.ExceptionHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Roy on 15-3-26.
 */
public class JsonRequest<ResponseType> extends Request<ResponseType> {

    public JsonRequest(){
        setMethod(Methods.POST);
    }

    @Override
    public HttpEntity toHttpEntity() {
        try {
            addHeader("Content-Type","application/json");
            return new StringEntity(JSON.toJSONString(entity));
        } catch (UnsupportedEncodingException e) {
            ExceptionHandler.Throw(e);
        }
        return null;
    }
}
