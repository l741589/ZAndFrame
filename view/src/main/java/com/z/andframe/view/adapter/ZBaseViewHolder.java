package com.z.andframe.view.adapter;

import android.content.Context;
import android.view.View;

import com.z.andframe.binder.ZBinder;
import com.z.andframe.binder.ZOptimizedViewHolder;
import com.z.andframe.binder.tools.EventListenerStub;
import com.z.andframe.view.R;
import com.z.andframe.view.interfaces.IViewHolder;
import com.z.andframe.view.interfaces.IZBaseViewHolder;

/**
 * Created by Roy on 15-3-29.
 */
public class ZBaseViewHolder implements IZBaseViewHolder {

    protected EventListenerStub eventListenerStub=new EventListenerStub();
    protected Object data;
    protected int position;
    public static interface ICanBeDisabled{
        boolean isEnabled(int position);
    }

    private ZOptimizedViewHolder viewHolder;
    private View contentView;

    @Override
    public ZOptimizedViewHolder getViewHolder() {
        if (viewHolder==null) viewHolder=new ZOptimizedViewHolder();
        return viewHolder;
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    @Override
    public void setContentView(View v) {
        contentView=v;
    }

    @Override
    public void setViewHolder(ZOptimizedViewHolder viewHolder) {
        this.viewHolder=viewHolder;
    }

    @Override
    public void onLoaded(){}

    @Override
    public void onUpdate(Object data, int position) {
        this.data=data;
        this.position=position;
        ZBinder.bindValue(this,data);
    }

    @Override
    public View onPrepare(Context context) {
        ZBinder.inject(context, this, null);
        View v = this.getContentView();
        ZBinder.bindEvent(this, eventListenerStub);
        return v;
    }
}
