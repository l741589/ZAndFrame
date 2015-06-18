package com.z.andframe.nav;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by Roy on 15-3-30.
 */
@SuppressWarnings("unused")
public class FragmentNav{

    private Nav nav;
    private Activity a;
    private FragmentManager fm;
    private android.support.v4.app.FragmentManager sfm;
    private FragmentTransaction ft;
    private android.support.v4.app.FragmentTransaction sft;
    private int containerViewId=R.id.fragment_container;
    private boolean fallback;

    private boolean doCompat(){
        return fallback||Build.VERSION.SDK_INT<Nav.FRAGMENT_SUPPORT_VER;
    }

    FragmentNav(Nav nav,boolean fallback){
        this.nav=nav;
        a=(Activity)nav.context;
        this.fallback=fallback;
        if (Build.VERSION.SDK_INT>=Nav.FRAGMENT_SUPPORT_VER) {
            fm = a.getFragmentManager();
        }
        if (a instanceof FragmentActivity) {
            sfm = ((FragmentActivity) a).getSupportFragmentManager();
        }
    }

    public android.support.v4.app.FragmentTransaction getFragmentTransactionS() {
        if (sft==null) sft=sfm.beginTransaction();
		return sft;
    }

    public FragmentTransaction getFragmentTransaction() {
        if (ft==null) ft=fm.beginTransaction();
        return ft;
    }



    public FragmentManager getFragmentManager() {
        return fm;
    }
    public android.support.v4.app.FragmentManager getFragmentManagerS() {
        return sfm;
    }

    public FragmentNav add(Fragment fragment) {
       getFragmentTransaction().add(containerViewId, fragment);
		return this;
    }

    public FragmentNav add(android.support.v4.app.Fragment fragment) {
        getFragmentTransactionS().add(containerViewId, fragment);
        return this;
    }

    public FragmentNav replace(Fragment fragment, String tag) {
        getFragmentTransaction().replace(containerViewId, fragment, tag);
        return this;
    }

    public FragmentNav replace(android.support.v4.app.Fragment fragment, String tag) {
        getFragmentTransactionS().replace(containerViewId, fragment, tag);
        return this;
    }

