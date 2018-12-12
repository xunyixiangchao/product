package com.ichsy.libs.core.comm.utils;

/**
 * 用来写入loger的工具类
 * Created by liuyuhang on 2018/6/22.
 */

public class LogerWriter {

    /**
     * 写入log到文件
     *
     * @param logPath 写入路径
     * @param logName 写入文件名
     * @param log     写入内容
     */
    public static void write(String logPath, String logName, String log) {
        FileUtil.writeByBufferWriter(logPath, logName, log, false);
    }
}
