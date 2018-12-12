package com.leyou.library.le_library.comm.collection

import android.content.Context
import com.ichsy.libs.core.dao.SharedPreferencesProvider
import org.json.JSONArray
import org.json.JSONObject

/**
 * 神策埋点一些工具方法
 *
 * Created by ss on 2018/7/18.
 */

fun Context.saveProduct(sku: String, fromModule: String?, fromTag: String?, fromSubTag: String?
                        , commodityID: String?, commodityName: String?
                        , originalPrice: Float?, preferentialPrice: Float?, purchaseNum: Int?
                        , commodityBrandID: String?, commodityBrandName: String?
                        , secondCommodityName: String?, secondCommodityID: String?
                        , forthCommodityName: String?, forthCommodityID: String?
                        , sixthCommodityName: String?, sixthCommodityID: String?
                        , sceneType: String?, requestId: String?) {
    val sensors = SensorsOriginVo()
    sensors.fromModule = fromModule ?: ""
    sensors.fromTag = fromTag ?: ""
    sensors.fromSubTag = fromSubTag ?: ""
    sensors.commodityID = commodityID ?: ""
    sensors.commodityName = commodityName ?: ""
    sensors.originalPrice = originalPrice ?: 0f
    sensors.preferentialPrice = preferentialPrice ?: 0f
    sensors.purchaseNum = purchaseNum ?: 0
    sensors.commodityBrandID = commodityBrandID ?: ""
    sensors.commodityBrandName = commodityBrandName ?: ""
    sensors.secondCommodityName = secondCommodityName ?: ""
    sensors.secondCommodityID = secondCommodityID ?: ""
    sensors.forthCommodityName = forthCommodityName ?: ""
    sensors.forthCommodityID = forthCommodityID ?: ""
    sensors.sixthCommodityName = sixthCommodityName ?: ""
    sensors.sixthCommodityID = sixthCommodityID ?: ""
    sensors.sceneType = sceneType
    sensors.requestId = requestId

    val provider = SharedPreferencesProvider().getProvider(this)
    provider.putCache(sku, sensors)
}

fun Context.getProduct(sku: String): SensorsOriginVo {
    val provider = SharedPreferencesProvider().getProvider(this)
    var originVo = provider.getCache(sku, SensorsOriginVo::class.java)
    if (originVo == null) {
        originVo = SensorsOriginVo()
        originVo.commodityID = sku
    }
    return originVo
}

fun putProduct(originVo: SensorsOriginVo): JSONObject {
    val properties = JSONObject()
    properties.putOpt("fromModule", originVo.fromModule ?: "")
            .putOpt("fromTag", originVo.fromTag ?: "")
            .putOpt("fromSubTag", originVo.fromSubTag ?: "")
            .putOpt("commodityID", originVo.commodityID ?: "")
            .putOpt("commodityName", originVo.commodityName ?: "")
            .putOpt("originalPrice", originVo.originalPrice)
            .putOpt("preferentialPrice", originVo.preferentialPrice)
            .putOpt("purchaseNum", originVo.purchaseNum)
            .putOpt("commodityBrandID", originVo.commodityBrandID ?: "")
            .putOpt("commodityBrandName", originVo.commodityBrandName ?: "")
            .putOpt("secondCommodityName", originVo.secondCommodityName ?: "")
            .putOpt("secondCommodityID", originVo.secondCommodityID ?: "")
            .putOpt("forthCommodityName", originVo.forthCommodityName ?: "")
            .putOpt("forthCommodityID", originVo.forthCommodityID ?: "")
            .putOpt("sixthCommodityName", originVo.sixthCommodityName ?: "")
    return properties
}

fun Context.trackProduct(eventName: String, sku: String) {
    trackProduct(eventName, getProduct(sku))
}

fun Context.trackProduct(eventName: String, originVo: SensorsOriginVo) {
    AppTrackHelper.onEvent(this, eventName, putProduct(originVo))
}

fun Context.trackPayMethod(paymentMethod: String) {
    val properties = JSONObject()
    properties.put("paymentMethod", paymentMethod)
    AppTrackHelper.onEvent(this, "selectPaymentMethod", properties)
}

fun Context.trackPayOrder(orderId: String, orderType: String, payableAmount: String, paymentMethod: String) {
    val properties = JSONObject()
    properties.put("orderID", orderId)
    properties.put("orderType", orderType)
    properties.put("actualPaymentAmount", try {
        payableAmount.toFloat()
    } catch (e: Exception) {
        0f
    })
    properties.put("paymentMethod", paymentMethod)
    AppTrackHelper.onEvent(this, "payOrder", properties)
}

fun Context.trackPayOrders(sku: String, qty: Int, orderId: String, orderType: String, payableAmount: String, paymentMethod: String) {
    val properties = putProduct(getProduct(sku))

    properties.put("commodityNumber", qty)

    properties.put("orderID", orderId)
    properties.put("orderType", orderType)
    properties.put("totalPriceOfCommodity", try {
        payableAmount.toFloat()
    } catch (e: Exception) {
        0f
    })
    properties.put("paymentMethod", paymentMethod)

    AppTrackHelper.onEvent(this, "payOrderDetail", properties)
}

/**
 * 1.fromType（来源）：领券中心/商品详情页
 * 2.couponValue（优惠券面值）：传优惠券面值
 * 3.couponName（优惠券名称）：传优惠券的名称
 * 4.couponID（券号）：传优惠券的券号
 * 5.promotionID（促销ID）：传优惠券的促销ID
 */
fun Context.trackGetCoupon(fromType: String, couponValue: String?, couponName: String?, couponID: String?, promotionID: String?) {
    val properties = JSONObject()
    properties.putOpt("fromType", fromType)
            .putOpt("couponName", couponName)
            .putOpt("couponValue", couponValue)
            .putOpt("couponID", couponID)
            .putOpt("promotionID", promotionID)
    AppTrackHelper.onEvent(this, "getCoupon", properties)
}

fun Context.trackRecommend(eventName: String, recPlace: String, skuJsonArray: JSONArray?, recSource: String?) {
    val properties = JSONObject()
    properties.put("sku", skuJsonArray)
    properties.put("recSource", recSource)
    properties.put("recPlace", recPlace)
    AppTrackHelper.onEvent(this, eventName, properties)
}

fun Context.trackSearchClick(eventName: String, sku: String, searchType: String, keyword: String,
                             searchFrom: String, searchIndex: Int) {
    val properties = JSONObject()
    properties.put("commodityID", sku)
    properties.put("searchType", searchType)
    properties.put("keyWord", keyword)
    properties.put("searchFrom", searchFrom)
    properties.put("searchIndex", searchIndex)
    AppTrackHelper.onEvent(this, eventName, properties)
}
