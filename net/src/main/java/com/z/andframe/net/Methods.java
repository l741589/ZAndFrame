package com.z.andframe.net;

import android.text.TextUtils;

import com.z.andframe.net.request.Request;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * Created by Roy on 15-3-24.
 */
public enum Methods {
    GET(false),
    POST(true),
    HEAD(false),
    PUT(true),
    DELETE(false),
    OPTIONS(false),
    TRACE(false);

    private boolean _haveEntity;

    Methods(boolean haveEntity){
        this._haveEntity=haveEntity;
    }

    public boolean haveEntity() {
        return _haveEntity;
    }

    public HttpUriRequest getHttpRequest(String url,Request<?> obj) {
        if (!haveEntity()){
            String fd=obj.toQueryString();
            if (!TextUtils.isEmpty(fd)) url+="?"+fd;
        }
        HttpUriRequest req = null;
        switch (this) {
            case POST: req = new HttpPost(url); break;
            case GET: req = new HttpGet(url); break;
            case PUT: req = new HttpPut(url); break;
            case HEAD: req = new HttpHead(url); break;
            case DELETE: req = new HttpDelete(url); break;
            case TRACE: req = new HttpTrace(url); break;
            case OPTIONS: req = new HttpOptions(url); break;
        }
        if (req instanceof HttpEntityEnclosingRequest) {
            ((HttpEntityEnclosingRequest) req).setEntity(obj.toHttpEntity());
            for (Header h:obj.getHeaders()) req.addHeader(h);
            boolean haveContentType=false;
            Header[] headers=req.getAllHeaders();
            for (Header h : headers) {
                if ("Content-Type".equals(h.getName())){
                    haveContentType=true;
                    break;
                }
            }
            if (!haveContentType) req.addHeader("Content-Type", "application/x-www-form-urlencoded");
        }else{
            for (Header h:obj.getHeaders()) req.addHeader(h);
        }
        return req;
    }
}
