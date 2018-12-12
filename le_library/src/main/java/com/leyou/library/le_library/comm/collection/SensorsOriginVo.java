package com.leyou.library.le_library.comm.collection;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 带有页面来源的商品
 * <p>
 * Created by ss on 2018/3/27.
 */

public class SensorsOriginVo implements Parcelable {

    public SensorsOriginVo() {

    }

    public SensorsOriginVo(String fromModule) {
        this(fromModule, "");
    }

    public SensorsOriginVo(String fromModule, String fromTag) {
        this(fromModule, fromTag, "");
    }

    public SensorsOriginVo(String fromModule, String fromTag, String fromSubTag) {
        this.fromModule = fromModule;
        this.fromTag = fromTag;
        this.fromSubTag = fromSubTag;
    }

    public String fromModule; // 模块来源 搜索，分类，活动页，购物车，商品推荐（首页），商品详情页商品推荐，订单详情，其他 搜索-促销ID
    public String fromTag; // 来源一级标签 搜索词，一级分类，活动名称（链接）,促销id
    public String fromSubTag;// 二级分类

    public String commodityID;//	商品ID	字符串
    public String commodityName;//	商品名称	字符串

    public float originalPrice;//	商品原价	数值
    public float preferentialPrice;//	商品优惠价	数值

    public int purchaseNum;//	已购买人数	数值

    //品牌ID commodityBrandID
    public String commodityBrandID;
    //品牌名称 commodityBrandName
    public String commodityBrandName;
    //商品二级分类名称：
    public String secondCommodityName;
    //商品二级分类ID：
    public String secondCommodityID;
    //商品四级分类名称：
    public String forthCommodityName;
    //商品四级分类ID：
    public String forthCommodityID;
    //商品六级分类名称：
    public String sixthCommodityName;
    //商品六级分类ID:
    public String sixthCommodityID;

    public String sceneType;
    public String requestId;

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fromModule);
        dest.writeString(this.fromTag);
        dest.writeString(this.fromSubTag);
        dest.writeString(this.commodityID);
        dest.writeString(this.commodityName);
        dest.writeFloat(this.originalPrice);
        dest.writeFloat(this.preferentialPrice);
        dest.writeInt(this.purchaseNum);
        dest.writeString(this.commodityBrandID);
        dest.writeString(this.commodityBrandName);
        dest.writeString(this.secondCommodityName);
        dest.writeString(this.secondCommodityID);
        dest.writeString(this.forthCommodityName);
        dest.writeString(this.forthCommodityID);
        dest.writeString(this.sixthCommodityName);
        dest.writeString(this.sixthCommodityID);
        dest.writeString(this.sceneType);
        dest.writeString(this.requestId);
    }

    protected SensorsOriginVo(Parcel in) {
        this.fromModule = in.readString();
        this.fromTag = in.readString();
        this.fromSubTag = in.readString();
        this.commodityID = in.readString();
        this.commodityName = in.readString();
        this.originalPrice = in.readFloat();
        this.preferentialPrice = in.readFloat();
        this.purchaseNum = in.readInt();
        this.commodityBrandID = in.readString();
        this.commodityBrandName = in.readString();
        this.secondCommodityName = in.readString();
        this.secondCommodityID = in.readString();
        this.forthCommodityName = in.readString();
        this.forthCommodityID = in.readString();
        this.sixthCommodityName = in.readString();
        this.sixthCommodityID = in.readString();
        this.sceneType = in.readString();
        this.requestId = in.readString();
    }

    public static final Parcelable.Creator<SensorsOriginVo> CREATOR = new Parcelable.Creator<SensorsOriginVo>() {
        @Override
        public SensorsOriginVo createFromParcel(Parcel source) {return new SensorsOriginVo(source);}

        @Override
        public SensorsOriginVo[] newArray(int size) {return new SensorsOriginVo[size];}
    };
}
