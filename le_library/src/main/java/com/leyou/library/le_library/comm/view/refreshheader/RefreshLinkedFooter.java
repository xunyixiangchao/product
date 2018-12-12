package com.leyou.library.le_library.comm.view.refreshheader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dalong.refreshlayout.OnFooterListener;
import com.dalong.refreshlayout.RefreshStatus;

import library.liuyh.com.lelibrary.R;

/**
 * 下拉刷新自定义底部
 * Created by zhaoye
 */

public class RefreshLinkedFooter extends LinearLayout implements OnFooterListener {

    private View mHeaderView;

    public RefreshLinkedFooter(Context context) {
        super(context);
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.refress_linked_footer_view, this, true);
    }


    @Override
    public void onLoadBefore(int scrollY) {
    }

    @Override
    public void onLoadAfter(int scrollY) {
    }

    @Override
    public void onLoadReady(int scrollY) {
    }

    @Override
    public void onLoading(int scrollY) {
    }

    @Override
    public void onLoadComplete(int scrollY, boolean isLoadSuccess) {
    }

    @Override
    public void onLoadCancel(int scrollY) {
    }
}
