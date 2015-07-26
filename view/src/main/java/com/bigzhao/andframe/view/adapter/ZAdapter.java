package com.bigzhao.andframe.view.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bigzhao.andframe.binder.ZBinder;
import com.bigzhao.andframe.binder.ZInjector;
import com.bigzhao.andframe.binder.annotions.ZBind;
import com.bigzhao.andframe.view.interfaces.IViewHolder;
import com.bigzhao.andframe.binder.tools.EventListenerStub;
import com.bigzhao.andframe.util.ExceptionHandler;
import com.bigzhao.andframe.view.R;
import com.bigzhao.andframe.view.interfaces.IZBaseViewHolder;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Roy on 15-3-29.
 */
public class ZAdapter extends BaseAdapter{

    private List<Object> data;
    private Object viewHolder;
    private Class<? extends IZBaseViewHolder> viewHolderType;
    private HashMap<Class<?>,Integer> itemViewTypes;
    private Context context;
    private EventListenerStub eventListenerStub=new EventListenerStub();
    private SparseArray<WeakReference<View>> views=new SparseArray<>();

    @Override
    public int getCount() {
        return data.size();
    }

    public ZAdapter(Context context,Class<? extends IZBaseViewHolder> viewHolderType,Object data){
        try {
            this.context=context;
            this.viewHolderType=viewHolderType;
            if (ZMultiViewHolder.class.isAssignableFrom(viewHolderType))
                this.viewHolder=viewHolderType.newInstance();
            else this.viewHolder=null;
            this.data=new ArrayList<>();
            if (data instanceof Iterable){
                for (Object o:(Iterable<?>)data){
                    this.data.add(o);
                }
            }else if (data.getClass().isArray()){
                for (int i=0;i< Array.getLength(data);++i){
                    this.data.add(Array.get(data,i));
                }
            }
        } catch (InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            Class<? extends IZBaseViewHolder> vhType;
            if (viewHolder!=null) vhType=((ZMultiViewHolder)viewHolder).getItemViewHolder(position);
            else vhType=viewHolderType;
            try{
                IZBaseViewHolder vh=vhType.newInstance();
                convertView=vh.onPrepare(context);
                convertView.setTag(R.id.tag_viewholder, vh);
                if (vh instanceof ZBaseViewHolder.ICanBeDisabled)
                    views.put(position, new WeakReference<>(convertView));
            } catch (InstantiationException|IllegalAccessException e) {
                ExceptionHandler.ThrowNoIgnore(e);
            }
        }
        if (convertView!=null) {
            IZBaseViewHolder vh = (IZBaseViewHolder)convertView.getTag(R.id.tag_viewholder);
            vh.onUpdate(data.get(position),position);
            vh.onLoaded();
        }
        return convertView;
    }



    @Override
    public int getItemViewType(int position) {
        if (viewHolder!=null) {
            Class<?> cls=((ZMultiViewHolder)viewHolder).getItemViewHolder(position);
            if (itemViewTypes==null) itemViewTypes=new HashMap<>();
            Integer i=itemViewTypes.get(cls);
            if (i==null) {
                i=itemViewTypes.size();
                itemViewTypes.put(cls,i);
            }
            return i;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        if (viewHolder!=null) return ((ZMultiViewHolder)viewHolder).getItemViewHolderCount();
        return 1;
    }

    @Override
    public boolean isEnabled(int position) {
        WeakReference<View> rv=views.get(position);
        if (rv==null||rv.get()==null) return true;
        View v=rv.get();
        Object vh = v.getTag(R.id.tag_viewholder);
        return vh == null || !(vh instanceof ZBaseViewHolder.ICanBeDisabled) || ((ZBaseViewHolder.ICanBeDisabled) vh).isEnabled(position);
    }
}
