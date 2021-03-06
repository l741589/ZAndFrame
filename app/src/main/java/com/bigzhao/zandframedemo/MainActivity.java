package com.bigzhao.zandframedemo;

import android.content.Context;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigzhao.andframe.zorm.ZOrm;
import com.j256.ormlite.dao.Dao;
import com.bigzhao.andframe.binder.ZBinder;
import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.binder.annotions.ZEvent;
import com.bigzhao.andframe.nav.Nav;
import com.bigzhao.andframe.net.ZNet;
import com.bigzhao.andframe.net.request.FormRequest;
import com.bigzhao.andframe.net.request.JsonRequest;
import com.bigzhao.andframe.net.request.Request;
import com.bigzhao.andframe.plugin.ormlite.ZDataBase;
import com.bigzhao.andframe.plugin.ormlite.ZDatabaseHelper;
import com.bigzhao.andframe.view.ZBaseActivity;
import com.bigzhao.zandframedemo.db.TestModel;
import com.bigzhao.zandframedemo.request.Test_Get;

import java.sql.SQLException;
import java.util.List;

@ZBind(id=R.layout.activity_main)
public class MainActivity extends ZBaseActivity {

    @ZBind Context context;
    @ZBind(name="textView") private TextView textView1;
    @ZBind private TextView textView2;
    @ZBind("text,>num.toColor:textColor") private TextView textView3;
    @ZBind("num.toString") private TextView textView4;
    @ZBind("text") private ImageView imageView;
    @ZBind private String app_name;
    @ZBind private EditText editText;
    @ZBind private ZDatabaseHelper db;
    @ZDataBase(where="name=435") private TestModel tm;
    @ZDataBase(table = TestModel.class) private Dao<TestModel,?> tmdao;
    //@ZBind private Button button;
    //@ZBind private Button button2;
    @SuppressWarnings("NewApi")
    private final String contentUri= Telephony.Sms.Sent.CONTENT_URI.toString();
    @ZOrm(table ="var://android.provider.Telephony$Sms$Sent/CONTENT_URI")
    private List<Object> obj;

    @Override
    public Request<?> initRequest() {
        return null;
        //request(FormRequest.class).set("number",123).set("text","123").setPath("Test/Get").exec();
        //return  request(FormRequest.class).add(ZBinder.createForm(this)).setPath("Test/Get");
    }

    @Override
    public void onLoaded() {
        textView1.setText(app_name);
        TestModel t=new TestModel();
        t.name="435";
        try {
            tmdao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //t=Model.load(TestModel.class,435);

    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    public void onError(Request<?> req, Exception e) {
        super.onError(req,e);
        textView4.setText(req + "," + e);
    }

    @Override
    public void onSuccess(Request<?> req, Object res) {
        super.onSuccess(req, res);
    }

    @ZEvent(event = EVENT_CLICK, target = "textView1")
    public void textViewClick(View v){
        toast("tv clicked");
    }

    @ZEvent(event =EVENT_LONG_CLICK, target = {"@button","@button2"},id=R.id.editText)
    public boolean button1LongClick(View v){
        toast("bn1 long click");
        Nav.from(this).to(FActivity.class);

        return true;
    }

    @ZEvent(event = EVENT_MENU_ITEM_CLICK,id=R.id.action_u1)
    public boolean action_u1_MenuItemClick(MenuItem item){
       toast("u1 clicked");
        return true;
    }

    @ZEvent()
    public boolean $action_u2_MenuItemClick(MenuItem item){
        toast("u2 clicked:"+item.getItemId());
        return true;
    }

    @ZEvent
    public boolean _action_settings_MenuItemClick(MenuItem item){
        toast("setting click");
        return true;
    }
}
