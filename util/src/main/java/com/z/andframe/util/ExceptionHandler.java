package com.z.andframe.util;

import java.util.Objects;

/**
 * Created by Roy on 15-3-18.
 */
public class ExceptionHandler {
    public static boolean ignoreHandledException = false;

    public static class HandledException extends RuntimeException{
        public HandledException(Throwable t){
            super(t);
        }
    }

    public static void Throw(Throwable t) {
        if (!ignoreHandledException){
            throw new HandledException(t);
        }else{
            t.printStackTrace();
        }
    }

    public static void Hide(Throwable t){
        t.printStackTrace();;
    }

    public static void ThrowNoIgnore(Throwable t) {
        throw new HandledException(t);
    }

}
