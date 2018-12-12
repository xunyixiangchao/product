package com.leyou.library.le_library.model.request;

/**
 * 分页请求的base类
 * Created by liuyuhang on 2017/3/3.
 */

public class BasePagingRequest {
    public int page_index;
    public int page_size;
    public Integer is_daguan_data;// 达观ab测试

    public BasePagingRequest() {
    }

    /**
     *
     * @param page_index 索引
     * @param page_size 单页宽度
     */
    public BasePagingRequest(int page_index, int page_size) {
        this.page_index = page_index;
        this.page_size = page_size;
    }
}
