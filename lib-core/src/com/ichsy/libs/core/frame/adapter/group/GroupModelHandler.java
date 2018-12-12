package com.ichsy.libs.core.frame.adapter.group;

/**
 * 用来操作adapter的方法
 * Created by liuyuhang on 2018/8/30.
 */

public interface GroupModelHandler {

    interface AdapterHandler {

        /**
         * 重绘adapter
         */
        void adapterNotifyDataSetChanged();
    }

    void onAdapterHandlerCallback(AdapterHandler handler);
}
