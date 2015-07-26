package com.bigzhao.andframe.view.interfaces;

import android.os.Bundle;
import android.view.Menu;

import com.bigzhao.andframe.view.template.ActivityTemplate;

/**
 * Created by Roy on 15-3-20.
 */
public interface IZBaseActivity extends IViewHolder,ITemplateViewHolder<ActivityTemplate> {
    void onCreate(Bundle savedInstanceState);
    boolean onNavigateUp();
    boolean onCreateOptionsMenu(Menu menu);
}
