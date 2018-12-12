package com.leyou.library.le_library.comm.view.refreshheader;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dalong.refreshlayout.RefreshLayout;
import com.ichsy.libs.core.comm.utils.LogUtils;

/**
 * 下拉刷新
 * Created by zhouweilong on 2016/10/25.
 *
 * @author: guanbr
 * @date: 2018/1/3
 * 修复下拉刷新只滑动一点和轮播冲突
 */
public class RefreshLinkedLayout extends RefreshLayout {
    public RefreshLinkedLayout(Context context) {
        super(context);
    }

    public RefreshLinkedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        RefreshLinkedHeader header = new RefreshLinkedHeader(getContext());
        RefreshLinkedFooter footer = new RefreshLinkedFooter(getContext());

        addHeader(header);
        addFooter(footer);
        setOnHeaderListener(header);
        setOnFooterListener(footer);
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
