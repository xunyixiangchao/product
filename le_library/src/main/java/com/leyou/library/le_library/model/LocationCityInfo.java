package com.leyou.library.le_library.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 选择城市
 * Created by liuyuhang on 2017/2/8.
 */

public class LocationCityInfo implements Parcelable {

    public String city_id;
    public String city_name;
    public String index;

    public LocationCityInfo() {
    }

    public LocationCityInfo(String cityName) {
        this.city_name = cityName;
    }

    public LocationCityInfo(String city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city_id);
        dest.writeString(this.city_name);
        dest.writeString(this.index);
    }

    protected LocationCityInfo(Parcel in) {
        this.city_id = in.readString();
        this.city_name = in.readString();
        this.index = in.readString();
    }

    public static final Creator<LocationCityInfo> CREATOR = new Creator<LocationCityInfo>() {
        @Override
        public LocationCityInfo createFromParcel(Parcel source) {
            return new LocationCityInfo(source);
        }

        @Override
        public LocationCityInfo[] newArray(int size) {
            return new LocationCityInfo[size];
        }
    };
}
