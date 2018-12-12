package com.leyou.library.le_library.model

import android.os.Parcel
import android.os.Parcelable

/**
 * 扫码购的门店对象
 * Created by liuyuhang on 2018/3/1.
 */
data class QrShopVo(
        val shop_id: Int,
        val shop_name: String?,
        val distance_str: String?,
        val shop_target: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(shop_id)
        parcel.writeString(shop_name)
        parcel.writeString(distance_str)
        parcel.writeString(shop_target)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QrShopVo> {
        override fun createFromParcel(parcel: Parcel): QrShopVo {
            return QrShopVo(parcel)
        }

        override fun newArray(size: Int): Array<QrShopVo?> {
            return arrayOfNulls(size)
        }
    }
}
