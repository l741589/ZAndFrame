package com.z.andframe.view.interfaces;

import com.z.andframe.view.interfaces.IViewHolder;

/**
 * Created by Roy on 15-3-29.
 */
public interface IMultiViewHolder extends IViewHolder {
    Class<?> getItemViewHolder(int position);
    int getItemViewHolderCount();
}