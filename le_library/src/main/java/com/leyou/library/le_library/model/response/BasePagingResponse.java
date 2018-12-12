package com.leyou.library.le_library.model.response;

/**
 * 分页返回结果的base类
 * Created by liuyuhang on 2017/3/3.
 */

public class BasePagingResponse {
    public int page_index;// 页码
    public int page_size;// 每页的记录数
    public int count;// 信息总数
    public boolean is_end = true;// 是否是最后一页
}
