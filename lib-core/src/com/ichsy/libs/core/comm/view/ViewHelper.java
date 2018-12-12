package com.ichsy.libs.core.comm.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ichsy.libs.core.comm.utils.ViewUtil;

/**
 * 处理view的框架类
 */
public class ViewHelper {

    private Context mContext;
    private View[] mViews;

    public ViewHelper(Context context) {
        mContext = context;
    }

    public static ViewHelper get(Context context) {
//        synchronized (ViewHelper.class) {
//            if (instance == null) {
//                synchronized (ViewHelper.class) {
//                    instance = new ViewHelper(context);
//                }
//            }
//            return instance;
//        }
        return new ViewHelper(context);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(Activity activity, @IdRes int id) {
        return (T) activity.findViewById(id);
    }

    /**
     * 初始化ids
     *
     * @param ids
     * @return
     */
    public ViewHelper id(int... ids) {
        Activity activity = (Activity) mContext;
        mViews = new View[ids.length];
        for (int i = 0; i < ids.length; i++) {
            if (null == activity) {
                break;
            }
            mViews[i] = activity.findViewById(ids[i]);
        }
        // mViews = new View[ids.length];
        // LayoutInflater inflater = LayoutInflater.from(mContext);
        // for (int i = 0; i < ids.length; i++) {
        // mViews[i] = inflater.
        // }
        return this;
    }

    /**
     * 初始化views
     *
     * @param views
     * @return
     */
    public ViewHelper view(View... views) {
        mViews = views;
        if (views.length != 0) {
            mContext = views[0].getContext();
        }
        return this;
    }

    // ----具体实现

    /**
     * 初始化layout布局
     *
     * @param layoutResID
     * @return
     */
    public ViewHelper layout(int layoutResID) {
        Activity activity = (Activity) mContext;
        activity.setContentView(layoutResID);
        return this;
    }

    public ViewHelper text(CharSequence... text) {
        if (text != null) {
            for (int i = 0; i < text.length; i++) {
                TextView textView = (TextView) mViews[i];
                if (null == textView) continue;

                textView.setText(text[i] == null ? "" : text[i]);
            }
        }
        return this;
    }

    public ViewHelper listener(OnClickListener... listener) {
        for (int i = 0; i < mViews.length; i++) {
            if (null == mViews[i]) {
                continue;
            }
            mViews[i].setOnClickListener(listener[i >= listener.length ? listener.length - 1 : i]);
        }
        return this;
    }

    public ViewHelper setVisibility(int visibility) {
        for (int i = 0; i < mViews.length; i++) {
            ViewUtil.setViewVisibility(visibility, mViews[i]);
//            mViews[i].setVisibility(visibility);
        }
        return this;
    }

    public ViewHelper adapter(BaseAdapter adapter) {
        for (int i = 0; i < mViews.length; i++) {
            View child = mViews[i];
            if (child instanceof AdapterView<?>) {
                ((AdapterView<BaseAdapter>) child).setAdapter(adapter);
            }
        }
        return this;
    }

    public ViewHelper itemListener(OnItemClickListener listener) {
        for (int i = 0; i < mViews.length; i++) {
            View child = mViews[i];
            if (child instanceof AdapterView<?>) {
                ((AdapterView<BaseAdapter>) child).setOnItemClickListener(listener);
            }
        }
        return this;
    }

    public ViewHelper setTag(Object... obj) {
        for (int i = 0; i < mViews.length; i++) {
            if (null == mViews[i]) {
                continue;
            }
            mViews[i].setTag(obj[i >= obj.length ? obj.length - 1 : i]);
        }
        return this;
    }
}
