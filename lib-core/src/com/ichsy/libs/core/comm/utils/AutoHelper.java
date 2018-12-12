package com.ichsy.libs.core.comm.utils;

import android.view.View;

import com.ichsy.libs.core.frame.adapter.AdapterListener;
import com.ichsy.libs.core.frame.adapter.BaseFrameAdapter;
import com.ichsy.libs.core.frame.BaseDialogFrameActivity;

/**
 * 全自动的ListView进度条，可以完整处理ListView在展示数据的整个周期UI显示
 * Created by liuyuhang on 16/5/11.
 */
public class AutoHelper {


    /**
     * adapter和progress自动托管处理
     *
     * @param activity
     * @param adapter
     * @param listener 没有加载到数据的时候的监听
     */
    public static void autoProgress(final BaseDialogFrameActivity activity, BaseFrameAdapter<?> adapter, final View.OnClickListener listener) {
        adapter.setAdapterListener(new AdapterListener() {
            @Override
            public void onBeginBindData() {
                activity.getDialogHUB().showProgress();
            }

            @Override
            public void onDataBindComplete(boolean empty) {
                if(null == activity) return ;
                if (empty) {
                    activity.getDialogHUB().showMessageView("", listener);
                } else {
                    activity.getDialogHUB().dismiss();
                }
            }

        });
    }
}
