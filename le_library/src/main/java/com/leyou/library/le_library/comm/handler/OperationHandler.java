package com.leyou.library.le_library.comm.handler;

/**
 * Operation回调
 * Created by liuyuhang on 16/9/21.
 */

public interface OperationHandler<T> {

    void onCallback(T result);

    void onFailed(int code, String message);
}
