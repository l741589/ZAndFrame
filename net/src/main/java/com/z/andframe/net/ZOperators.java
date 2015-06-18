package com.z.andframe.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.z.andframe.net.request.Request;
import com.z.andframe.util.T;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Roy on 15-3-24.
 */
public class ZOperators {
    public static T.Func1<String,Request> requestOperator=new T.Func1<String, Request>() {
        @Override
        public String f(Request req) {
            Class<?> cls=req.getClass();
            return urlOperator.f(cls.getSimpleName().replace("_", "/"));
        }
    };

    public static T.Func1<String,String> urlOperator=new T.Func1<String, String>() {
        @Override
        public String f(String path) {
            String base=ZNet.getContext().getString(R.string.base_url);
            return base+path;
        }
    };

    public static T.Func1<String,Object> toFromData=new T.Func1<String, Object>() {
        @Override
        public String f(Object o) {
            Object json = JSON.toJSON(o);
            if (json instanceof JSONObject) {
                JSONObject obj = (JSONObject) json;
                List<NameValuePair> list=new ArrayList<>();
                for (Map.Entry<String,Object> p:obj.entrySet()) {
                    if (p.getValue()!=null)
                        list.add(new BasicNameValuePair(p.getKey(),p.getValue().toString()));
                }
                return URLEncodedUtils.format(list,"UTF-8");
            }
            return null;
        }
    };


}
