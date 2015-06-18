package com.z.andframe.binder;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.z.andframe.binder.annotions.ZEvent;
import com.z.andframe.view.interfaces.EventType;
import com.z.andframe.binder.tools.EventListenerStub;
import com.z.andframe.util.ExceptionHandler;
import com.z.andframe.util.Ref;
import com.z.andframe.util.T;
import com.z.andframe.util.ZUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Roy on 15-3-19.
 */
public class ZEventBinder extends ZBaseBinder implements EventType {
    private static SparseArray<String> eventProj1=new SparseArray<>();
    private static HashMap<String,Integer> eventProj2=new HashMap<>();

    static{
        init();
    }

    private static void putProj(String name,int value){
        eventProj1.put(value,name);
        eventProj2.put(name,value);
    }

    private static void init(){
        putProj("ApplyWindowInsets",EVENT_APPLY_WINDOW_INSETS);
        putProj("CreateContextMenu",EVENT_CREATE_CONTEXT_MENU);
        putProj("Click",EVENT_CLICK);
        putProj("Drag",EVENT_DRAG);
        putProj("FocusChange",EVENT_FOCUS_CHANGE);
        putProj("GenericMotion",EVENT_GENERIC_MOTION);
        putProj("Hover",EVENT_HOVER);
        putProj("Key",EVENT_KEY);
        putProj("LongClick",EVENT_LONG_CLICK);
        putProj("SystemUiVisibilityChange",EVENT_SYSTEM_UI_VISIBILITY_CHANGE);
        putProj("Touch",EVENT_TOUCH);
        putProj("ItemClick",EVENT_ITEM_CLICK);
        putProj("ItemLongClick",EVENT_ITEM_LONG_CLICK);
        putProj("ItemSelected",EVENT_ITEM_SELECTED);
        putProj("NothingSelected",EVENT_NOTHING_SELECTED);
        putProj("Scroll",EVENT_SCROLL);
        putProj("ScrollStateChanged",EVENT_SCROLL_STATE_CHANGED);
        putProj("LayoutChange",EVENT_LAYOUT_CHANGE);
        putProj("ViewAttachedToWindow",EVENT_VIEW_ATTACHED_TO_WINDOW);
        putProj("ViewDetachedFromWindow",EVENT_VIEW_DETACHED_FROM_WINDOW);
        putProj("MenuItemActionExpand",EVENT_MENU_ITEM_ACTION_EXPAND);
        putProj("MenuItemActionCollapse",EVENT_MENU_ITEM_ACTION_COLLAPSE);
        putProj("MenuItemClick",EVENT_MENU_ITEM_CLICK);
    }

    public static int getEventId(String name){
        return eventProj2.get(name);
    }

    public static String getEventName(int id){
        return eventProj1.get(id);
    }


    public static void bindEvent(Object holder,EventListenerStub stb){
        Class<?> cls;
        if (holder instanceof Class) cls=(Class<?>)holder;
        else cls=holder.getClass();
        List<Method>ms = Ref.getAllMethods(cls, "java.", "android.", "com.z.andframe.");
        for (Method m:ms){
            if (!m.isAnnotationPresent(ZEvent.class)) continue;
            ZEvent e=m.getAnnotation(ZEvent.class);
            int eventId=e.event();
            T.Tuple2<String,Integer> t=null;
            if (eventId==0){
                t=ZOperators.methodNameOperator.f(m);
                eventId=t.getItem2();
            }
            if (eventId==0) return;
            if ((e.id().length==0&&e.target().length==0&&e.useMethodName()==ZEvent.AUTO)||e.useMethodName()==ZEvent.USE){
                if (t==null) t=ZOperators.methodNameOperator.f(m);
                Object target=bindViewEvent(holder,m,t.getItem1());
                if (target!=null) stb.registerEvent(target,eventId,holder,m);
            }
            for (int id:e.id()){
                Object target=bindViewEvent(holder,m,id);
                if (target!=null) stb.registerEvent(target,eventId,holder,m);
            }
            for (String name:e.target()){
                Object target=bindViewEvent(holder,m,name);
                if (target!=null) stb.registerEvent(target,eventId,holder,m);
            }
        }
    }

    private static Object bindViewEvent(Object holder,Method m,String name) {
        if (TextUtils.isEmpty(name)) return null;
        if (ZBinder.Config.resourceNamePrefix.contains(name.substring(0,1))){
            int id = resources.getIdentifier(name.substring(1), "id", context.getPackageName());
            if (id==0) return null;
            return bindViewEvent(holder, m, id);
        }else{
            try {
                Object obj = Ref.getSuperValue(holder, name);
                if (obj instanceof View) return obj;
                if (obj instanceof MenuItem) return ((MenuItem) obj).getItemId();
                if (obj instanceof Integer) return obj;
            } catch (IllegalAccessException|NoSuchFieldException e1) {
                ExceptionHandler.Throw(e1);
            }
        }
        return null;
    }

    private static Object bindViewEvent(Object holder,Method m,int id) {
        if (id == 0) return null;
        View target = null;
        View v = ZUtil.getContentView(holder);
        if (v != null) {
            target = v.findViewById(id);
        }
        if (target != null) return target;
        else return id;
    }

    public static void injectMenuEvent(Menu menu,EventListenerStub els){
        for (int i=0;i<menu.size();++i)
            els.injectMenuEvent(menu.getItem(i));
    }


}
