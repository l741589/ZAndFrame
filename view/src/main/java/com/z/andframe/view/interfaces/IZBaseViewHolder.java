package com.z.andframe.view.interfaces;

import android.content.Context;
import android.view.View;

import com.z.andframe.binder.tools.EventListenerStub;
import com.z.andframe.view.interfaces.IViewHolder;

/**
 * Created by Roy on 15-6-6.
 */
public interface IZBaseViewHolder extends IViewHolder{
    void onLoaded();

    void onUpdate(Object data,int position);

    View onPrepare(Context context);
}
