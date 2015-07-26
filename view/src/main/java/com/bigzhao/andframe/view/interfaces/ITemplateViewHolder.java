package com.bigzhao.andframe.view.interfaces;

import android.os.Bundle;
import android.view.Menu;

import com.bigzhao.andframe.net.request.Request;
import com.bigzhao.andframe.view.interfaces.IViewHolder;
import com.bigzhao.andframe.view.template.ViewHolderTemplate;

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
