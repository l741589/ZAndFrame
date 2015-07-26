package com.bigzhao.andframe.binder;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by Roy on 15-3-19.
 */
public class ZBaseBinder {

    protected static Context context;
    protected static Resources resources;
    protected static AssetManager assets;

    public static void init(Application application) {
        context=application;
        resources=context.getResources();
        assets=context.getAssets();
    }
}
