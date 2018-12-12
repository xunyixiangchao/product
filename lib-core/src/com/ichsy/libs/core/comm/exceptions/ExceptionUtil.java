package com.ichsy.libs.core.comm.exceptions;

import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Exception错误处理类
 *
 * @author liuyuhang
 */
public class ExceptionUtil {

    /**
     * 获取Exception中的内容
     *
     * @param e
     * @return
     */
    public static String getException(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

    /**
     * 检查对象可能为空，如果为空，提前崩溃
     *
     * @param object
     */
    public static void checkIfNull(@NonNull Object... object) {
        for (int i = 0; i < object.length; i++) {
            if (null == object[i]) {
                throw new NullPointerException(String.format("第%s个参数为空了，需要对空对象进行合理处理", i));
            }
        }
    }
}
