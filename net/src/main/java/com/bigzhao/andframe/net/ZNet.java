package com.bigzhao.andframe.net;

import android.content.Context;
import android.util.Log;

import com.bigzhao.andframe.net.request.Request;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Roy on 15-3-24.
 */
public class ZNet {

    static void log(String text){
        if (doLog) Log.i("ZNet",text);
    }

    public static void setDoLog(boolean doLog) {
        ZNet.doLog = doLog;
    }

    private static boolean doLog=true;
    private static boolean doMock=false;

    public static void setDoMock(boolean doMock) {
        ZNet.doMock = doMock;
    }

    public static boolean isMock() {
        return doMock;
    }

    public static interface RequestHandler { void handle(Request<?> request, HttpUriRequest httpUriRequest);}
    public static interface RequestExecutor { void execute(Request request); }
    public static interface MockExecutor { Object execute(Request request); }
    public static interface ResponseHandler{
        /**In Work Thread*/
        Object handle(Request request,HttpResponse response);
        /**In Main Thread*/
        void handleSuccess(Request request,Object result);
        /**In Main Thread*/
        void handleException(Request request,Exception e);
    }

    private static Context context;
    private static HttpClient client;
    private static RequestHandler requestHandler;
    private static RequestExecutor requestExecutor;
    private static ResponseHandler responseHandler;
    private static List<MockExecutor> mockExecutors;

    public static void init(Context context){
        ZNet.context=context;
    }

    public static HttpClient getClient() {
        if (client==null) client=new DefaultHttpClient();
        return client;
    }

    public static void setClient(HttpClient client) {
        ZNet.client = client;
    }

    public static RequestHandler getRequestHandler() {
        if (requestHandler==null) requestHandler=new WorkThread.RequestHandlerImpl();
        return requestHandler;
    }

    public static void setRequestHandler(RequestHandler requestHandler) {
        ZNet.requestHandler = requestHandler;
    }

    public static ResponseHandler getResponseHandler() {
        if (responseHandler==null) responseHandler=new WorkThread.ResponseHandlerImpl();
        return responseHandler;
    }

    public static void setResponseHandler(ResponseHandler responseHandler) {
        ZNet.responseHandler = responseHandler;
    }

    public static RequestExecutor getRequestExecutor() {
        if (requestExecutor==null) requestExecutor=new WorkThread.RequestExecutorImpl();
        return requestExecutor;
    }

    public static void addMockExecutor(MockExecutor mockExecutor) {
        if (ZNet.mockExecutors==null) ZNet.mockExecutors=new LinkedList<>();
        ZNet.mockExecutors.add(mockExecutor);
    }

    public static void removeMockExecutor(MockExecutor mockExecutor){
        if (ZNet.mockExecutors==null) return;
        ZNet.mockExecutors.remove(mockExecutor);
    }

    public static List<MockExecutor> getMockExecutors() {
        return mockExecutors;
    }

    public static void setRequestExecutor(RequestExecutor requestExecutor) {
        ZNet.requestExecutor = requestExecutor;
    }

    public static Context getContext() {
        return context;
    }
}
