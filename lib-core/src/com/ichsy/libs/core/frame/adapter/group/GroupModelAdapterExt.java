package com.ichsy.libs.core.frame.adapter.group;

import java.util.List;

/**
 * GroupAdapter实现了在一个ListView当中有多个不同布局的效果
 *
 * @param <T>
 * @author liuyuhang
 */
public interface GroupModelAdapterExt<T> extends GroupModelAdapter<T> {

    void onData(List<T> list);

//    /**
//     * 初始化
//     *
//     * @param context
//     */
//    void onModelCreate(Context context);
//
//    /**
//     * 返回当前模板的Id(模板id为自定义)
//     *
//     * @return
//     */
//    String getGroupModelID();
//
//    /**
//     * view第一次创建的时候调用，做一些初始化工作，每个Item创建的时候只会调用一次
//     *
//     * @param rowPosition 在整个ListView当中的position
//     * @param inflater
//     * @param parent
//     * @return
//     */
//    View getGroupViewCreate(int rowPosition, LayoutInflater inflater, ViewGroup parent);
//
//    /**
//     * view每次出现都会调用，可以用来设置文字，监听器
//     *
//     * @param rowPosition 在整个ListView当中的position
//     * @param item           每个position的item
//     * @param convertView
//     */
//    void onGroupViewAttach(int rowPosition, T item, View convertView);

}
