package com.bigzhao.andframe.view.interfaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigzhao.andframe.view.template.FragmentTemplate;

/**
 * Created by Roy on 15-4-18.
 */
public interface IZBaseFragment extends ITemplateViewHolder<FragmentTemplate> {
    void onActivityCreated(Bundle savedInstanceState);
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    void onCreateOptionsMenu(Menu menu,MenuInflater inflater);
}
