package com.leyou.library.le_library.ui;

/**
 * 处理登陆之后事件的handler
 * Created by liuyuhang on 2017/4/6.
 */

public interface LoginEventHandler {

    /**
     * 登陆之后跳转到这里，会执行的方法
     *
     * @param event
     */
    void afterLoginEvent(String event);
}
