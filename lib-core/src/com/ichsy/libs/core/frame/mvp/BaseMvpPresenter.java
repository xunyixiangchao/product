package com.ichsy.libs.core.frame.mvp;

import android.app.Activity;
import android.content.Context;

import com.ichsy.libs.core.comm.utils.ThreadPoolUtil;

/**
 * mvp的业务处理基类
 * Created by liuyuhang on 16/10/12.
 */
public abstract class BaseMvpPresenter<T, M extends BaseMvpModel> {
    private M mDataModel;//数据模型
    private BaseMvpView<M> mRootView;//基础view

    public abstract void onInit(Context context);

    public void bind(Context context, M model, BaseMvpView<M> view) {
        this.mDataModel = model;
        this.mRootView = view;

        mDataModel.setContext(context);

        onInit(context);
        mRootView.initView((Activity) context, mDataModel);
    }

    public void notifyViewDataChanged(T data) {
        mDataModel.update(data);
        ThreadPoolUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mRootView.onViewUpdate(mDataModel);
            }
        });
    }

//    /**
//     * 更新数据,会通知view重新绘制
//     *
//     * @param data
//     */
//    public void update(T data) {
//        ThreadPoolUtil.runOnMainThread(new Runnable() {
//            @Override
//            public void run() {
//                mRootView.onViewUpdate(mDataModel);
//            }
//        });
//    }

//    public BaseMvpView<M> getModelView() {
//        return mRootView;
//    }

    public M getDataModel() {
        return mDataModel;
    }
}
