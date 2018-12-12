package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by liuyuhang on 2018/7/18.
 */

public class LeListView extends ListView {
    private GestureDetector mGestureDetector;


    public LeListView(Context context) {
        super(context);
        init();
    }

    public LeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    private class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }
}
