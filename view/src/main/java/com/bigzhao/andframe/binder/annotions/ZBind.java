package com.bigzhao.andframe.binder.annotions;

import com.bigzhao.andframe.view.interfaces.IViewHolder;
import com.bigzhao.andframe.view.adapter.ZAdapter;
import com.bigzhao.andframe.view.adapter.ZBaseViewHolder;
import com.bigzhao.andframe.view.interfaces.IZBaseViewHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Roy on 15-3-18.
 * Id优先级高于name,高于名字
 * MenuId优先级高于menu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface ZBind {

    public int id() default 0;
    public String name() default "";

    /** name[.converter][:property][name[.converter][:property]]... */
    public String value() default "";

    public int menuId() default 0;
    public String menu() default "";
    public Class<? extends IZBaseViewHolder> viewHolder() default ZBaseViewHolder.class;
    public Class<? extends ZAdapter> adapter() default ZAdapter.class;

}
