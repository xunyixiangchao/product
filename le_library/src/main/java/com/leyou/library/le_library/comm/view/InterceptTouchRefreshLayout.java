package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dalong.refreshlayout.RefreshLayout;
import com.leyou.library.le_library.comm.view.refreshheader.RefreshLinkedFooter;

/**
 * 下拉刷新
 * 修复下拉刷新只滑动一点和轮播冲突
 */
public class InterceptTouchRefreshLayout extends RefreshLayout {

    public InterceptTouchRefreshLayout(Context context) {
        super(context);
    }

    public InterceptTouchRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        InterceptTouchRefreshHeader header = new InterceptTouchRefreshHeader(getContext());
        addHeader(header);
        setOnHeaderListener(header);
        // 不用也不能为空 没发现有关闭加载更多的方法
        RefreshLinkedFooter foot = new RefreshLinkedFooter(getContext());
        addFooter(foot);
        setOnFooterListener(foot);
        setCanLoad(false);
    }


    float lastX = 0;
    float lastY = 0;
    boolean isMove = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 解决和轮播冲突
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = ev.getX();
            lastY = ev.getY();
            isMove = false;
            return super.onInterceptTouchEvent(ev);
        }

        int x2 = (int) Math.abs(ev.getX() - lastX);
        int y2 = (int) Math.abs(ev.getY() - lastY);

        final int move = 100;
        //滑动图片最小距离检查
        if (x2 > y2) {
            if (x2 >= move) {
                isMove = true;
            }
            return false;
        }

        //是否移动图片(下拉刷新不处理)
        return !isMove && super.onInterceptTouchEvent(ev);
    }
}
