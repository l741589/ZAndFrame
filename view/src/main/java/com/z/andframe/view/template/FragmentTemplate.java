package com.z.andframe.view.template;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.z.andframe.view.interfaces.IZBaseFragment;

/**
 * Created by Roy on 15-4-18.
 */
public class FragmentTemplate extends ViewHolderTemplate<IZBaseFragment> {

    private View contentView;
    private FrameLayout container;

    public Activity getActivity(){
        if (tvh instanceof Fragment) return ((Fragment) tvh).getActivity();
        else return ((android.support.v4.app.Fragment) tvh).getActivity();
    }

    public FragmentTemplate(IZBaseFragment fragment){
        super(fragment);
        if (!(tvh instanceof Fragment)&&!(tvh instanceof android.support.v4.app.Fragment)) throw new IllegalArgumentException("must be an Fragment");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (this.container==null) {
            this.container = new FrameLayout(inflater.getContext());
        }
        return this.container;
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    @Override
    public void setContentView(View v) {
        this.contentView=v;
        this.container.removeAllViews();
        this.container.addView(v);
    }

}
