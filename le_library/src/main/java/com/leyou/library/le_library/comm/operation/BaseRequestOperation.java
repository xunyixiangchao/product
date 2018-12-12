package com.leyou.library.le_library.comm.operation;

/**
 * Created by liuyuhang on 16/6/13.
 */
public class BaseRequestOperation {

    public interface OperationListener<T> {
        void onCallBack(T t);
    }
}
