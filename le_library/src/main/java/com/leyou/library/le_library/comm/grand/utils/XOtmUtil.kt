package com.leyou.library.le_library.comm.grand.utils

import android.content.Context
import android.text.TextUtils
import com.ichsy.libs.core.comm.utils.DeviceUtil
import com.leyou.library.le_library.comm.collection.getProduct
import com.leyou.library.le_library.comm.grand.constant.XOtmConstant
import com.leyou.library.le_library.comm.grand.modle.request.XOtmRequest
import com.leyou.library.le_library.comm.network.comm.user.TokenOperation

/**
 * Created by ss on 2018/7/31.
 */

private fun request(context: Context, url: String, request: Any) {
//    val options = RequestOptions()
//    options.requestType = RequestOptions.Mothed.POST
//    SimpleHttpHelper(context, options).post(url + "?${System.currentTimeMillis() / 1000}", request, XOtmResponse::class.java, null)
//    SimpleHttpHelper(context, options).doPost(url, request, XOtmResponse::class.java, null)
}

/*
dimensional_code (必填)	varchar	该用户行为的维度代码，在X智能后台中取得
obj_id (必填)	varchar	SKU
word	varchar	商品名
category 	text	六级分类名称
cateid	varchar	分类ID，格式：2级分类_4级分类_6级分类xx_xx_xx，从左到右分别为主、子分类关系
 */
fun requestRecordAction(context: Context, dimensional: String, sku: String) {
    val request = XOtmRequest()

    val userId = TokenOperation.getUserId(context)
    val imei = DeviceUtil.getDeviceId(context)
    var ip: String? = DeviceUtil.getIp(context)
    if (ip != null) {
        ip = ip.replace("-", ".")
    }
    request.userID = if (TextUtils.isEmpty(userId)) imei else userId
    request.mac_addr = imei
    request.log_ip = ip

    request.dimensional_code = dimensional

    val product = context.getProduct(sku)
    val cateIds = "${product.secondCommodityID ?: ""}_${product.forthCommodityID
            ?: ""}_${product.sixthCommodityID ?: ""}"
    request.obj_id = product.commodityID
    request.word = product.commodityName
    request.category = product.sixthCommodityName
    request.cateid = cateIds

    request(context, XOtmConstant.URL_RECORD_ACTION, request)
}

