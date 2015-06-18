package com.z.andframe.pluginmanager;

import android.app.Application;

/**
 * Created by Roy on 15-5-21.
 */
@NotPlugin
public abstract class ZBasePlugin {
    protected Application app;

    public Application getApplication() {
        return app;
    }

    void setApplication(Application application) {
        app=application;
    }

    public abstract void onLoad();
    public boolean onScan(Class<?> cls){
        return false;
    }
}
