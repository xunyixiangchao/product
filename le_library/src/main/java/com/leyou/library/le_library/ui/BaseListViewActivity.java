package com.leyou.library.le_library.ui;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.ichsy.libs.core.frame.adapter.BasePagingFrameAdapter;
import com.leyou.library.le_library.comm.helper.PullViewHelper;
import com.leyou.library.le_library.model.response.BasePagingResponse;

import in.srain.cube.views.ptr.PtrFrameLayout;
import library.liuyh.com.lelibrary.R;

/**
 * 基础的显示listview的activity
 * Created by liuyuhang on 2017/2/9.
 */

public abstract class BaseListViewActivity extends BaseActivity {
    private ListView mListView;

    public interface ListViewPullListener {
        void onPull();
    }

    /**
     * listview的Divider是否要隐藏
     *
     * @return
     */
    protected abstract boolean hideDivider();

    public interface AdapterPagingHandler {
        void onNextPage();
    }

    /**
     * 初始化布局，设置的view
     *
     * @return Modifier： Modified Date： Modify：
     */
    @Override
    protected int onLayoutInflate() {
        return R.layout.activity_empty_listview_layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (hideDivider()) {
            getListView().setDividerHeight(0);
            getListView().setDivider(new BitmapDrawable());
        }

        getListView().setBackgroundResource(R.color.le_base_background);
    }

    /**
     * 设置顶部view
     */
    public void setHeaderView(View view) {
        LinearLayout headerGroup = (LinearLayout) findViewById(R.id.group_header);
        headerGroup.removeAllViews();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        headerGroup.addView(view);
    }

    /**
     * 会自动转换单位，不需要在写
     *
     * @param top
     */
    public void setMarginTop(int top) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp.topMargin = ViewUtil.dip2px(getContext(), top);
        getListView().setLayoutParams(lp);

//        val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
//        lp.topMargin = ViewUtil.dip2px(context, 11F)
//        listView.layoutParams = lp
    }

    public ListView getListView() {
        if (null == mListView) {
            mListView = (ListView) findViewById(R.id.listview_main);
        }
        return mListView;
    }

    public void setAdapter(BaseAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        getListView().setOnItemClickListener(listener);
    }

    public void autoAdapter(BasePagingFrameAdapter adapter, BasePagingResponse response, AdapterPagingHandler handler) {
        if (response.is_end) {
            adapter.noMorePage();
        } else {
            adapter.mayHaveNextPage();
            handler.onNextPage();
        }
    }

    public void setBottomView(View view) {
        LinearLayout bottomGroup = (LinearLayout) findViewById(R.id.ll_bottom);

        bottomGroup.removeAllViews();
        bottomGroup.addView(view);
    }

    protected void setListViewPull(final ListViewPullListener listener) {
        PullViewHelper.bindView(this, getPullView(), new PullViewHelper.PullListener<PtrFrameLayout>() {


            @Override
            public void onStartPull() {

            }

            @Override
            public void onStopPull() {

            }

            @Override
            public void onPull(PtrFrameLayout refreshLayout) {
                listener.onPull();
            }
        });
    }

    protected PtrFrameLayout getPullView() {
        return (PtrFrameLayout) findViewById(R.id.view_refresh);
    }

}
