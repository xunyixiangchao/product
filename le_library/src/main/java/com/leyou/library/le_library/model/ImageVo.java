package com.leyou.library.le_library.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片对象
 * Created by liuyuhang on 2017/2/9.
 */

public class ImageVo implements Parcelable {
    public String url;
    public String desc;

    public String link;//点击的链接

    public int width;//图片宽度
    public int height;//图片高度
    public int high;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.desc);
        dest.writeString(this.link);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.high);
    }

    public ImageVo() {
    }

    protected ImageVo(Parcel in) {
        this.url = in.readString();
        this.desc = in.readString();
        this.link = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.high = in.readInt();
    }

    public static final Creator<ImageVo> CREATOR = new Creator<ImageVo>() {
        @Override
        public ImageVo createFromParcel(Parcel source) {
            return new ImageVo(source);
        }

        @Override
        public ImageVo[] newArray(int size) {
            return new ImageVo[size];
        }
    };
}
