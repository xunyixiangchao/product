package com.ichsy.libs.core.frame.adapter.group;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichsy.libs.core.frame.adapter.BaseAdapterViewType;
import com.ichsy.libs.core.frame.adapter.BasePagingFrameAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Item分组（每个ItemLayout不相同），并且带有分页的adapter
 *
 * @param <T>
 * @author liuyuhang
 */
public abstract class BaseGroupPagingAdapter<T> extends BasePagingFrameAdapter<T> {
    // 存放model的模板，根据模板ID可以拿到当前model;
    private HashMap<String, Holder> mModelMap;
    // 存放modelID的模板，根据当前type可以拿到modelID;
    private SparseArray<String> mModelIDMap;

    /**
     * 根据当前位置获取模板type
     *
     * @param data
     * @param position
     * @return
     */
    public abstract String getTypeByPosition(List<T> data, int position);

    /**
     * 初始化
     *
     * @param context
     * @param groupModels 支持的模板list
     */
    public BaseGroupPagingAdapter(Context context, List<GroupModelAdapter<T>> groupModels) {
        super(context);

        this.mModelMap = new HashMap<>();
        this.mModelIDMap = new SparseArray<>();

        int startPosition = getTypeCounts() + 1;
        for (int i = 0; i < groupModels.size(); i++) {
            // 遍历model，初始化并做记录
            GroupModelAdapter<T> groupModelAdapter = groupModels.get(i);

            // 调用每个model模板的初始化方法
            groupModelAdapter.onModelCreate(getContext());

            if (groupModelAdapter instanceof GroupModelHandler) {
                GroupModelHandler.AdapterHandler handler = new GroupModelHandler.AdapterHandler() {
                    @Override
                    public void adapterNotifyDataSetChanged() {
                        notifyDataSetChanged();
                    }
                };
                ((GroupModelHandler) groupModelAdapter).onAdapterHandlerCallback(handler);
            }

            if (groupModelAdapter instanceof GroupModelAdapterExt) {
                ((GroupModelAdapterExt<T>) groupModelAdapter).onData(getData());
            }

            // 为每个模板分配type，type为目前有的typeCount+i;
            int type = startPosition + i;

            Holder holder = new Holder();
            holder.position = type;
            holder.groupModelAdapter = groupModelAdapter;
            this.mModelMap.put(groupModelAdapter.getGroupModelID(), holder);

            this.mModelIDMap.put(type, groupModelAdapter.getGroupModelID());

            // System.out.println("init type: " + type + "    model id: " +
            // groupModelAdapter.getGroupModelID());
        }
    }

    @Override
    public int getViewTypeCount() {
        // 应该是+1，但是因为有可能有不存在的布局(返回了没有定义的布局)，所以预留出一位
        return getTypeCounts() + 2;
    }

    /**
     * 默认有的TypeCount
     *
     * @return
     */
    private int getTypeCounts() {
        return super.getViewTypeCount() + mModelMap.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getItemViewType(position) == BaseAdapterViewType.VIEW_TYPE_CONTENT) {
            // System.out.println("return type is: " +
            // mGroupPostion.get(mGroupPostion.get(getTypeByPosition(position)).groupModelAdapter.getGroupModelID()).position
            // + " getViewTypeCount is: " + getViewTypeCount() );

            BaseGroupPagingAdapter<T>.Holder holder = mModelMap.get(getTypeByPosition(getData(), position));// 查找到当前position的holder
            if (holder == null) {
                return getTypeCounts() + 1;// 返回一个不存在的ErrorType
            } else {
                return mModelMap.get(holder.groupModelAdapter.getGroupModelID()).position;
            }
        } else {
            return super.getItemViewType(position);
        }
    }

    /**
     * 根据modelID拿到type
     *
     * @param type
     * @return
     */
    private String getModelIDByType(int type) {
        return mModelIDMap.get(type);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type <= super.getViewTypeCount()) {
            return super.getView(position, convertView, parent);
        } else if (type == (getTypeCounts() + 1)) {
            // ErrorType
            if (convertView == null) {
                // 并且没有设置过布局
                convertView = new View(getContext());
            }
        } else {
            GroupModelAdapter<T> groupModelAdapter = mModelMap.get(getModelIDByType(type)).groupModelAdapter;
            if (convertView == null) {
                convertView = groupModelAdapter.getGroupViewCreate(position, getInflater(), parent);
            }
            groupModelAdapter.onGroupViewAttach(position, getItem(position), convertView);
        }
        return convertView;
    }

    @Override
    public View onViewCreate(int position, @NonNull LayoutInflater inflater, ViewGroup parent) {
        // doNothing
        return null;
    }

    @Override
    public void onViewAttach(int position, @NonNull T item, View convertView) {
        // doNothing
    }

    private class Holder {
        public int position;
        private GroupModelAdapter<T> groupModelAdapter;
    }

    /**
     * 根据指定模板的type，找到列表中第一个position位置
     *
     * @return
     */
    public int findFirstTypePosition(String type) {
        for (int i = 0; i < getData().size(); i++) {
            String currentType = getTypeByPosition(getData(), i);
            if (currentType.equals(type)) {
                return i;
            }
        }
        return -1;
    }
}
