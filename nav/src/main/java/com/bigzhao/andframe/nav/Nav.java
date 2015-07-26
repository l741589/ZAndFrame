package com.bigzhao.andframe.nav;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Roy on 15-3-18.
 */
public class Nav implements NavTags {

    static final int FRAGMENT_SUPPORT_VER=Build.VERSION_CODES.HONEYCOMB;
    Context context;
    Intent intent = new Intent();
    private static HashMap<String, Intent> template = new HashMap<>();

    public static void setTemplate(String key, Intent intent) {
        template.put(key, intent);
    }

    public static void setTemplate(Intent intent) {
        setTemplate(null, intent);
    }

    private Nav(Context context) {
        this.context = context;
        if (template.get(null) == null) intent = new Intent();
        else intent = new Intent(template.get(null));
    }

    private Nav(Context context, Intent template) {
        this.context = context;
        this.intent = new Intent(template);
    }

    private Nav(Context context, String templateName) {
        this.context = context;
        this.intent = new Intent(template.get(templateName));
    }

    public static Nav from(Context context) {
        return new Nav(context);
    }

    public static Nav from(Context context,Intent template) {
        return new Nav(context,template);
    }

    public static Nav from(Context context,String template) {
        return new Nav(context,template);
    }

    public Nav flag(int flags) {
        intent.addFlags(flags);
        return this;
    }

    public Nav flag(int... flags) {
        int f = 0;
        for (int e : flags) f |= e;
        intent.addFlags(f);
        return this;
    }

    public Nav category(String category){
        intent.addCategory(category);
        return this;
    }

    public Nav category(String... categories){
        for (String category:categories)
            intent.addCategory(category);
        return this;
    }

    public Nav action(String action){
        intent.setAction(action);
        return this;
    }

    public Intent getIntent(){return intent;}
    public Nav prepare(IntentPreparer preparer){
        preparer.prepare(intent);
        return this;
    }

    public Nav pkg(String pkg){
        intent.setPackage(pkg);
        return this;
    }

   	public Nav put(String name, String value)     { intent.putExtra(name, value); return this; }
    public Nav put(String name, CharSequence value)     { intent.putExtra(name, value); return this; }
    public Nav put(String name, byte value)       { intent.putExtra(name, value); return this; }
    public Nav put(String name, short value)       { intent.putExtra(name, value); return this; }
	public Nav put(String name, int value)       { intent.putExtra(name, value); return this; }
    public Nav put(String name, long value)       { intent.putExtra(name, value); return this; }
	public Nav put(String name, boolean value)	{ intent.putExtra(name, value); return this; }
	public Nav put(String name, float value)		{ intent.putExtra(name, value); return this; }
	public Nav put(String name, double value)	{ intent.putExtra(name, value); return this; }
	public Nav put(String name, char value)		{ intent.putExtra(name, value); return this; }
	public Nav put(String name, Serializable value){ intent.putExtra(name, value); return this; }
    public Nav put(String name, Parcelable value){ intent.putExtra(name, value); return this; }
	public Nav put(String name, Bundle value)		{ intent.putExtra(name, value); return this; }
    public Nav put(String name, String[] value)     { intent.putExtra(name, value); return this; }
    public Nav put(String name, CharSequence[] value)     { intent.putExtra(name, value); return this; }
    public Nav put(String name, byte[] value)       { intent.putExtra(name, value); return this; }
    public Nav put(String name, short[] value)       { intent.putExtra(name, value); return this; }
    public Nav put(String name, int[] value)       { intent.putExtra(name, value); return this; }
    public Nav put(String name, long[] value)       { intent.putExtra(name, value); return this; }
    public Nav put(String name, boolean[] value)	{ intent.putExtra(name, value); return this; }
    public Nav put(String name, float[] value)		{ intent.putExtra(name, value); return this; }
    public Nav put(String name, double[] value)	{ intent.putExtra(name, value); return this; }
    public Nav put(String name, char[] value)		{ intent.putExtra(name, value); return this; }
    public Nav put(String name, Parcelable[] value){ intent.putExtra(name, value); return this; }



