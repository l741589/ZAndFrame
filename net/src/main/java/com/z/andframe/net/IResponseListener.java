package com.z.andframe.net;

import com.z.andframe.net.request.Request;

/**
 * Created by Roy on 15-3-25.
 */
public interface IResponseListener {
    void onSuccess(Request<?> req, Object res);
    void onError(Request<?> req, Exception e);
}
