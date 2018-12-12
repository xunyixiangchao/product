package com.leyou.library.le_library.comm.handler;

/**
 * 用户状态相关接口
 * Created by liuyuhang on 2016/12/15.
 */

public interface UserLoginStatusHandler {

    /**
     * 当用户登录的时候做的操作，一般是刷新UI
     */
    void onUserLogin();

}
