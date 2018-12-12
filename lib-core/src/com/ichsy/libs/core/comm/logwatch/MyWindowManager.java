package com.ichsy.libs.core.comm.logwatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.logwatch.ui.LogWatcherActivity;
import com.ichsy.libs.core.comm.logwatch.view.FloatContentView;
import com.ichsy.libs.core.comm.logwatch.view.FloatView;

public class MyWindowManager {

    private static MyWindowManager manager;
    private static WindowManager winManager;
    private static Context context;
    private LayoutParams paramsView;
    private LayoutParams paramsContentView;
    private FloatView floatView;
    private FloatContentView floatContentView;
    private static int displayWidth;
    private static int displayHeight;
    private String moveViewContent;

    private boolean isActivityOpen = false;

    @SuppressWarnings("deprecation")
    public static synchronized MyWindowManager getInstance(Context context) {
        if (manager == null) {
            MyWindowManager.context = context;
            winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            displayWidth = winManager.getDefaultDisplay().getWidth();
            displayHeight = winManager.getDefaultDisplay().getHeight();
            manager = new MyWindowManager();
        }
        return manager;
    }

    /**
     * 显示悬浮框
     */
    public void show(String text) {
        moveViewContent = text;
        floatView = getView();
        if (floatView.getParent() != null) {
            winManager.removeView(floatView);
        }
        winManager.addView(floatView, paramsView);
    }

    /**
     * 显示悬浮框
     */
    public void show() {
        floatView = getView();
        if (floatView.getParent() != null) {
            winManager.removeView(floatView);
        }
        winManager.addView(floatView, paramsView);
    }

    public void dismissFloat() {
        winManager.removeView(floatView);
        floatContentView = null;
    }


    /**
     * 点击悬浮框后的显示详细信息
     */
    public void showContent() {
        View floatContentView = getContentView();
        if (floatContentView.getParent() != null) {
            winManager.removeView(floatContentView);
        }
//		winManager.addView(floatContentView, paramsContentView);
//		if (floatView != null) {
//			winManager.removeView(floatView);
//			floatView = null;
//		}

        if (!isActivityOpen) {
            Intent intent = new Intent(context, LogWatcherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
        isActivityOpen = !isActivityOpen;
    }

    // 移动悬浮框
    public void move(View view, int delatX, int deltaY) {
        if (view == floatView) {
            paramsView.x += delatX;
            paramsView.y += deltaY;
            winManager.updateViewLayout(view, paramsView);
        }
    }

    // 移除内容悬浮框
    public void dismiss() {
        winManager.removeView(floatContentView);
        floatContentView = null;
    }

    public void back() {
        winManager.addView(getView(), paramsView);
        winManager.removeView(floatContentView);
        floatContentView = null;
    }

    public boolean isShow() {
        if (floatView != null) {
            return floatView.getParent() != null;
        } else {
            return false;
        }
    }

    public boolean isUpdate() {
        return floatContentView != null;
    }

    /**
     * 显示默认悬浮框
     *
     * @return
     */
    public FloatView getView() {
        if (floatView == null) {
            floatView = new FloatView(context);
            TextView mTextViewMove = (TextView) floatView.findViewById(R.id.move);
            if (!TextUtils.isEmpty(moveViewContent)) {
                mTextViewMove.setText(moveViewContent);
            }
        }
        if (paramsView == null) {
            paramsView = new LayoutParams();
            paramsView.type = LayoutParams.TYPE_PHONE;// 悬浮框的类型
            paramsView.format = PixelFormat.RGBA_8888;
            paramsView.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;// 悬浮框的行为
            paramsView.gravity = Gravity.LEFT | Gravity.TOP;// 悬浮框的对齐方式
            paramsView.width = floatView.mWidth;// 悬浮框的宽度
            paramsView.height = floatView.mHeight;// 悬浮框的高度
            paramsView.x = displayWidth - floatView.mWidth;// 横向位置
            paramsView.y = displayHeight / 2;// 纵向位置
        }
        return floatView;
    }

    /**
     * 显示内容的悬浮框
     *
     * @return
     */
    public View getContentView() {
        if (floatContentView == null) {
            floatContentView = new FloatContentView(context);
        }
        if (paramsContentView == null) {
            paramsContentView = new LayoutParams();
            paramsContentView.type = LayoutParams.TYPE_PHONE;
            paramsContentView.format = PixelFormat.RGBA_8888;
            paramsContentView.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
            paramsContentView.gravity = Gravity.LEFT | Gravity.TOP;
            paramsContentView.width = displayWidth / 8 * 7;
            paramsContentView.height = displayHeight / 8 * 7;
            paramsContentView.x = (displayWidth - (displayWidth / 8 * 7)) / 2;
            paramsContentView.y = (displayHeight - (displayHeight / 8 * 7)) / 2 - 50;
        }
        return floatContentView;
    }

}
