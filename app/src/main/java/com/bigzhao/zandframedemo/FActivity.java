package com.bigzhao.zandframedemo;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigzhao.andframe.binder.ZBinder;
import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.binder.annotions.ZEvent;
import com.bigzhao.andframe.nav.Nav;
import com.bigzhao.andframe.net.IResponseListener;
import com.bigzhao.andframe.net.request.JsonRequest;
import com.bigzhao.andframe.net.request.Request;
import com.bigzhao.andframe.view.ZBaseActivity;
import com.bigzhao.andframe.view.ZBaseFragment;

@ZBind(id=R.layout.zaf__fragment_activtiy)
public class FActivity extends ZBaseActivity {
    @ZBind(id=R.layout.activity_main,menuId=R.menu.menu3)
    public static class MyFragment extends ZBaseFragment {

        @ZBind
        Context context;
        private
        @ZBind(name = "textView")
        TextView textView1;
        @ZBind
        private TextView textView2;
        @ZBind("text,>num.toColor:textColor")
        private TextView textView3;
        @ZBind("num.toString")
        private TextView textView4;
        @ZBind("text")
        private ImageView imageView;
        @ZBind
        private String app_name;
        @ZBind
        private EditText editText;
        //@ZBind private Button button;
        //@ZBind private Button button2;


        @Override
        public Request<?> initRequest() {
            return request(JsonRequest.class).setEntity(ZBinder.createForm(this)).setPath("Test/Get");
        }

        @Override
        public void onLoaded() {
            textView1.setText(app_name);

            //request(FormRequest.class).set("abc","def").set("X",0).setPath("Test/Get").setResponseType(String.class).post();
        }


        @Override
        public boolean needLogin() {
            return true;
        }

        @Override
        public void onError(Request<?> req, Exception e) {
            super.onError(req,e);
            textView4.setText(req + "," + e);
        }

        @ZEvent(event = EVENT_CLICK, target = "textView1")
        public void textViewClick(View v) {
            toast("tv clicked");
        }

        @ZEvent(event = EVENT_LONG_CLICK, target = {"@button", "@button2"}, id = R.id.editText)
        public boolean button1LongClick(View v) {
            toast("bn1 long click");
            Nav.from(getActivity()).to(ListActivity.class);
            return true;
        }

        @ZEvent(event = EVENT_MENU_ITEM_CLICK, id = R.id.action_u1)
        public boolean action_u1_MenuItemClick(MenuItem item) {
            toast("u1 clicked");
            return true;
        }

        @ZEvent()
        public boolean $action_u2_MenuItemClick(MenuItem item) {
            toast("u2 clicked:" + item.getItemId());
            return true;
        }

        @ZEvent
        public boolean _action_settings_MenuItemClick(MenuItem item) {
            toast("setting click");
            return true;
        }
    }

    @Override
    public void onLoaded() {
        Nav.from(this).to(MyFragment.class);
    }
}
