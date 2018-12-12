package com.ichsy.libs.core.frame.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichsy.libs.core.frame.adapter.delegate.AdapterDataDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerAdapter的基类
 * Created by liuyuhang on 16/5/12.
 */
public abstract class BaseRecyclerFrameAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> implements RecyclerViewDrawer<T>, AdapterDataDelegate<T> {
    //    private AdapterHelper<T> adapterHelper;
    public interface OnRecyclerItemClickListener<T> {
        void onItemClick(View parent, List<T> data, int position);
    }

    private Context context;
    protected LayoutInflater inflater;
    public List<T> list;
    private OnRecyclerItemClickListener<T> itemClickListener;

    public BaseRecyclerFrameAdapter(Context context) {
//        this.adapterHelper = new AdapterHelper<>(context);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = onViewCreate(viewType, inflater, viewGroup);
        return new BaseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder viewHolder, final int position) {
        onViewAttach(position, list.get(position), viewHolder);
        if (itemClickListener != null) {
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(viewHolder.getConvertView(), list, viewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public void resetData(List<T> data) {
        if (null == data || data.isEmpty()) {
            return;
        }
        if (null == this.list) {
            this.list = new ArrayList<>();
        } else {
            this.list.clear();
        }
        addData(data);
    }

    @Override
    public void addData(List<T> data) {
        if (null == data) {
            return;
        }

        if (null == this.list) {
            this.list = new ArrayList<>();
        }

//        int start = list.size() - 1;
        this.list.addAll(data);
        notifyDataSetChanged();
//        notifyItemRangeInserted(start, data.size());
    }

    @Override
    public List<T> getData() {
        if (null == list) {
            list = new ArrayList<>();
        }
        return this.list;
    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void clearData() {
        if (list != null) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }


}
