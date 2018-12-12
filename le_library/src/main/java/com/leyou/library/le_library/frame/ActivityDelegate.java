package com.leyou.library.le_library.frame;

/**
 * Created by liuyuhang on 2018/7/30.
 */
public class ActivityDelegate {

    public static ActivityDelegate getDefaultDelegate() {
        return new ActivityDelegate();
    }


    public void addObserver(DataObserver observer) {
    }
}
