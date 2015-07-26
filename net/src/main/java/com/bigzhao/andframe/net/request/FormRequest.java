package com.bigzhao.andframe.net.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.bigzhao.andframe.net.Methods;
import com.bigzhao.andframe.net.TypeReference;
import com.bigzhao.andframe.util.ExceptionHandler;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Roy on 15-3-26.
 */
public class FormRequest<ResponseType> extends Request<ResponseType>{

    private List<NameValuePair> list=new ArrayList<>();

    public FormRequest<ResponseType> set(String key,Object value){
        list.add(new BasicNameValuePair(key,value.toString()));
        return this;
    }

    public FormRequest<ResponseType> add(Map<String,Object> map){
        for (Map.Entry<String,Object> e:map.entrySet())
            list.add(new BasicNameValuePair(e.getKey(),e.getValue().toString()));
        return this;
    }

    @Override
    public String toQueryString() {
        return URLEncodedUtils.format(list,"UTF-8");
    }

    @Override
    public HttpEntity toHttpEntity() {
        try {
            addHeader("Content-Type", "application/x-www-form-urlencoded");
            return new StringEntity(URLEncodedUtils.format(list,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ExceptionHandler.Throw(e);
        }
        return null;
    }
}
