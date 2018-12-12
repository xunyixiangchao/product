package com.ichsy.libs.core.comm.bus.url.route;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由表解析类，将不规范的格式都解析为路由标准的格式
 * Created by liuyuhang on 2018/6/25.
 */
public class RouteParser {
    /**
     * 规则列表
     */
    private List<RouteRule> rules;

    public void addRule(RouteRule routeRule) {
        if (null == rules) {
            rules = new ArrayList<>();
        }
    }

    public interface RouteRule {
        String[] ruleKeys();

        String rule(@NonNull RouterBuilder builder);
    }

}
