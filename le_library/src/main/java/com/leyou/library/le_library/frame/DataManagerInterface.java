package com.leyou.library.le_library.frame;

import android.content.Context;

/**
 * Created by liuyuhang on 2018/7/26.
 */
public interface DataManagerInterface<T extends BaseDataManager> {

    /**
     * 绑定数据管理接口
     *
     * @param context
     * @return
     */
    T bindDataManger(Context context);

}
