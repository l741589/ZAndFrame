package com.bigzhao.andframe.view.adapter;

import android.content.Context;
import android.view.View;

import com.bigzhao.andframe.binder.ZOptimizedViewHolder;
import com.bigzhao.andframe.view.interfaces.IMultiViewHolder;
import com.bigzhao.andframe.view.interfaces.IZBaseViewHolder;

/**
 * Created by Roy on 15-3-29.
 */
public abstract class ZMultiViewHolder implements IZBaseViewHolder,IMultiViewHolder{
    public abstract Class<? extends ZBaseViewHolder> getItemViewHolder(int position);
    public abstract int getItemViewHolderCount();

    @Override
    public ZOptimizedViewHolder getViewHolder() {
        return null;
    }
    @Override
    public void setViewHolder(ZOptimizedViewHolder viewHolder) {}
    @Override
    public View getContentView() {
        return null;
    }

    @Override
    public View onPrepare(Context context) {
        return null;
    }

    @Override
    public void onUpdate(Object data, int position) {

    }

    @Override
    public void onLoaded() {

    }
}
