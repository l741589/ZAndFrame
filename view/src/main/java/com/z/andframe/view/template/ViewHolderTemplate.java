package com.z.andframe.view.template;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.z.andframe.binder.ZBinder;
import com.z.andframe.binder.ZEventBinder;
import com.z.andframe.binder.ZOptimizedViewHolder;
import com.z.andframe.binder.tools.EventListenerStub;
import com.z.andframe.nav.Nav;
import com.z.andframe.net.IResponseListener;
import com.z.andframe.net.request.Request;
import com.z.andframe.normal.ZNormalViewEvents;
import com.z.andframe.util.ExceptionHandler;
import com.z.andframe.view.R;
import com.z.andframe.view.interfaces.IMenuInjectable;
import com.z.andframe.view.interfaces.ITemplateViewHolder;
import com.z.andframe.view.interfaces.IViewHolder;
import com.z.andframe.view.interfaces.IZBaseActivity;

import java.util.HashMap;

/**
 * Created by Roy on 15-4-19.
 */
public abstract class ViewHolderTemplate<T extends ITemplateViewHolder> extends ZNormalViewEvents implements IMenuInjectable,IViewHolder,IResponseListener{

    protected T tvh;
    protected ZOptimizedViewHolder viewHolder;
    protected int optionMenuId=0;
    protected EventListenerStub eventListenerStub=new EventListenerStub();
    private final HashMap<String,Object> extentdData=new HashMap<>();
    //protected Dialog dlg=null;

    public abstract Activity getActivity();

    public ViewHolderTemplate(T holder){
        this.tvh =holder;
    }

    protected void onCreate(Bundle savedInstanceState) {
        if (tvh.onPrepare(savedInstanceState)) {
            tvh.onLoaded();
        }
    }

    public boolean onPrepare(Bundle savedInstance){
        if (tvh.needLogin()&&!isLogin()){
           onStartLogin();
            return false;
        }else {
            ZBinder.inject(getActivity(), tvh, this);
            ZBinder.bindEvent(tvh, eventListenerStub);
            getActivity().invalidateOptionsMenu();
            Request<?> req = tvh.initRequest();
            if (req == null) return true;
            else {
               onStartWaiting();
                req.exec();
                return false;
            }
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==getActivity().getResources().getInteger(R.integer.zaf__requestcode_login)&&resultCode==Activity.RESULT_OK&&isLogin()){
            onCreate(null);
            return true;
        }else{
            if (tvh.needLogin()) onLoginFail(requestCode,data);
            return false;
        }
    }

    public void setOptionsMenu(int resId) {
        this.optionMenuId = resId;
        (getActivity()).invalidateOptionsMenu();
    }

    @Override
    public void injectMenuEvent(Menu menu) {
        ZEventBinder.injectMenuEvent(menu, eventListenerStub);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return onCreateOptionsMenu(menu,getActivity().getMenuInflater());
    }

    public boolean onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        if (optionMenuId==0) return true;
        else {
            menu.clear();
            inflater.inflate(optionMenuId, menu);
            injectMenuEvent(menu);
        }
        return true;
    }

    public<T extends Request> T request(Class<T> cls) {
        if (!(tvh instanceof IResponseListener)) return null;
        try {
            T t = cls.newInstance();
            t.setResponseListener((IResponseListener) tvh);
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            ExceptionHandler.Throw(e);
        }
        return null;
    }

    public ZOptimizedViewHolder getViewHolder() {
        return viewHolder;
    }

    @Override
    public void setViewHolder(ZOptimizedViewHolder viewHolder) {
        this.viewHolder=viewHolder;
    }

    @Override
    public View getContentView() {
        return tvh.getContentView();
    }

    @Override
    public void setContentView(View v) {
        tvh.setContentView(v);
    }

    @Override
    public void onSuccess(Request<?> req, Object res) {
       onStopWaiting();
        ZBinder.bindValue(this,res);
        tvh.onLoaded();
    }

    @Override
    public void onError(Request<?> req, Exception e) {
      onNetError(req,e);
    }

    public Object putExtendData(String key,Object val){
        return extentdData.put(key,val);
    }

    public Object getExtendData(String key){
        return extentdData.get(key);
    }

    public EventListenerStub getEventListenerStub() {
        return eventListenerStub;
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    public void onStartWaiting(){ onStartWaiting(this);}
    public void onStopWaiting(){ onStopWaiting(this);}
    public void onNetError(Request<?> request, Exception e){ onNetError(this,request,e);}
    public void onLoginFail(Integer integer, Intent intent){ onLoginFail(this,integer,intent);}
    public void onStartLogin(){ onStartLogin(this);}
    public void setLoginState(Boolean isLogin){ setLoginState(this,isLogin);}
    public Boolean isLogin(){return isLogin(this);}



}
