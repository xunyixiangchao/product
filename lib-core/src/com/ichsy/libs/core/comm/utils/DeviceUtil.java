package com.ichsy.libs.core.comm.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ichsy.libs.core.comm.permission.PermissionManager;
import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

/**
 * 获取设备信息的工具类
 *
 * @author LiuYuHang Date: 2015年5月14日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.public_libs.utils
 * @File DeviceUtil.java
 */
public class DeviceUtil {
    public static final String CACHE_DEVICE_KEY = "CACHE_DEVICE_KEY";
    private static int windows_size_height = 0, windows_size_width = 0;

    /**
     * 打开或者关闭输入法
     *
     * @param context
     * @param focusView 焦点View
     * @param hide      是否显示
     * @author LiuYuHang
     * @date 2014年9月28日
     */
    public static void hidKeyBoard(Context context, final View focusView, boolean hide) {
        if (focusView == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (hide) {
            // 强制隐藏键盘
            if (imm != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        } else {
//            focusView.requestFocus();
//            imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);

            if (focusView instanceof EditText) {
                ((EditText) focusView).setSelection(0, ((EditText) focusView).length());
            }

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.showSoftInput(focusView, 0);
                    }
                }
            }, 200);


        }
    }

    /**
     * 获取运营商名字
     */
    @NonNull
    public static String getOperatorName(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String operator = getOperator(context);
        if (operator != null) {
            switch (operator) {
                case "46000":
                case "46002":
                    // operatorName="中国移动";
                    return "中国移动";
//                signalTextView.setText("中国移动");
                // Toast.makeText(this, "此卡属于(中国移动)",
                // Toast.LENGTH_SHORT).show();
                case "46001":
                    // operatorName="中国联通";
//                signalTextView.setText("中国联通");
                    return "中国联通";
                // Toast.makeText(this, "此卡属于(中国联通)",
                // Toast.LENGTH_SHORT).show();
                case "46003":
                    // operatorName="中国电信";
//                signalTextView.setText("中国电信");
                    return "中国电信";
                // Toast.makeText(this, "此卡属于(中国电信)",
                // Toast.LENGTH_SHORT).show();
            }
        }
        return "UnKnown";
    }

    /**
     * 获取运营商code
     *
     * @return
     */
    @Nullable
    public static String getOperator(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyManager) {
            return null;
        }
        return telephonyManager.getSimOperator();

//        String operator = telephonyManager.getSimOperator();
//        if (operator != null) {
//            if (operator.equals("46000") || operator.equals("46002")) {
//                // operatorName="中国移动";
//                return "中国移动";
////                signalTextView.setText("中国移动");
//                // Toast.makeText(this, "此卡属于(中国移动)",
//                // Toast.LENGTH_SHORT).show();
//            } else if (operator.equals("46001")) {
//                // operatorName="中国联通";
////                signalTextView.setText("中国联通");
//                return "中国联通";
//                // Toast.makeText(this, "此卡属于(中国联通)",
//                // Toast.LENGTH_SHORT).show();
//            } else if (operator.equals("46003")) {
//                // operatorName="中国电信";
////                signalTextView.setText("中国电信");
//                return "中国电信";
//                // Toast.makeText(this, "此卡属于(中国电信)",
//                // Toast.LENGTH_SHORT).show();
//            }
//        }
//        return "UnKnown";
    }


    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNumber
     */
    public static void call(final Context context, final String phoneNumber) {
        AlertDialog.Builder builder = DialogBuilder.buildAlertDialog(context, "", "是否拨打电话 " + phoneNumber);

        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNumber);
                intent.setData(data);
                context.startActivity(intent);
            }
        });

        builder.show();
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        if (windows_size_height == 0) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager(context).getDefaultDisplay().getMetrics(dm);
            windows_size_height = getDisplayMetrics(context).heightPixels;
        }
        return windows_size_height;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        if (windows_size_width == 0) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager(context).getDefaultDisplay().getMetrics(dm);
            windows_size_width = getDisplayMetrics(context).widthPixels;
        }
        return windows_size_width;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager(context).getDefaultDisplay().getMetrics(dm);
        return dm;

    }

    public static WindowManager getWindowManager(Context context) {
        return ((WindowManager) context.getSystemService(Activity.WINDOW_SERVICE));
    }

    public static int getDeviceVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获得状态栏高度
     */
    public static int getStateBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获得底部NavigationBar高度
     */
    public static int getNavigationBarHeight(Context context) {
        int height = 0;
        try {
            Resources resources = context.getResources();
            int resourcesId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourcesId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 通过class名找到activity完整路径
     *
     * @return Modifier： Modified Date： Modify：
     */
    public static String getActivityByClassName(Context context, String className) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo.activities != null) {
                for (ActivityInfo ai : packageInfo.activities) {
                    if (ai.name.endsWith(className)) {
                        return ai.name;
                    }
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 获取手机mac地址
     *
     * @param context
     * @return
     */

    public static String getMac(Context context) {
        String macAddress = "";
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress()))
                    macAddress = info.getMacAddress().replace(":", "");
            }
        } catch (Exception e) {
            macAddress = "mac";
            e.printStackTrace();
        }
        return macAddress;
    }

    @Nullable
    public static String getIMEI(Context context) {
        if (PermissionManager.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } else {
            return null;
        }
    }

    public static String getDeviceId(Context context) {
        String deviceId = getIMEI(context);
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
//        deviceId = getMac(context);
//        if (!TextUtils.isEmpty(deviceId)) {
//            return deviceId;
//        }

        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        deviceId = provider.getCache(CACHE_DEVICE_KEY);
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        } else {
            deviceId = "LE" + System.currentTimeMillis() + new Random().nextInt(10);
            provider.putCache(CACHE_DEVICE_KEY, deviceId);
            return deviceId;
        }
    }

    public static String getIp(Context context) {
        return getIPAddress(context);
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ip = wifiInfo.getIpAddress();
//                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return (ip & 0xFF) + "." +
                        ((ip >> 8) & 0xFF) + "." +
                        ((ip >> 16) & 0xFF) + "." +
                        (ip >> 24 & 0xFF);
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

//    private String intToIp(int i) {
//        return (i & 0xFF) + "." +
//                ((i >> 8) & 0xFF) + "." +
//                ((i >> 16) & 0xFF) + "." +
//                (i >> 24 & 0xFF);
//    }


    public static String getModel() {
        return android.os.Build.MODEL;
    }

    public static String getChannel(Context context) {
        return "1";
    }

    /**
     * 获取channelid，会先从sd卡查找，如果找不到，会从配置文件中查找，并且保存到sd卡
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context, String path) {
//        String channelFilePath = FileUtil.getSdCardPath() + "/" + path;
//        String channelFileName = "channel.txt";
        String channel;

//        if (FileUtil.isSdCardExist()) {
//            channel = FileUtil.readByBufferReader(channelFilePath, channelFileName);
//        }

//        if (TextUtils.isEmpty(channel)) {

        channel = ChannelUtil.getChannel(context, "developer");

//            FileUtil.writeByBufferWriter(channelFilePath, channelFileName, channel, false);
//        }
        return channel;
    }
}
