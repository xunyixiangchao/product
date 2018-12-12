package com.ichsy.libs.core.comm.hotfix;

import java.util.HashMap;
import java.util.List;

public class HotFixResponse {
    /**
     * 热修复的列表
     */
    public List<HotFixVo> hotFixList;

    /**
     * 总线网络切换逻辑
     */
    public HashMap<String, String> bus_mapping;
}
