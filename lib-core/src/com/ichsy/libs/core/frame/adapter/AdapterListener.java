package com.ichsy.libs.core.frame.adapter;

/**
 * 监听adapter的各个流程
 *
 * @author liuyuhang
 * @date 16/5/11
 */
public interface AdapterListener {

    /**
     * 准备给adapter绑定数据
     */
    void onBeginBindData();

    /**
     * adapter已经绑定数据
     *
     * @param empty 数据是否为空
     */
    void onDataBindComplete(boolean empty);
}
