package com.bigzhao.andframe.binder.annotions;

import com.bigzhao.andframe.view.interfaces.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Roy on 15-3-19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ZEvent {
    public int event() default EventType.EVENT_AUTO;
    public int[] id() default {};

    public String[] target() default {};
    public int useMethodName() default 0;

    public static final int AUTO = 0;
    public static final int USE = 1;
    public static final int NOTUSE = 2;

}
