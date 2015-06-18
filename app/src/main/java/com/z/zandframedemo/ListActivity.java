package com.z.zandframedemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.z.andframe.binder.ZBinder;
import com.z.andframe.binder.annotions.ZBind;
import com.z.andframe.binder.annotions.ZEvent;
import com.z.andframe.net.request.JsonRequest;
import com.z.andframe.net.request.Request;
import com.z.andframe.view.ZBaseActivity;
import com.z.andframe.view.adapter.ZBaseViewHolder;
import com.z.zandframedemo.request.Test_Get;

/**
 * Created by Roy on 15-3-29.
 */
@ZBind
public class ListActivity extends ZBaseActivity {

    @ZBind(value="obj",viewHolder = Item.class) ListView listView;

    @ZBind(id=R.layout.activity_main)
    public static class Item extends ZBaseViewHolder implements ZBaseViewHolder.ICanBeDisabled{
        @ZBind("text") TextView textView;
        @ZBind("num.toString") Button button;
        @ZBind Context context;
        @ZBind("text") ImageView imageView;

        @Override
        public boolean isEnabled(int position) {
            return position%2==0;
        }

        @ZEvent
        void _button2_Click(View v){
            Toast.makeText(context,"button2",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Request<?> initRequest() {
        return request(JsonRequest.class).setEntity(ZBinder.createForm(this)).setPath("Test/Get");


    }
}
