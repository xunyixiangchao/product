package com.leyou.library.le_library.frame;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ichsy.libs.core.frame.BaseDialogFrameActivity;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.leyou.library.le_library.comm.network.filter.LeRequestPackingFilter;
import com.leyou.library.le_library.config.LeConstant;
import com.leyou.library.le_library.ui.BaseActivity;

/**
 * 解耦数据操作的activity
 * Created by liuyuhang on 2018/7/19.
 */
public abstract class BaseDataActivity<DM extends BaseDataManager, UM extends BaseUiManager> extends BaseActivity {
    /**
     * 数据管理器
     */
    public DM dataManager;

    /**
     * ui管理器
     */
    public UM uiManager;

    @Override
    protected int onLayoutInflate() {
        return uiManager.initLayoutUi();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiManager.onInitUi(this, mContentView);

        dataManager.onDataRequest(this, new RequestListener() {
            @Override
            public void onHttpRequestComplete(@NonNull String url, @NonNull HttpContext httpContext) {
                super.onHttpRequestComplete(url, httpContext);

                if (httpContext.code == LeConstant.CODE.CODE_OK) {
                    uiManager.onUpdateUi((BaseDialogFrameActivity) getActivity(), mContentView, httpContext.getResponseObject());
                } else if (LeRequestPackingFilter.CODE_ERROR_NET_ERROR == httpContext.code) {
                    uiManager.onNetError();
                } else {
                    uiManager.onDataError(httpContext.code, httpContext.message);
                }
            }
        });
    }

}
