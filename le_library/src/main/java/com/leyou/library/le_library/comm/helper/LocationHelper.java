package com.leyou.library.le_library.comm.helper;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.CoordType;
import com.ichsy.libs.core.comm.bus.BusManager;
import com.ichsy.libs.core.comm.utils.LogUtils;

import java.util.List;

/**
 * 地理位置Helper类
 * Created by liuyuhang on 16/6/8.
 */
public class LocationHelper {
    private static final int LOCATION_DELAY_TIME = 3 * 60 * 1000;//定位间隔
    private static LocationHelper instance;

    public static final String EVENT_LOCATION_CHANGED = "EVENT_LOCATION_CHANGED";

    public LocationVo locationObject;

    public LocationClient mLocationClient = null;


    public static class LocationVo {
        public double latitude;// 纬度
        public double longitude;// 经度

        public String province;
        public String city;
        public String district;
        public String street;

        @Override
        public String toString() {
            return "LocationVo{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", street='" + street + '\'' +
                    '}';
        }
    }

    private LocationHelper() {
    }

    public static LocationHelper getInstance() {
        synchronized (LocationHelper.class) {
            if (null == instance) {
                instance = new LocationHelper();

                instance.locationObject = new LocationVo();
            }
            return instance;
        }
    }

    public void init(Context context) {
        mLocationClient = new LocationClient(context.getApplicationContext());     //声明LocationClient类

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(LOCATION_DELAY_TIME);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                //Receive Location
                StringBuilder sb = new StringBuilder(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nmessage: ");
                sb.append(getErrorString(location.getLocType()));
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                // GPS定位结果
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    sb.append("\nspeed : ");
                    // 单位：公里每小时
                    sb.append(location.getSpeed());
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    // 单位：米
                    sb.append(location.getAltitude());
                    sb.append("\ndirection : ");
                    // 单位度
                    sb.append(location.getDirection());
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    //运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                sb.append("\nlocationdescribe : ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                List<Poi> list = location.getPoiList();// POI数据
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId()).append(" ").append(p.getName()).append(" ").append(p.getRank());
                    }
                }

                sb.append("\ngetProvince = ").append(location.getProvince());
                sb.append("\ngetCity = ").append(location.getCity());
                sb.append("\ngetStreet = ").append(location.getStreet());

//                if (null == locationObject) {
//                    locationObject = new LocationVo();
//                }

                locationObject.latitude = location.getLatitude();
                locationObject.longitude = location.getLongitude();

                if (locationObject.latitude == 4.9E-324) {
                    locationObject.latitude = 0.0;
                }

                if (locationObject.longitude == 4.9E-324) {
                    locationObject.longitude = 0.0;
                }

                locationObject.province = location.getProvince();
                locationObject.city = location.getCity();
                locationObject.district = location.getDistrict();
                locationObject.street = location.getStreet();

                BusManager.getInstance().postEvent(EVENT_LOCATION_CHANGED, locationObject);

                LogUtils.i("BaiduLocationApiDem", sb.toString());

            }
        });//注册监听函数
        mLocationClient.start();
        updateLocation(context);
    }

    public static String getErrorString(int errorCode) { // BDLocation.getLocType()
        if (errorCode == 61) {
            return "GPS定位结果";
        } else if (errorCode == 62) {
            return "扫描整合定位依据失败。此时定位结果无效。";
        } else if (errorCode == 63) {
            return "网络异常，没有成功向服务器发起请求。此时定位结果无效。";
        } else if (errorCode == 65) {
            return "定位缓存的结果。";
        } else if (errorCode == 66) {
            return "离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果";
        } else if (errorCode == 67) {
            return "离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果";
        } else if (errorCode == 68) {
            return "网络连接失败时，查找本地离线定位时对应的返回结果";
        } else if (errorCode == 161) {
            return "表示网络定位结果";
        } else if (errorCode >= 162 || errorCode <= 167) {
            return "服务端定位失败。";
        }
        return "其它错误";
    }

    /**
     * 更新位置信息
     */
    public void updateLocation(Context context) {
        if (null == mLocationClient) {
            init(context);
        }
        mLocationClient.requestLocation();
    }

    public void stop() {
        if (null != mLocationClient) {
            mLocationClient.stop();
        }
    }


    ///

//    static double pi = 3.14159265358979324;
//    static double a = 6378245.0;
//    static double ee = 0.00669342162296594323;
//    public final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
//
//    public static double[] wgs2bd(double lat, double lon) {
//        double[] wgs2gcj = wgs2gcj(lat, lon);
//        double[] gcj2bd = gcj2bd(wgs2gcj[0], wgs2gcj[1]);
//        return gcj2bd;
//    }
//
//    public static double[] gcj2bd(double lat, double lon) {
//        double x = lon, y = lat;
//        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
//        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
//        double bd_lon = z * Math.cos(theta) + 0.0065;
//        double bd_lat = z * Math.sin(theta) + 0.006;
//        return new double[]{bd_lat, bd_lon};
//    }
//
//    public static double[] bd2gcj(double lat, double lon) {
//        double x = lon - 0.0065, y = lat - 0.006;
//        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
//        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
//        double gg_lon = z * Math.cos(theta);
//        double gg_lat = z * Math.sin(theta);
//        return new double[]{gg_lat, gg_lon};
//    }
//
//    public static double[] wgs2gcj(double lat, double lon) {
//        double dLat = transformLat(lon - 105.0, lat - 35.0);
//        double dLon = transformLon(lon - 105.0, lat - 35.0);
//        double radLat = lat / 180.0 * pi;
//        double magic = Math.sin(radLat);
//        magic = 1 - ee * magic * magic;
//        double sqrtMagic = Math.sqrt(magic);
//        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
//        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
//        double mgLat = lat + dLat;
//        double mgLon = lon + dLon;
//        double[] loc = {mgLat, mgLon};
//        return loc;
//    }
//
//    private static double transformLat(double lat, double lon) {
//        double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
//        ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
//        ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
//        ret += (160.0 * Math.sin(lon / 12.0 * pi) + 320 * Math.sin(lon * pi / 30.0)) * 2.0 / 3.0;
//        return ret;
//    }
//
//    private static double transformLon(double lat, double lon) {
//        double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat));
//        ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
//        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
//        ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0;
//        return ret;
//    }
}
