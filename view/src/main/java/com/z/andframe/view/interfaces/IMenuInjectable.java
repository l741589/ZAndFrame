package com.z.andframe.view.interfaces;

import android.view.Menu;

/**
 * Created by Roy on 15-3-20.
 */
public interface IMenuInjectable {
    void setOptionsMenu(int resId);
    void injectMenuEvent(Menu menu);
}
