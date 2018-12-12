package com.ichsy.libs.core.frame.adapter.recycler;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Recycler绘制者
 * <p>
 * Created by liuyuhang on 2018/9/4.
 */

public interface RecyclerViewDrawer<T> {
    /**
     * 在adapter的getView中，首次创建view，初始化并返回创建的布局
     *
     * @param viewType
     * @param inflater
     * @param parent
     * @return
     */
    View onViewCreate(int viewType, @NonNull LayoutInflater inflater, ViewGroup parent);

    /**
     * 在adapter的getView中，每次滑动listView会循环执行本方法
     *
     * @param position
     * @param item
     * @param viewHolder
     */
    void onViewAttach(int position, @NonNull T item, @NonNull BaseRecyclerViewHolder viewHolder);
}
