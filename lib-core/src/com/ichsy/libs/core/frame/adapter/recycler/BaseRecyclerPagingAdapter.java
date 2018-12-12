package com.ichsy.libs.core.frame.adapter.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ichsy.libs.core.frame.adapter.delegate.AdapterPagingDelegate;

/**
 * 可分页的recyclerView的父类
 * Created by liuyuhang on 16/5/16.
 */
public abstract class BaseRecyclerPagingAdapter<T> extends BaseRecyclerFrameAdapter<T> {

    private AdapterPagingDelegate<T> pagingDelegate;

//    private AdapterPagingDelegate.PagingListener<T> pagingListener;

    public BaseRecyclerPagingAdapter(Context context) {
        super(context);

        pagingDelegate = new AdapterPagingDelegate<>(this);
    }

    public void setPagingListener(AdapterPagingDelegate.PagingListener<T> pagingListener) {
        pagingDelegate.setPagingListener(pagingListener);
    }

    /**
     * 清空数据，之后会重新执行分页方法 onNextPageRequest(adapter, page)
     */
    @Override
    public void clearData() {
        super.clearData();
        pagingDelegate.resetPage();
    }

    @Override
    public int getItemCount() {
        return pagingDelegate.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return pagingDelegate.getItemViewType(position);
    }

    public AdapterPagingDelegate getPagingDelegate() {
        return pagingDelegate;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = pagingDelegate.createLoadingView(viewType, parent);
        if (itemView == null) {
//            itemView = onViewCreate(viewType, inflater, parent);
            return super.onCreateViewHolder(parent, viewType);
        } else {
            return new BaseRecyclerViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        if (position < getData().size()) {
            super.onBindViewHolder(viewHolder, position);
//            onViewAttach(position, getData().get(position), viewHolder);
        } else {
            pagingDelegate.onItemViewAttach(position, viewHolder);
        }
    }

}
