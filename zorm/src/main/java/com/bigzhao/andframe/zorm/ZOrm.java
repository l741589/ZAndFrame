package com.bigzhao.andframe.zorm;

import android.os.CancellationSignal;

/**
 * Created by Roy on 15-7-25.
 */
public @interface ZOrm {
    String database() default "zaf_aa_default.db";
    String table();
    String[] projection() default {};
    String selection() default "";
    String[] selectionArgs() default {};
    String sortOrder() default "";

    boolean distinct() default false;
    String groupBy() default "";
    String having() default "";
    String limit() default "";
    String raw() default "";
}
