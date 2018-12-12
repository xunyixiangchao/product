package com.ichsy.libs.core.comm.helper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerViewHelper
 * Created by liuyuhang on 2017/5/17.
 */

public class RecyclerViewHelper {

    public static GridLayoutManager createGridLayoutManager(Context context, int column) {
        return new GridLayoutManager(context, column);
    }

    /**
     * 修复在scrollView中滑动异常
     */
    public static void fixInScroll(RecyclerView... recyclerView) {
        for (RecyclerView view : recyclerView) {

            RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
            if (null != layoutManager && layoutManager instanceof GridLayoutManager) {
                ((GridLayoutManager) layoutManager).setSmoothScrollbarEnabled(true);
                layoutManager.setAutoMeasureEnabled(true);
            }

            view.setHasFixedSize(true);
            view.setNestedScrollingEnabled(false);
        }
    }

    /**
     * 给RecyclerView设置adapter
     *
     * @param recyclerView
     * @param layoutManager
     * @param adapter
     */
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 给RecyclerView设置adapter
     *
     * @param recyclerView
     * @param adapter
     */
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }
}
