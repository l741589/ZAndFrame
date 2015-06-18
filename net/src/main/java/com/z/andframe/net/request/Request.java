package com.z.andframe.net.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.z.andframe.net.IResponseListener;
import com.z.andframe.net.Methods;
import com.z.andframe.net.TypeReference;
import com.z.andframe.net.ZNet;
import com.z.andframe.net.ZOperators;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Roy on 15-3-24.
 */
public class Request<ResponseType> {

    private Methods method = Methods.GET;
    protected String url;
    protected Object entity=this;
    protected TypeReference<?> typeReference;
    private IResponseListener responseListener;
    private List<Header> headers;

    @JSONField(serialize = false)
    public Methods getMethod() {
        return method;
    }

    public Request<ResponseType> setMethod(Methods method) {
        this.method = method;
        return this;
    }


    public Request(){
        this(null, Methods.GET);
    }

    protected void init(){

    }

    public Request(String url,Methods method){
        this.url=url;
        this.method=method;
        if (this.url==null) this.url= ZOperators.requestOperator.f(this);
        init();
    }

    public Request<ResponseType> setEntity(Object entity){
        if (entity==null) this.entity=this;
        else this.entity=entity;
        return this;
    }

    @JSONField(serialize = false)
    public Object getEntity() {
        return this.entity;
    }

    public HttpEntity toHttpEntity() {
        if (entity instanceof HttpEntity) return (HttpEntity)entity;
        else if (entity!=this&&entity instanceof Request) return ((Request)entity).toHttpEntity();
        else try {
            if (entity instanceof JSON) return new StringEntity(entity.toString());
            else return new StringEntity(ZOperators.toFromData.f(entity));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toQueryString(){
        if (entity instanceof String) return (String)entity;
        else if (entity!=this&&entity instanceof Request) return ((Request)entity).toQueryString();
        else return ZOperators.toFromData.f(entity);
    }

    @JSONField(serialize = false)
    public String getUrl() {
        return url;
    }

    public Request<ResponseType> setUrl(String url) {
        this.url = url;
        return this;
    }

    public Request<ResponseType> setPath(String path){
        return setUrl(ZOperators.urlOperator.f(path));
    }

    @JSONField(serialize = false)
    public TypeReference<?> getTypeReference() {
        if (typeReference!=null) return typeReference;
        else {
            Type superClass = getClass().getGenericSuperclass();
            return typeReference=new TypeReference<ResponseType>(((ParameterizedType) superClass).getActualTypeArguments()[0]){};
        }

    }

    public Request<ResponseType> setResponseType(TypeReference<?> typeReference) {
        this.typeReference = typeReference;
        return this;
    }

    public Request<ResponseType> setResponseType(Class<?> type) {
        typeReference=new TypeReference<Object>(type) {};
        return this;
    }

    @JSONField(serialize = false)
    public HttpUriRequest getHttpRequest(){
        return method.getHttpRequest(url,this);
    }

    @JSONField(serialize = false)
    public IResponseListener getResponseListener() {
        return responseListener;
    }

    public Request<ResponseType> setResponseListener(IResponseListener reponseListener) {
        this.responseListener = reponseListener;
        return this;
    }

    public void exec(){
        ZNet.getRequestExecutor().execute(this);
    }

    public void get(){
        setMethod(Methods.GET);
        ZNet.getRequestExecutor().execute(this);
    }

    public void post(){
        setMethod(Methods.POST);
        ZNet.getRequestExecutor().execute(this);
    }

    public void put(){
        setMethod(Methods.PUT);
        ZNet.getRequestExecutor().execute(this);
    }

    public void delete(){
        setMethod(Methods.DELETE);
        ZNet.getRequestExecutor().execute(this);
    }

    public void options(){
        setMethod(Methods.OPTIONS);
        ZNet.getRequestExecutor().execute(this);
    }

    public void trace(){
        setMethod(Methods.TRACE);
        ZNet.getRequestExecutor().execute(this);
    }

    public void head(){
        setMethod(Methods.HEAD);
        ZNet.getRequestExecutor().execute(this);
    }

    public Request<ResponseType> addHeader(Header header){
        if (headers==null) headers=new ArrayList<>();
        headers.add(header);
        return this;
    }

    public Request<ResponseType> addHeader(String name,String value){
        return addHeader(new BasicHeader(name,value));
    }

    public void removeHeader(String name){
        for (int i=0;i<headers.size();++i){
            if (headers.get(i).getName().equals(name)){
                headers.remove(i);
                return;
            }
        }
    }



    @JSONField(serialize = false)
    public List<Header> getHeaders(){
        if (this.headers==null) this.headers=new ArrayList<>();
        LinkedList<Header> headers=new LinkedList<>(this.headers);
        if (entity!=this&&entity instanceof Request){
            headers.addAll(((Request<?>)entity).getHeaders());
        }
        return headers;
    }
}
