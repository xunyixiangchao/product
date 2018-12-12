package com.ichsy.libs.core.comm.logwatch.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.logwatch.InfoBean;
import com.ichsy.libs.core.comm.logwatch.LogWatcher;
import com.ichsy.libs.core.comm.logwatch.db.DAO;
import com.ichsy.libs.core.comm.utils.ToastUtils;
import com.ichsy.libs.core.comm.utils.ViewHolder;
import com.ichsy.libs.core.frame.BaseFrameActivity;
import com.ichsy.libs.core.frame.adapter.BasePagingFrameAdapter;

import java.util.ArrayList;

/**
 * logwatch点击之后的UI
 * Created by liuyuhang on 2016/12/13.
 */

public class LogWatcherActivity extends BaseFrameActivity {
    private String mCurrentType;

    /**
     * 初始化布局，设置的view
     *
     * @return Modifier： Modified Date： Modify：
     */
    @Override
    protected int onLayoutInflate() {
        return R.layout.activity_logwatcher_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationController.setBackgroundColor(Color.WHITE);
        navigationController.setDividerResource(android.R.color.darker_gray);
        navigationController.setTitle("LogWatcher");

        navigationController.setRightButton("清除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAO dao = new DAO(getContext());
                dao.delete();

                ListView listView = (ListView) findViewById(R.id.lv_main);

                if (null != listView.getAdapter()) {
                    ((LogWatcherAdapter) listView.getAdapter()).clearAdapter();
                }
            }
        });

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogWatcher.getInstance().dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogWatcher.getInstance().show();
    }


    private void initView() {
        LinearLayout headerView = (LinearLayout) findViewById(R.id.ll_header);
        ListView listView = (ListView) findViewById(R.id.lv_main);

        final LogWatcherAdapter adapter = new LogWatcherAdapter(this);

        final BasePagingFrameAdapter.PagingListener<InfoBean> pagingListener = new BasePagingFrameAdapter.PagingListener<InfoBean>() {
            @Override
            public void onNextPageRequest(BasePagingFrameAdapter<InfoBean> adapter, int page) {
                DAO dao = new DAO(getActivity());
                ArrayList<InfoBean> query;

                int size = 50;

                if (TextUtils.isEmpty(mCurrentType) || "全部".equals(mCurrentType)) {
                    query = dao.query(page, size);
                } else {
                    query = dao.query(mCurrentType, page, size);
                }
                if (null != query) {
                    adapter.addData(query);
                    if (query.size() < size) {
                        adapter.noMorePage();
                    } else {
                        adapter.mayHaveNextPage();
                    }
                } else {
                    adapter.noMorePage();
                }
            }
        };


        ArrayList<InfoBean> headerList = new DAO(getActivity()).queryCateGory();
        mCurrentType = headerList.get(0).getClassesName();

        for (final InfoBean infoBean : headerList) {
            final Button headerItem = new Button(getActivity());

            headerItem.setText(infoBean.getClassesName());

            headerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((Button) v).getText().toString().equals(mCurrentType)) {
                        return;
                    }

                    mCurrentType = infoBean.getClassesName();
                    adapter.clearAdapter();
                    adapter.setOnPagingListener(pagingListener);
                }
            });

            headerView.addView(headerItem);
        }

        listView.setAdapter(adapter);

        adapter.setOnPagingListener(pagingListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InfoBean item = adapter.getItem(position);

                ClipboardManager myClipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData myClip = ClipData.newPlainText("text", text);
                myClipboard.setText(item.getClassesContent());

                ToastUtils.showMessage(getActivity(), "复制成功");
            }
        });

//        listView.setSelection(adapter.getCount() - 1);
    }

    private class LogWatcherAdapter extends BasePagingFrameAdapter<InfoBean> {

        public LogWatcherAdapter(Context context) {
            super(context);
        }

        /**
         * 在adapter的getView中，首次创建view，初始化并返回创建的布局
         *
         * @param position
         * @param inflater
         * @param parent
         * @return
         */
        @Override
        public View onViewCreate(int position, LayoutInflater inflater, ViewGroup parent) {
            return inflater.inflate(R.layout.adapter_logwatcher_item, parent, false);
        }

        /**
         * 在adapter的getView中，每次滑动listView会循环执行本方法
         *
         * @param position
         * @param item
         * @param convertView
         */
        @Override
        public void onViewAttach(int position, InfoBean item, View convertView) {
            TextView contentTextView = ViewHolder.get(convertView, R.id.tv_content);

            contentTextView.setText(item.getClassesContent());
        }
    }
}
