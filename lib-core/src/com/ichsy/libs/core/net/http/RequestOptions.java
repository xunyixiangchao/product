package com.ichsy.libs.core.net.http;

import java.util.HashMap;

/**
 * 请求所需要的参数
 *
 * @author liuyuhang
 */
public class RequestOptions {

    public static class Mothed {
        public static final String POST = "post";
        public static final String GET = "get";

    }

    private int timeout = 0;
    private Object tag;
    private HashMap<String, String> header;
    private String requestType;

    private String httpsCer;// https证书位置
    private String httpsCerPassWord;
    private boolean toast = true;

    public HashMap<String, String> params = new HashMap<>();

    private boolean cancelIfActivityFinish = false;

    public int getTimeout() {
        return timeout;
    }

    /**
     * 设置超时时间，单位，秒
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public <T> T getTag() {
        return (T) tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getHttpsCer() {
        return httpsCer;
    }

    public void setHttpsCer(String httpsCer) {
        this.httpsCer = httpsCer;
    }

    public String getHttpsCerPassWord() {
        return httpsCerPassWord;
    }

    public void setHttpsCerPassWord(String httpsCerPassWord) {
        this.httpsCerPassWord = httpsCerPassWord;
    }

    public void setRequestType(String type) {
        this.requestType = type;
    }

    public String getRequestType() {
        return requestType;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public void toastDisplay(boolean display) {
        this.toast = display;
    }

    public boolean isToastDisplay() {
        return toast;
    }

    public boolean isCancelIfActivityFinish() {
        return cancelIfActivityFinish;
    }

    public void setCancelIfActivityFinish(boolean cancelIfActivityFinish) {
        this.cancelIfActivityFinish = cancelIfActivityFinish;
    }
}
