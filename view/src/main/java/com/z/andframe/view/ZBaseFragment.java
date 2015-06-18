package com.z.andframe.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.z.andframe.binder.ZOptimizedViewHolder;
import com.z.andframe.net.IResponseListener;
import com.z.andframe.net.request.Request;
import com.z.andframe.view.interfaces.EventType;
import com.z.andframe.view.interfaces.IZBaseFragment;
import com.z.andframe.view.template.FragmentTemplate;

/**
 * Created by Roy on 15-4-18.
 */
public class ZBaseFragment extends Fragment implements IZBaseFragment,IResponseListener,EventType {

    private FragmentTemplate t=new FragmentTemplate(this);

    @Override
    public FragmentTemplate getTemplate() {
        return t;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTemplate().onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onPrepare(Bundle savedInstanceState) {
        return getTemplate().onPrepare(savedInstanceState);
    }

    @Override
    public void onLoaded() {
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getTemplate().onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Request<?> initRequest() {
        return null;
    }

    @Override
    public ZOptimizedViewHolder getViewHolder() {
        return getTemplate().getViewHolder();
    }

    @Override
    public void setViewHolder(ZOptimizedViewHolder viewHolder) {
        getTemplate().setViewHolder(viewHolder);
    }

    @Override
    public View getContentView() {
        return getTemplate().getContentView();
    }

    @Override
    public void setContentView(View v) {
        getTemplate().setContentView(v);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getTemplate().onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onSuccess(Request<?> req, Object res) {
        getTemplate().onSuccess(req,res);
    }

    @Override
    public void onError(Request<?> req, Exception e) {
        getTemplate().onError(req,e);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTemplate().onActivityResult(requestCode,resultCode,data);
    }

    /////////TOOLS/////////

    public void toast(String s){
        Toast.makeText(getTemplate().getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    public<T extends Request>  T request(Class<T> cls){
        return getTemplate().request(cls);
    }


}
