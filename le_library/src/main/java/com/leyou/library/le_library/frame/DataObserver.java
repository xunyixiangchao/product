package com.leyou.library.le_library.frame;

import android.content.Context;

/**
 * 数据改变的观察者
 * Created by liuyuhang on 2018/7/30.
 */

public class DataObserver<T> {
    private Context context;
    private UiModel<T> uiModel;

    public DataObserver(Context context, UiModel<T> uiModel) {
        this.context = context;
        this.uiModel = uiModel;
    }

    /**
     * 每当数据需要改变的时候，调用此方法来发送给观察者数据
     *
     * @param value 改变的数据
     */
    public void setValue(T value) {
        uiModel.onUpdateUi(value);
    }
}
