package com.bigzhao.andframe.binder.tools;

import android.os.Build;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.bigzhao.andframe.view.interfaces.EventType;
import com.bigzhao.andframe.util.ExceptionHandler;
import com.bigzhao.andframe.util.T;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* Created by Roy on 15-3-19.
*/
public class EventListenerStub implements
        EventType,
        View.OnAttachStateChangeListener,
        View.OnClickListener,
        View.OnDragListener,
        View.OnFocusChangeListener,
        View.OnGenericMotionListener,
        View.OnHoverListener,
        View.OnKeyListener,
        View.OnLayoutChangeListener,
        View.OnLongClickListener,
        View.OnTouchListener,
        View.OnCreateContextMenuListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemSelectedListener,
        AbsListView.OnScrollListener,
        MenuItem.OnActionExpandListener,
        MenuItem.OnMenuItemClickListener
        //NumberPicker.OnScrollListener
{
    private T.Map3<Object, Integer, T.Tuple2<Object,Method>> eventCall=new T.Map3<>();

    private class OnSystemUiVisibilityChangeListenerStub implements View.OnSystemUiVisibilityChangeListener{

        private View v;

        public OnSystemUiVisibilityChangeListenerStub(View v){
            this.v=v;
        }

        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            call(v, EVENT_SYSTEM_UI_VISIBILITY_CHANGE, visibility);
        }
    }

    @SuppressWarnings("NewApi")
    private class OnApplyWindowInsetsListenerStub implements View.OnApplyWindowInsetsListener{

        @Override
        public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
            return (WindowInsets)call(v,EVENT_APPLY_WINDOW_INSETS,v,insets);
        }
    }

    public void registerEvent(Object v,int eventId,Object target,Method m){
        if (eventId!=0&&v!=null) {
            m.setAccessible(true);
            switch(eventId){
                case EVENT_AUTO:break;
                case EVENT_APPLY_WINDOW_INSETS: if (Build.VERSION.SDK_INT>=20) ((View)v).setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListenerStub());break;
                case EVENT_CREATE_CONTEXT_MENU: ((View)v).setOnCreateContextMenuListener(this);break;
                case EVENT_CLICK: ((View)v).setOnClickListener(this);break;
                case EVENT_DRAG: ((View)v).setOnDragListener(this);break;
                case EVENT_FOCUS_CHANGE: ((View)v).setOnFocusChangeListener(this);break;
                case EVENT_GENERIC_MOTION: ((View)v).setOnGenericMotionListener(this);break;
                case EVENT_HOVER: ((View)v).setOnHoverListener(this);break;
                case EVENT_KEY: ((View)v).setOnKeyListener(this);break;
                case EVENT_LONG_CLICK: ((View)v).setOnLongClickListener(this);break;
                case EVENT_SYSTEM_UI_VISIBILITY_CHANGE: ((View)v).setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListenerStub((View)v));break;
                case EVENT_TOUCH: ((View)v).setOnTouchListener(this);break;
                case EVENT_ITEM_CLICK: ((AdapterView)v).setOnItemClickListener(this);break;
                case EVENT_ITEM_LONG_CLICK: ((AdapterView)v).setOnItemLongClickListener(this);break;
                case EVENT_ITEM_SELECTED:
                case EVENT_NOTHING_SELECTED: ((AdapterView)v).setOnItemSelectedListener(this);break;
                case EVENT_SCROLL:
                case EVENT_SCROLL_STATE_CHANGED:  ((AbsListView)v).setOnScrollListener(this);break;
                case EVENT_LAYOUT_CHANGE: ((View)v).addOnLayoutChangeListener(this);break;
                case EVENT_VIEW_ATTACHED_TO_WINDOW:
                case EVENT_VIEW_DETACHED_FROM_WINDOW:  ((View)v).addOnAttachStateChangeListener(this);break;
                case EVENT_MENU_ITEM_ACTION_EXPAND:
                case EVENT_MENU_ITEM_ACTION_COLLAPSE://((MenuItem)v).setOnActionExpandListener(this);break;
                case EVENT_MENU_ITEM_CLICK: //((MenuItem)v).setOnMenuItemClickListener(this);break;
            }
            eventCall.put(v, eventId, new T.Tuple2<>(target, m));
        }
    }

    public Object call(Object v,int eventId,Object... args){
        T.Tuple2<Object,Method> t=eventCall.get(v,eventId);
        if (t==null) return null;
        try {
            return t.getItem2().invoke(t.getItem1(),args);
        } catch (IllegalAccessException|InvocationTargetException e) {
            ExceptionHandler.Throw(e);
        }
        return null;
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        call(v,EVENT_VIEW_ATTACHED_TO_WINDOW,v);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        call(v,EVENT_VIEW_DETACHED_FROM_WINDOW,v);
    }

    @Override
    public void onClick(View v) {
        call(v,EVENT_CLICK,v);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        call(v,EVENT_CREATE_CONTEXT_MENU,menu,v,menuInfo);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return (Boolean)call(v,EVENT_DRAG,v,event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        call(v,EVENT_FOCUS_CHANGE,v,hasFocus);
    }

    @Override
    public boolean onGenericMotion(View v, MotionEvent event) {
        return (Boolean)call(v,EVENT_GENERIC_MOTION,v,event);
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        return (Boolean)call(v,EVENT_HOVER,v,event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return (Boolean)call(v,EVENT_KEY,v,keyCode,event);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        call(v,EVENT_LAYOUT_CHANGE,v,left,top,right,bottom,oldLeft,oldTop,oldRight,oldBottom);
    }

    @Override
    public boolean onLongClick(View v) {
        return (Boolean)call(v,EVENT_LONG_CLICK,v);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return (Boolean)call(v,EVENT_TOUCH,v,event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        call(parent,EVENT_ITEM_CLICK,parent,view,position,id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return (Boolean)call(parent,EVENT_ITEM_LONG_CLICK,parent,view,position,id);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        call(parent,EVENT_ITEM_SELECTED,parent,view,position,id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        call(parent,EVENT_NOTHING_SELECTED,parent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        call(view,EVENT_SCROLL_STATE_CHANGED,view,scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        call(view,EVENT_SCROLL,view,firstVisibleItem,visibleItemCount,totalItemCount);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return (Boolean)call(item,EVENT_MENU_ITEM_ACTION_EXPAND,item);
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return (Boolean)call(item,EVENT_MENU_ITEM_ACTION_COLLAPSE,item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return (Boolean)call(item.getItemId(),EVENT_MENU_ITEM_CLICK,item);
    }


    /*@Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        call(EVENT_SCROLL_STATE_CHANGED,view,scrollState);
    }*/


    public void injectMenuEvent(MenuItem item){
        T.Tuple2<Object,Method> t=eventCall.get(item,EVENT_MENU_ITEM_ACTION_EXPAND);
        if (t!=null) item.setOnActionExpandListener(this);
        else{
            t=eventCall.get(item.getItemId(),EVENT_MENU_ITEM_ACTION_COLLAPSE);
            if (t!=null) item.setOnActionExpandListener(this);
        }
        t=eventCall.get(item.getItemId(),EVENT_MENU_ITEM_CLICK);
        if (t!=null) item.setOnMenuItemClickListener(this);
        if (item instanceof Menu){
            Menu menu=(Menu)item;
            for (int i=0;i<menu.size();++i) injectMenuEvent(menu.getItem(i));
        }
    }
}
