package com.z.andframe.view.interfaces;

import android.os.Bundle;
import android.view.Menu;

import com.z.andframe.net.request.Request;
import com.z.andframe.view.interfaces.IViewHolder;
import com.z.andframe.view.template.ViewHolderTemplate;

/**
 * Created by Roy on 15-4-19.
 */
public interface ITemplateViewHolder<T extends ViewHolderTemplate> extends IViewHolder{
    T getTemplate();
    boolean onPrepare(Bundle savedInstanceState);
    void onLoaded();
    boolean needLogin();
    Request<?> initRequest();

}
