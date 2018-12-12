package com.leyou.library.le_library.model;

/**
 * event事件
 * Created by liuyuhang on 2017/3/9.
 */

public class EventObject {
    public String event;
    public Object params;

    public EventObject(String event) {
        this.event = event;
    }
}
