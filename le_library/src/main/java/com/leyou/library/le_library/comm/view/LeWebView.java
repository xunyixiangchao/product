package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 自定义的webview
 * Created by liuyuhang on 2018/7/18.
 */

public class LeWebView extends WebView {
    /**
     * Constructs a new WebView with a Context object.
     *
     * @param context a Context object used to access application assets
     */
    public LeWebView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs   an AttributeSet passed to our parent
     */
    public LeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructs a new WebView with layout parameters and a default style.
     *
     * @param context      a Context object used to access application assets
     * @param attrs        an AttributeSet passed to our parent
     * @param defStyleAttr an attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     */
    public LeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LeWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }
    public void init() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
