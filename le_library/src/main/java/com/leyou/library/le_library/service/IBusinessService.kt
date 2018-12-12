package com.leyou.library.le_library.service

import android.content.Context
import com.ichsy.libs.core.comm.utils.LogUtils

/**
 * 基础业务服务的基类
 * Created by liuyuhang on 2017/7/8.
 */
interface IBusinessService {
    companion object {
        val TAG = "business_service"
    }

    /**
     * 初始化安装方法
     */
    fun install(context: Context) {
        LogUtils.i(TAG, "${this::class.qualifiedName} has install")
    }

    /**
     * 卸载方法，做销毁处理
     */
    fun unInstall(context: Context) {
        LogUtils.i(TAG, "${this::class.qualifiedName} has unInstall")
    }
}