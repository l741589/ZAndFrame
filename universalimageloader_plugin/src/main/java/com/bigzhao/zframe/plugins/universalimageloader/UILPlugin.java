package com.bigzhao.zframe.plugins.universalimageloader;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.bigzhao.andframe.binder.ZBinder;
import com.bigzhao.andframe.pluginmanager.ZBasePlugin;
import com.bigzhao.andframe.util.T;

/**
 * Created by Roy on 15-4-16.
 */
public class UILPlugin extends ZBasePlugin {

    @Override
    public void onLoad() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplication())
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);
        ZBinder.registerViewValueBinder(ImageView.class, CharSequence.class, new T.Proc2<View, Object>() {
            @Override
            public void f(View view, Object o) {
                ImageView iv = (ImageView) view;
                String url = o.toString();
                ImageLoader.getInstance().displayImage(url, iv);
                iv.setTag(R.id.tag_univeralimageloader_image_url, url);
            }
        });
        ZBinder.registerViewValueGetter(ImageView.class, new T.Func1<Object, View>() {
            @Override
            public Object f(View view) {
                ImageView iv = (ImageView) view;
                Object o = iv.getTag(R.id.tag_univeralimageloader_image_url);
                if (o == null) return iv.getDrawable();
                return o.toString();
            }
        });
    }
}
