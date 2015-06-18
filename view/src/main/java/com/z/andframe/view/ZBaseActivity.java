package com.z.andframe.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.z.andframe.view.interfaces.EventType;
import com.z.andframe.binder.ZOptimizedViewHolder;
import com.z.andframe.net.IResponseListener;
import com.z.andframe.net.request.Request;
import com.z.andframe.view.interfaces.IZBaseActivity;
import com.z.andframe.view.template.ActivityTemplate;


public class ZBaseActivity extends Activity implements EventType,IZBaseActivity,IResponseListener {

    private ActivityTemplate t=new ActivityTemplate(this);

    public ActivityTemplate getTemplate(){return t;}
    public boolean onPrepare(Bundle savedInstanceState){
        return getTemplate().onPrepare(savedInstanceState);
    }
    public void onLoaded(){}
    public boolean needLogin(){
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTemplate().onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return getTemplate().onCreateOptionsMenu(menu);
    }

    @Override
    public Request<?> initRequest() {
        return null;
    }

    public ZOptimizedViewHolder getViewHolder(){
        return getTemplate().getViewHolder();
    }

    @Override
    public void setViewHolder(ZOptimizedViewHolder viewHolder) {
        getTemplate().setViewHolder(viewHolder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTemplate().onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onSuccess(Request<?> req, Object res) {
       getTemplate().onSuccess(req,res);
    }

    @Override
    public void onError(Request<?> req, Exception e) {
        getTemplate().onError(req,e);
    }
    public View getContentView()  {
        return ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
    }
    /////////TOOLS/////////

    public void toast(String s){
        Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
    }

    public<T extends Request>  T request(Class<T> cls){
        return getTemplate().request(cls);
    }

    @Override
    public boolean onNavigateUp() {
        return getTemplate().onNavigateUp();
    }

    ///////De////////////


    /**
     * @deprecated use {@link com.z.andframe.nav.Nav} instead
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * @deprecated use {@link com.z.andframe.nav.Nav} instead
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * @deprecated use {@link com.z.andframe.nav.Nav} instead
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

}