    public FragmentNav setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
        if (doCompat()) getFragmentTransactionS().setCustomAnimations(enter, exit, popEnter, popExit);
        else getFragmentTransaction().setCustomAnimations(enter, exit, popEnter, popExit);
		return this;
    }

    public FragmentNav addToBackStack(String name) {
        if (doCompat()) getFragmentTransactionS().addToBackStack(name);
        else getFragmentTransaction().addToBackStack(name);
		return this;
    }

    public FragmentNav setBreadCrumbShortTitle(int res) {
        if (doCompat()) getFragmentTransactionS().setBreadCrumbShortTitle(res);
        else getFragmentTransaction().setBreadCrumbShortTitle(res);
		return this;
    }

    public FragmentNav replace(Fragment fragment) {
        getFragmentTransaction().replace(containerViewId, fragment);
		return this;
    }

    public FragmentNav replace(android.support.v4.app.Fragment fragment) {
        getFragmentTransactionS().replace(containerViewId, fragment);
        return this;
    }

    public FragmentNav hide(Fragment fragment) {
        getFragmentTransaction().hide(fragment);
		return this;
    }

    public FragmentNav hide(android.support.v4.app.Fragment fragment) {
        getFragmentTransactionS().hide(fragment);
        return this;
    }

    public FragmentNav disallowAddToBackStack() {
        if (doCompat()) getFragmentTransactionS().disallowAddToBackStack();
        else getFragmentTransaction().disallowAddToBackStack();
		return this;
    }

    public int commitAllowingStateLoss() {
        if (doCompat()) return getFragmentTransactionS().commitAllowingStateLoss();
        return getFragmentTransaction().commitAllowingStateLoss();
    }

    @SuppressWarnings("NewApi")
    public FragmentNav addSharedElement(View sharedElement, String name) {
        if (doCompat()) getFragmentTransactionS().addSharedElement(sharedElement, name);
        else getFragmentTransaction().addSharedElement(sharedElement, name);
		return this;
    }

    public FragmentNav show(Fragment fragment) {
        getFragmentTransaction().show(fragment);
		return this;
    }

    public FragmentNav show(android.support.v4.app.Fragment fragment) {
        getFragmentTransactionS().show(fragment);
        return this;
    }

    public FragmentNav setBreadCrumbTitle(int res) {
        if (doCompat()) getFragmentTransactionS().setBreadCrumbTitle(res);
        else getFragmentTransaction().setBreadCrumbTitle(res);
		return this;
    }

    public FragmentNav setTransition(int transit) {
        if (doCompat()) getFragmentTransactionS().setTransition(transit);
        else getFragmentTransaction().setTransition(transit);
		return this;
    }

    public boolean isAddToBackStackAllowed() {
        if (doCompat()) return getFragmentTransactionS().isAddToBackStackAllowed();
        return getFragmentTransaction().isAddToBackStackAllowed();
    }

    public FragmentNav add(Fragment fragment, String tag) {
        getFragmentTransaction().add(containerViewId, fragment, tag);
		return this;
    }

    public FragmentNav add(android.support.v4.app.Fragment fragment, String tag) {
        getFragmentTransactionS().add(containerViewId, fragment, tag);
        return this;
    }

    public FragmentNav setBreadCrumbShortTitle(CharSequence text) {
        if (doCompat()) getFragmentTransactionS().setBreadCrumbShortTitle(text);
        else getFragmentTransaction().setBreadCrumbShortTitle(text);
		return this;
    }

    public FragmentNav attach(Fragment fragment) {
        getFragmentTransaction().attach(fragment);
		return this;
    }

    public FragmentNav attach(android.support.v4.app.Fragment fragment) {
        getFragmentTransactionS().attach(fragment);
        return this;
    }

    public FragmentNav remove(Fragment fragment) {
        getFragmentTransaction().remove(fragment);
		return this;
    }

    public FragmentNav remove(android.support.v4.app.Fragment fragment) {
        getFragmentTransactionS().remove(fragment);
        return this;
    }

    public FragmentNav setCustomAnimations(int enter, int exit) {
        if (doCompat()) getFragmentTransactionS().setCustomAnimations(enter, exit);
        else getFragmentTransaction().setCustomAnimations(enter, exit);
		return this;
    }

    public FragmentNav setTransitionStyle(int styleRes) {
        if (doCompat()) getFragmentTransactionS().setTransitionStyle(styleRes);
        else getFragmentTransaction().setTransitionStyle(styleRes);
		return this;
    }

    public FragmentNav detach(Fragment fragment) {
        getFragmentTransaction().detach(fragment);
		return this;
    }

    public FragmentNav detach(android.support.v4.app.Fragment fragment) {
        getFragmentTransactionS().detach(fragment);
        return this;
    }

    public FragmentNav setBreadCrumbTitle(CharSequence text) {
        if (doCompat()) getFragmentTransactionS().setBreadCrumbTitle(text);
        else getFragmentTransaction().setBreadCrumbTitle(text);
		return this;
    }

    public Nav commit() {
        if (doCompat()) getFragmentTransactionS().commit();
        else getFragmentTransaction().commit();
		return nav;
    }

    ////////////////////////////////////////////////////////////////////////////////////


    public FragmentTransaction beginTransaction() {
        return getFragmentTransaction();
    }

    public android.support.v4.app.FragmentTransaction beginTransactionS() {
        return getFragmentTransactionS();
    }

    public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener listener) {
        getFragmentManager().removeOnBackStackChangedListener(listener);
    }

    public void removeOnBackStackChangedListener(android.support.v4.app.FragmentManager.OnBackStackChangedListener listener) {
        getFragmentManagerS().removeOnBackStackChangedListener(listener);
    }

    public static void enableDebugLogging(boolean enabled) {
        FragmentManager.enableDebugLogging(enabled);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        if (doCompat()) return getFragmentManagerS().popBackStackImmediate(name, flags);
        else return getFragmentManager().popBackStackImmediate(name, flags);
    }

    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener listener) {
        getFragmentManager().addOnBackStackChangedListener(listener);
    }

    public void addOnBackStackChangedListener(android.support.v4.app.FragmentManager.OnBackStackChangedListener listener) {
        getFragmentManagerS().addOnBackStackChangedListener(listener);
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        getFragmentManager().putFragment(bundle, key, fragment);
    }

    public void putFragment(Bundle bundle, String key, android.support.v4.app.Fragment fragment) {
        getFragmentManagerS().putFragment(bundle, key, fragment);
    }

    public void popBackStack(String name, int flags) {
        if (doCompat()) getFragmentManagerS().popBackStack(name, flags);
        else getFragmentManager().popBackStack(name, flags);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        if (doCompat()) return getFragmentManagerS().popBackStackImmediate(id, flags);
        else return getFragmentManager().popBackStackImmediate(id, flags);
    }

    public void popBackStack() {
        if (doCompat()) getFragmentManagerS().popBackStack();
        else getFragmentManager().popBackStack();
    }

    public Fragment findFragmentById(int id) {
        return getFragmentManager().findFragmentById(id);
    }

    public android.support.v4.app.Fragment findFragmentByIdS(int id) {
        return getFragmentManagerS().findFragmentById(id);
    }

    public int getBackStackEntryCount() {
        if (doCompat()) return getFragmentManagerS().getBackStackEntryCount();
        else return getFragmentManager().getBackStackEntryCount();
    }

    public FragmentManager.BackStackEntry getBackStackEntryAt(int index) {
        return getFragmentManager().getBackStackEntryAt(index);
    }

    public android.support.v4.app.FragmentManager.BackStackEntry getBackStackEntryAtS(int index) {
        return getFragmentManagerS().getBackStackEntryAt(index);
    }

    public void invalidateOptionsMenu() {
        if (doCompat())a.invalidateOptionsMenu();
        else getFragmentManager().invalidateOptionsMenu();
    }

    public boolean executePendingTransactions() {
        if (doCompat()) return getFragmentManagerS().executePendingTransactions();
        else return getFragmentManager().executePendingTransactions();
    }

    public boolean popBackStackImmediate() {
        if (doCompat()) return getFragmentManagerS().popBackStackImmediate();
        else return getFragmentManager().popBackStackImmediate();
    }

    @SuppressWarnings("NewApi")
    public boolean isDestroyed() {
        if (doCompat()) return getFragmentManagerS().isDestroyed();
        else return getFragmentManager().isDestroyed();
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
        return getFragmentManager().saveFragmentInstanceState(f);
    }

    public android.support.v4.app.Fragment.SavedState saveFragmentInstanceState(android.support.v4.app.Fragment f) {
        return getFragmentManagerS().saveFragmentInstanceState(f);
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        if (doCompat()) getFragmentManagerS().dump(prefix, fd, writer, args);
        else getFragmentManager().dump(prefix, fd, writer, args);
    }

    public void popBackStack(int id, int flags) {
        if (doCompat()) getFragmentManagerS().popBackStack(id, flags);
        else getFragmentManager().popBackStack(id, flags);
    }

    public Fragment findFragmentByTag(String tag) {
        return getFragmentManager().findFragmentByTag(tag);
    }

    public android.support.v4.app.Fragment findFragmentByTagS(String tag) {
        return getFragmentManagerS().findFragmentByTag(tag);
    }

    public Fragment getFragment(Bundle bundle, String key) {
        return getFragmentManager().getFragment(bundle, key);
    }

    public android.support.v4.app.Fragment getFragmentS(Bundle bundle, String key) {
        return getFragmentManagerS().getFragment(bundle, key);
    }


}
