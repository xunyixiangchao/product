package com.leyou.library.le_library.comm.helper;

import android.content.Context;

import com.leyou.library.le_library.comm.view.LePullRefreshHeader;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by liuyuhang on 16/6/24.
 */
public class PullViewHelper {

    public interface PullListener<T> {
        /**
         * 准备下拉
         */
        void onStartPull();

        /**
         * 结束下拉
         */
        void onStopPull();

        /**
         * 下拉监听
         *
         * @param refreshLayout
         */
        void onPull(T refreshLayout);
    }

    public abstract static class LePullListener<T> implements PullListener<T> {

        @Override
        public void onStartPull() {

        }

        @Override
        public void onStopPull() {

        }
    }

    public static void bindView(Context context, PtrFrameLayout refreshView, final PullListener<PtrFrameLayout> listener) {
        LePullRefreshHeader defaultHeader = new LePullRefreshHeader(context);
        refreshView.setHeaderView(defaultHeader);
        refreshView.addPtrUIHandler(defaultHeader);

        refreshView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                listener.onPull(frame);
            }
        });

        defaultHeader.setPullListener(new PullListener<PtrFrameLayout>() {
            @Override
            public void onStartPull() {
                listener.onStartPull();
            }

            @Override
            public void onStopPull() {
                listener.onStopPull();
            }

            @Override
            public void onPull(PtrFrameLayout refreshLayout) {
            }
        });
    }

}
