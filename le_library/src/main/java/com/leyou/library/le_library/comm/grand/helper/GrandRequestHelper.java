package com.leyou.library.le_library.comm.grand.helper;

import android.content.Context;

import com.ichsy.libs.core.net.http.RequestOptions;
import com.leyou.library.le_library.comm.grand.modle.GrandTypeContentVo;
import com.leyou.library.le_library.comm.grand.modle.request.GrandRequest;
import com.leyou.library.le_library.comm.grand.modle.response.GrandResponse;
import com.leyou.library.le_library.comm.grand.network.SimpleHttpHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 达观请求帮助类
 *
 * Created by ss on 2018/7/10.
 */
public class GrandRequestHelper {

    private GrandRequest request;

    private GrandRequestHelper(GrandRequest request) {
        this.request = request;
    }

    public void request(Context context, String url) {
        RequestOptions options = new RequestOptions();
        options.setRequestType(RequestOptions.Mothed.POST);
        options.toastDisplay(false);
        new SimpleHttpHelper(context, options).post(url, request, GrandResponse.class, null);
    }


    public static class Builder {
        private int appId;
        private String tableName;
        private List<GrandTypeContentVo> contentList;

        private String cmd;

        public Builder() {
            contentList = new ArrayList<>();
        }

        public Builder setAppId(int appId) {
            this.appId = appId;
            return this;
        }

        public Builder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder setCmd(String cmd) {
            this.cmd = cmd;
            return this;
        }

        public final Builder addField(Object... ts) {
            for (Object t : ts) {
                GrandTypeContentVo content = new GrandTypeContentVo();
                content.cmd = cmd;
                content.fields = t;
                contentList.add(content);
            }
            return this;
        }

        public GrandRequestHelper create() {
            GrandRequest request = new GrandRequest();
            request.appid = appId;
            request.table_name = tableName;
            request.table_content = contentList;

            return new GrandRequestHelper(request);
        }
    }
}
