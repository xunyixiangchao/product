package com.ichsy.libs.core.comm.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * 对于View操作的工具类
 *
 * @author LiuYuHang Date: 2015年3月25日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.public_libs.util
 * @File ViewUtil.java
 */
public class ViewUtil {

    /**
     * 单位换算
     *
     * @param context
     * @param dipValue
     * @return
     * @author LiuYuHang
     * @date 2014年10月23日
     */
    public static int dip2px(Context context, float dipValue) {
        if (context == null)
            return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        totalHeight += (listView.getDividerHeight() * (listAdapter.getCount() - 1)) +
                listView.getPaddingBottom() + listView.getPaddingTop();

        return totalHeight;
    }

    /**
     * 单位换算
     *
     * @param context
     * @param pxValue
     * @return
     * @author LiuYuHang
     * @date 2014年10月23日
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 批量设置View的显示和隐藏
     *
     * @param visibility Modifier： Modified Date： Modify：
     */
    public static void setViewVisibility(int visibility, View... views) {
        setViewVisibility(visibility, null, views);
    }

    /**
     * 批量设置View的显示和隐藏
     *
     * @param visibility
     * @param animation  使用动画
     * @param views
     */
    public static void setViewVisibility(final int visibility, final Animation animation, View... views) {
        if (views == null || views.length == 0)
            return;
        for (final View v : views) {
            if (null == v || v.getVisibility() == visibility) {
                continue;
            }

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (animation == null) {
                        v.setVisibility(visibility);
                    } else {
                        v.setVisibility(visibility);
                        v.startAnimation(animation);

                    }
                }
            };

            ThreadPoolUtil.runOnMainThread(runnable);
        }
    }

    public static void swapView(View from, View to) {
        setViewVisibility(View.VISIBLE, to);
        setViewVisibility(View.GONE, from);
    }

    /**
     * edittext和button做绑定
     *
     * @param editText
     * @param btn
     */
    public static void bindButton(EditText editText, final View btn) {
        btn.setEnabled(getText(editText).length() > 0);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn.setEnabled(s.length() > 0);
            }
        });
    }

    /**
     * 从editText中获取里面的文本内容
     *
     * @param editText
     * @return
     */
    public static
    @NonNull
    String getText(EditText editText) {
        if (ObjectUtils.isNull(editText) || ObjectUtils.isNull(editText.getText())) return "";
        return editText.getText().toString().trim();
    }

    /**
     * 检查editText内容是否为空，如果为空，提示信息，
     *
     * @param editText
     * @param emptyMessage 如果内容为空，弹出的提示
     * @return true:检测通过，不为空，false:内容为空
     */
    public static String checkEditText(EditText editText, String emptyMessage) {
        String text;
        if (TextUtils.isEmpty(text = getText(editText))) {
            ToastUtils.showMessage(editText.getContext(), emptyMessage);
        }
        return text;
    }

    /**
     * view设置到group中
     *
     * @param viewGroup
     * @param child
     */
    public static void setViewToGroup(ViewGroup viewGroup, View child) {
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        viewGroup.addView(child);
    }

//    public void resetLayoutByDeviceWidth(Context context, View view) {
//        view.getLayoutDirection()
//
//    }


    public interface ViewMovedListener {
        void moveUp();

        void moveDown();
    }

    /**
     * 监听listview的上下滑动
     *
     * @param touchView
     * @param movedListener
     */
    public static void setOnScrollerListener(final ListView touchView, final ViewMovedListener movedListener) {
        touchView.setOnTouchListener(new OnTouchListener() {

            int height = 50;
            float offsetUp = height * 1.0f;
            float offsetDown = 10.0f;
            float oY = 0;
            float nY = 0;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (oY == 0) {
                            oY = event.getRawY();
                        }
                        nY = event.getRawY();
                        if ((nY - oY) <= -offsetUp
                            // && pagerCanScroll
                                ) {
                            if (touchView.getFirstVisiblePosition() > 0) {
                                movedListener.moveUp();
                            }
                        }
                        if ((nY - oY) >= offsetDown) {
                            movedListener.moveDown();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // oY = event.getRawY();
                        oY = 0;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获得到ListView滚动的距离
     *
     * @return
     */
    public static int getScrollY(ListView listView) {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }


    // 状态栏高度
    private static int statusBarHeight = 0;
    // 屏幕像素点
    private static final Point screenSize = new Point();

    // 获取屏幕像素点
    public static Point getScreenSize(Activity context) {

        if (context == null) {
            return screenSize;
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            Display diplay = wm.getDefaultDisplay();
            if (diplay != null) {
                diplay.getMetrics(mDisplayMetrics);
                int W = mDisplayMetrics.widthPixels;
                int H = mDisplayMetrics.heightPixels;
                if (W * H > 0 && (W > screenSize.x || H > screenSize.y)) {
                    screenSize.set(W, H);
                }
            }
        }
        return screenSize;
    }

    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight <= 0) {
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        if (statusBarHeight <= 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
