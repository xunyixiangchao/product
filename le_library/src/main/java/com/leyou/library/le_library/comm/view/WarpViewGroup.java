package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import library.liuyh.com.lelibrary.R;

/**
 * 根据子控件宽度自动换行
 * <p>
 * Created by ss on 2017/7/25.
 */
public class WarpViewGroup extends ViewGroup {

    private Type mType;
    private List<WarpLine> mWarpLineGroup;

    public WarpViewGroup(Context context) {
        this(context, null);
    }

    public WarpViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.WarpLinearLayoutDefault);
    }

    public WarpViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mType = new Type(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        int with = 0;
        int height = 0;
        //在调用childView。getMeasure之前必须先调用该行代码，用于对子View大小的测量
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //计算宽度
        switch (withMode) {
            case MeasureSpec.EXACTLY:
                with = withSize;
                break;
            case MeasureSpec.AT_MOST:
                for (int i = 0; i < childCount; i++) {
                    if (i != 0) {
                        with += mType.horizontal_space;
                    }
                    with += getChildAt(i).getMeasuredWidth();
                }
                with += getPaddingLeft() + getPaddingRight();
                with = with > withSize ? withSize : with;
                break;
            case MeasureSpec.UNSPECIFIED:
                for (int i = 0; i < childCount; i++) {
                    if (i != 0) {
                        with += mType.horizontal_space;
                    }
                    with += getChildAt(i).getMeasuredWidth();
                }
                with += getPaddingLeft() + getPaddingRight();
                break;
            default:
                with = withSize;
                break;
        }

        // 根据计算出的宽度，计算出所需要的行数
        WarpLine warpLine = new WarpLine();
        // 不能够在定义属性时初始化，因为onMeasure方法会多次调用
        mWarpLineGroup = new ArrayList();

        for (int i = 0; i < childCount; i++) {
            if (warpLine.lineWidth + getChildAt(i).getMeasuredWidth() + mType.horizontal_space > with) {
                if (warpLine.lineView.size() == 0) {
                    warpLine.addView(getChildAt(i));
                    mWarpLineGroup.add(warpLine);
                    warpLine = new WarpLine();
                } else {
                    mWarpLineGroup.add(warpLine);
                    warpLine = new WarpLine();
                    warpLine.addView(getChildAt(i));
                }
            } else {
                warpLine.addView(getChildAt(i));
            }
        }
        // 添加最后一行
        if (warpLine.lineView.size() > 0 && !mWarpLineGroup.contains(warpLine)) {
            mWarpLineGroup.add(warpLine);
        }

        // 计算高度
        height = getPaddingTop() + getPaddingBottom();
        int showLine = getShowLine();
        for (int i = 0; i < showLine; i++) {
            if (i != 0) {
                height += mType.vertical_space;
            }
            height += mWarpLineGroup.get(i).height;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = height > heightSize ? heightSize : height;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        setMeasuredDimension(with, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        t = getPaddingTop();
        int showLine = getShowLine();
        for (int i = 0; i < showLine; i++) {
            int left = getPaddingLeft();
            WarpLine warpLine = mWarpLineGroup.get(i);
            int lastWidth = getMeasuredWidth() - warpLine.lineWidth;
            for (int j = 0; j < warpLine.lineView.size(); j++) {
                View view = warpLine.lineView.get(j);
                if (isFull()) {//需要充满当前行时
                    view.layout(left, t, left + view.getMeasuredWidth() + lastWidth / warpLine.lineView.size(), t + view.getMeasuredHeight());
                    left += view.getMeasuredWidth() + mType.horizontal_space + lastWidth / warpLine.lineView.size();
                } else {
                    switch (getGravity()) {
                        case Gravity.RIGHT://右对齐
                            view.layout(left + lastWidth, t, left + lastWidth + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                        case Gravity.CENTER://居中对齐
                            view.layout(left + lastWidth / 2, t, left + lastWidth / 2 + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                        default://左对齐
                            view.layout(left, t, left + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                    }
                    left += view.getMeasuredWidth() + mType.horizontal_space;
                }
            }
            t += warpLine.height + mType.vertical_space;
        }
    }

    private int getGravity() {
        return mType.on_gravity;
    }

    private boolean isFull() {
        return mType.is_full;
    }

    public int getShowSize() {
        int size = 0;
        int showLine = getShowLine();
        for (int i = 0; i < showLine; i++) {
            WarpLine warpLine = mWarpLineGroup.get(i);
            size += warpLine.lineView.size();
        }
        return size;
    }

    private int getShowLine() {
        int totalLine = mWarpLineGroup.size();
        int showLine = mType.max_line;
        return showLine <= 0 ? totalLine : (showLine > totalLine ? totalLine : showLine);
    }

    /**
     * 对样式的初始化
     */
    private final static class Type {
        // 对齐方式 right 0，left 1，center 2
        private int on_gravity;
        // 水平间距,单位px
        private float horizontal_space;
        // 垂直间距,单位px
        private float vertical_space;
        // 是否自动填满
        private boolean is_full;
        // 最大行数
        private int max_line;

        Type(Context context, AttributeSet attrs) {
            if (attrs == null) {
                return;
            }
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WarpLinearLayout);
            on_gravity = typedArray.getInt(R.styleable.WarpLinearLayout_on_gravity, on_gravity);
            horizontal_space = typedArray.getDimension(R.styleable.WarpLinearLayout_horizontal_space, horizontal_space);
            vertical_space = typedArray.getDimension(R.styleable.WarpLinearLayout_vertical_space, vertical_space);
            is_full = typedArray.getBoolean(R.styleable.WarpLinearLayout_is_full, is_full);
            max_line = typedArray.getInt(R.styleable.WarpLinearLayout_max_line, max_line);
        }
    }

    /**
     * 每行子View的对齐方式
     */
    public final static class Gravity {
        public final static int RIGHT = 0;
        public final static int LEFT = 1;
        public final static int CENTER = 2;
    }

    /**
     * 用于存放一行子View
     */
    private final class WarpLine {
        private List<View> lineView = new ArrayList<>();
        /**
         * 当前行中所需要占用的宽度
         */
        private int lineWidth = getPaddingLeft() + getPaddingRight();
        /**
         * 该行View中所需要占用的最大高度
         */
        private int height = 0;

        private void addView(View view) {
            if (lineView.size() != 0) {
                lineWidth += mType.horizontal_space;
            }
            height = height > view.getMeasuredHeight() ? height : view.getMeasuredHeight();
            lineWidth += view.getMeasuredWidth();
            lineView.add(view);
        }
    }
}
