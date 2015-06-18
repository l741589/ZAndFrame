package com.z.andframe.view.interfaces;

import android.view.View;

import com.z.andframe.binder.ZOptimizedViewHolder;

/**
 * Created by Roy on 15-3-28.
 */
public interface IViewHolder {
    ZOptimizedViewHolder getViewHolder();
    void setViewHolder(ZOptimizedViewHolder viewHolder);
    View getContentView();
    void setContentView(View v);
}
