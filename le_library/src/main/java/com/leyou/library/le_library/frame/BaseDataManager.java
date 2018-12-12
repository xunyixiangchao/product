package com.leyou.library.le_library.frame;

import android.support.annotation.NonNull;

import com.ichsy.libs.core.frame.BaseDialogFrameActivity;
import com.ichsy.libs.core.frame.BaseFrameActivity;
import com.ichsy.libs.core.net.http.RequestListener;

import org.jetbrains.annotations.NotNull;

/**
 * 基础的数据获取类
 *
 * @author liuyuhang
 * @date 2018/7/19
 */
public abstract class BaseDataManager {
    private BaseFrameActivity context;

    /**
     * 数据请求
     *
     * @param context
     * @param listener
     */
    public abstract void onDataRequest(@NonNull BaseDialogFrameActivity context, @NonNull RequestListener listener);

    public BaseDataManager(@NotNull BaseDialogFrameActivity context) {
        this.context = context;
    }
}
