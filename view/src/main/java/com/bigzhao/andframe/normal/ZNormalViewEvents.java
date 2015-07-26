package com.bigzhao.andframe.normal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.bigzhao.andframe.nav.Nav;
import com.bigzhao.andframe.net.request.Request;
import com.bigzhao.andframe.util.T;
import com.bigzhao.andframe.view.R;
import com.bigzhao.andframe.view.template.ViewHolderTemplate;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by Roy on 15-4-19.
 */
public class ZNormalViewEvents {

    public static final String TAG_WAITINGDIALOG="$ZNormalViewEvents$WaitingDialog";


	public static void onStartWaiting(final ViewHolderTemplate<?> o){ onStartWaiting.f(o);}
	public static T.Proc1<ViewHolderTemplate<?>> onStartWaiting=new T.Proc1<ViewHolderTemplate<?>>() {
        @Override
        public void f(final ViewHolderTemplate<?> o) {
            Dialog dlg= ProgressDialog.show(o.getActivity(), null, "请稍等", true);
            Object a=o.putExtendData(TAG_WAITINGDIALOG, dlg);
            if (a!=null&&a instanceof Dialog) ((Dialog)a).dismiss();
            dlg.setCancelable(true);
            dlg.setCanceledOnTouchOutside(false);
            dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Nav.from(o.getActivity()).back();
                }
            });

        }
    };
  	public static void onStopWaiting(ViewHolderTemplate<?> o){onStopWaiting.f(o); }
	public static T.Proc1<ViewHolderTemplate<?>> onStopWaiting=new T.Proc1<ViewHolderTemplate<?>>() {
        @Override
        public void f(ViewHolderTemplate<?> o) {
            Object a=o.getExtendData(TAG_WAITINGDIALOG);
            if (a!=null&&a instanceof Dialog) ((Dialog)a).dismiss();
        }
    };

    public static void onNetError(final ViewHolderTemplate o, Request<?> request, Exception e){ onNetError.f(o,request,e);}
	public static T.Proc3<ViewHolderTemplate,Request<?>,Exception> onNetError=new T.Proc3<ViewHolderTemplate, Request<?>, Exception>() {
        @Override
        public void f(final ViewHolderTemplate o, Request<?> request, Exception e) {
            onStopWaiting.f(o);
            final Dialog dlg=new AlertDialog.Builder(o.getActivity())
                    .setMessage("错误:"+e.getMessage())
                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Nav.from(o.getActivity()).back();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Nav.from(o.getActivity()).back();
                        }
                    })
                    .show();
        }
    };


   public static void onLoginFail(ViewHolderTemplate viewHolderTemplate, Integer integer, Intent intent){ onLoginFail.f(viewHolderTemplate,integer,intent);}
	public static T.Proc3<ViewHolderTemplate,Integer,Intent> onLoginFail=new T.Proc3<ViewHolderTemplate, Integer, Intent>() {
        @Override
        public void f(ViewHolderTemplate viewHolderTemplate, Integer integer, Intent intent) {
            Nav.from(viewHolderTemplate.getActivity()).back();
        }
    };

    public static void onStartLogin(ViewHolderTemplate<?> t){ onStartLogin.f(t);}
	public static T.Proc1<ViewHolderTemplate<?>> onStartLogin=new T.Proc1<ViewHolderTemplate<?>>() {
        @Override
        public void f(ViewHolderTemplate<?> t) {
            Activity a=t.getActivity();
            int requestCode=t.getActivity().getResources().getInteger(R.integer.zaf__requestcode_login);
            Nav.from(a).action("com.zandframe.action.LOGIN").to(requestCode);
        }
    };

    public static void setLoginState(ViewHolderTemplate<?> t, Boolean isLogin){ setLoginState.f(t,isLogin);}
	public static T.Proc2<ViewHolderTemplate<?>,Boolean> setLoginState=new T.Proc2<ViewHolderTemplate<?>, Boolean>() {
        @Override
        public void f(ViewHolderTemplate<?> t, Boolean isLogin) {
            t.getActivity().getSharedPreferences("ZAndFrame__state",Activity.MODE_PRIVATE).edit().putBoolean("isLogin",isLogin).apply();
        }
    };

   public static Boolean isLogin(ViewHolderTemplate<?> t){ return isLogin.f(t);}
	public static T.Func1<Boolean,ViewHolderTemplate<?>> isLogin=new T.Func1<Boolean, ViewHolderTemplate<?>>() {
        @Override
        public Boolean f(ViewHolderTemplate<?> t) {
            return t.getActivity().getSharedPreferences("ZAndFrame__state",Activity.MODE_PRIVATE).getBoolean("isLogin",false);
        }
    };
}
