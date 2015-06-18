package com.z.andframe.plugin.ormlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Roy on 15-5-20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface ZDataBase {
    public Class<?> table() default Object.class;
    public String where() default "";
    public String raw() default "";
}