    public FragmentTransaction beginFragmentTransaction(){
        return ((Activity)context).getFragmentManager().beginTransaction();
    }

    public void clearFragment(){
        clearFragment(false);
    }

    public void clearFragment(boolean fallback){
        if (Build.VERSION.SDK_INT<FRAGMENT_SUPPORT_VER||fallback) {
            ((FragmentActivity)context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            ((Activity) context).getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public boolean backFragment(){
        return backFragment(false);
    }

    public boolean backFragment(boolean fallback){
        if (Build.VERSION.SDK_INT<FRAGMENT_SUPPORT_VER||fallback) {
            if (((FragmentActivity) context).getSupportFragmentManager().getBackStackEntryCount() > 0) {
                ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
                return true;
            }
        }else{
            if (((Activity) context).getFragmentManager().getBackStackEntryCount() > 0) {
                ((Activity) context).getFragmentManager().popBackStack();
                return true;
            }
        }
        return false;
    }

    public void back(){
        if (!backFragment()){
            ((Activity)context).finish();
        }
    }

    public FragmentNav fragment(){
        return fragment(false);
    }

    public FragmentNav fragment(boolean fallback){
        return new FragmentNav(this,fallback);
    }

    public void to(){
        try {
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context,"TO:"+intent,Toast.LENGTH_LONG).show();
            Log.e("Nav","TO:"+intent);
        }
    }

    public void to(int requestCode){
        if (context instanceof Activity){
            try {
                ((Activity)context).startActivityForResult(intent,requestCode);
            }catch (ActivityNotFoundException e){
                Toast.makeText(context,"TO:"+intent+"\nCODE:"+requestCode,Toast.LENGTH_LONG).show();
                Log.e("Nav","TO:"+intent+"\nCODE:"+requestCode);
            }
        }
    }

    public void to(Intent intent){
       this.intent=intent;
        to();
    }

    public void to(Intent intent,int requestCode){
        this.intent=intent;
        to(requestCode);
    }

    public void to(String uri) {
        to(Uri.parse(uri));
    }

    public void to(Uri uri) {
        intent.setData(uri);
        to(intent);
    }


    public void to(Class<? extends Activity> target,int requestCode) {
        intent.setClass(context, target);
        if (context instanceof Activity){
            to(intent,requestCode);
        }
    }

    public void to(String uri,int requestCode) {
        to(Uri.parse(uri),requestCode);
    }

    public void to(Uri uri,int requestCode) {
        intent.setData(uri);
        if (context instanceof Activity){
            to(intent,requestCode);
        }
    }

    @SuppressWarnings("unchecked")
    public void to(Class<?> target){
        if (Activity.class.isAssignableFrom(target)) toA((Class<? extends Activity>)target);
        else if (Fragment.class.isAssignableFrom(target)) toF((Class<? extends Fragment>)target);
        else if (android.support.v4.app.Fragment.class.isAssignableFrom(target)) toFS((Class<? extends android.support.v4.app.Fragment>) target);
    }

    public void toA(Class<? extends Activity> target) {
        intent.setClass(context, target);
        to(intent);
    }

    public void toF(Class<? extends Fragment> target) {
        try {
            to(target.newInstance());
        } catch (InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void toFS(Class<? extends android.support.v4.app.Fragment> target) {
        try {
            to(target.newInstance());
        } catch (InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void to(Fragment f){
        if (intent.getExtras()!=null) f.setArguments(intent.getExtras());
        fragment().add(f).commit();
    }

    public void to(android.support.v4.app.Fragment f){
        if (intent.getExtras()!=null) f.setArguments(intent.getExtras());
        fragment().add(f).commit();
    }

    public void finish(int result){
        Activity a=((Activity)context);
        a.setResult(result,intent);
        a.finish();
    }

    public void removeAllTags(){
        Field[] fs=NavTags.class.getFields();
        for (Field f:fs) {
            try {
                intent.getExtras().remove((String)f.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
