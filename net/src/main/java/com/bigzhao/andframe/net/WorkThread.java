package com.bigzhao.andframe.net;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.bigzhao.andframe.net.request.Request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Roy on 15-3-24.
 */
public class WorkThread extends AsyncTask<Request,Object,Object> {

    @Override
    protected Object doInBackground(Request... params) {
        Request req=params[0];
        Object result=null;
        if (ZNet.isMock()){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<ZNet.MockExecutor> es=ZNet.getMockExecutors();
            for(ZNet.MockExecutor e:es) {
                result=e.execute(req);
                if (result!=null) break;
            }
        }else {
            HttpClient client = ZNet.getClient();
            HttpUriRequest hreq = req.getHttpRequest();
            try {
                publishProgress(1, req, hreq);
                ZNet.log(req.getMethod().name() + ":" + req.getUrl() + ":" + hreq);
                HttpResponse hres = client.execute(hreq);
                ZNet.log("RES:" + hres);
                result = ZNet.getResponseHandler().handle(req, hres);
            } catch (Exception e) {
                publishProgress(2, req, e);
            }
        }
        publishProgress(result instanceof Exception ? 2 : 3, req, result);
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        Request<?> req = (Request<?>) values[1];
        Object obj = values[2];
        switch((int)values[0]){
            case 1:ZNet.getRequestHandler().handle(req, (HttpUriRequest) obj);break;
            case 2:ZNet.getResponseHandler().handleException(req, (Exception) obj);break;
            case 3:ZNet.getResponseHandler().handleSuccess(req, obj);break;
        }
    }

    public static class RequestExecutorImpl implements ZNet.RequestExecutor {
        @Override
        public void execute(Request request) { new WorkThread().execute(request); }
    };

    public static class RequestHandlerImpl implements ZNet.RequestHandler{
        @Override
        public void handle(Request<?> request, HttpUriRequest httpUriRequest) {

        }
    }

    @SuppressWarnings("unchecked")
    public static class ResponseHandlerImpl implements ZNet.ResponseHandler{

        @Override
        public Object handle(Request request, HttpResponse response) {
            if (response==null){
               return new ZNetException("Null Response",ZNetException.CODE_NULL_RESPONSE);
            }else if (response.getStatusLine().getStatusCode()>=400){
                StatusLine sl=response.getStatusLine();
                return new ZNetException(sl.getReasonPhrase(),sl.getStatusCode());
            }else {
                try {
                    Type t = request.getTypeReference().getType();
                    Object obj = null;
                    if (t instanceof Class) {
                        Class<?> c=(Class<?>)t;
                        if (t.equals(byte[].class))
                            obj = EntityUtils.toByteArray(response.getEntity());
                        else if (t.equals(String.class))
                            obj = EntityUtils.toString(response.getEntity());
                        else if (InputStream.class.isAssignableFrom((Class<?>) t))
                            obj = response.getEntity().getContent();
                        else if (HttpEntity.class.isAssignableFrom((Class<?>) t))
                            obj = response.getEntity();
                        else if (JSON.class.isAssignableFrom(c)){
                            obj=JSON.parseObject(EntityUtils.toString(response.getEntity()));
                        }
                    }
                    if (obj == null)
                        obj = JSON.parseObject(EntityUtils.toString(response.getEntity()), request.getTypeReference());
                    return obj;
                } catch (Exception e) {
                    return e;
                }
            }
        }

        @Override
        public void handleSuccess(Request request, Object result) {
            request.getResponseListener().onSuccess(request,result);
        }

        @Override
        public void handleException(Request request, Exception e) {
            request.getResponseListener().onError(request,e);
        }
    }


}
