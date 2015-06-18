package com.z.andframe.view.template;

import android.app.Activity;
import android.os.Bundle;

import com.z.andframe.nav.Nav;
import com.z.andframe.view.interfaces.IZBaseActivity;

/**
 * Created by Roy on 15-3-20.
 */
public class ActivityTemplate extends ViewHolderTemplate<IZBaseActivity>{

    public Activity getActivity(){
        return (Activity) tvh;
    }

    public ActivityTemplate(IZBaseActivity activity){
        super(activity);
        if (!(activity instanceof Activity)) throw new IllegalArgumentException("must be an Activity");
    }

    public boolean onNavigateUp() {
        Nav.from(getActivity()).back();
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
    }
}
