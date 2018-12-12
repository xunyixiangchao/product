package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 解决7.0版本不全屏问题
 * Created by liuyuhang on 2018/9/26.
 */

public class LePopupWindow extends PopupWindow {

    public LePopupWindow(Context context) {
        super(context);
    }

    public LePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public LePopupWindow(View contentView) {
        super(contentView);
    }

    public LePopupWindow(int width, int height) {
        super(width, height);
    }

    public LePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    /**
     * <p>Create a new popup window which can display the <tt>contentView</tt>.
     * The dimension of the window must be passed to this constructor.</p>
     * <p>
     * <p>The popup does not provide any background. This should be handled
     * by the content view.</p>
     *
     * @param contentView the popup's content
     * @param width       the popup's width
     * @param height      the popup's height
     * @param focusable   true if the popup can be focused, false otherwise
     */
    public LePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    /**
     * 在android7.0上，如果不主动约束PopuWindow的大小，比如，设置布局大小为 MATCH_PARENT,那么PopuWindow会变得尽可能大，
     * 以至于 view下方无空间完全显示PopuWindow，而且view又无法向上滚动，此时PopuWindow会主动上移位置，直到可以显示完全。
     * 　解决办法：主动约束PopuWindow的内容大小，重写showAsDropDown方法：
     *
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {

        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

}
