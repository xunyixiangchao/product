package com.leyou.library.le_library.comm.grand.modle.request;

import com.leyou.library.le_library.comm.grand.modle.GrandTypeContentVo;

import java.util.List;

/**
 * 达观请求类
 *
 * Created by ss on 2018/7/10.
 */
public class GrandRequest {
    public int appid;
    public String table_name;
    public List<GrandTypeContentVo> table_content;
}
