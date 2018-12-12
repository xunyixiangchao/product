package com.ichsy.libs.core.comm.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;


/**
 * 类名：AppUtils
 *
 * @author 戴小刚<br/>
 *         实现的主要功能: 创建日期：2014-11-24 修改者，修改日期，修改内容。
 */
public class AppUtils {

    private static final String TAG = AppUtils.class.getSimpleName();

    /**
     * 安装 App
     *
     * @param context
     * @param apkPath
     */
    public static void installApplication(Context context, String apkPath) {
        context.startActivity(getInstallApplicationIntent(context, apkPath));
    }

    /**
     * 获取安装app的intent
     *
     * @param apkPath
     * @return
     */
    public static Intent getInstallApplicationIntent(Context context, String apkPath) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context, getAppPackage(context) + ".fileprovider", new File(apkPath));
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        }
        return installIntent;
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取已安装应用的 uid，-1 表示未安装此应用或程序异常
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     * Created by cafeting on 2017/2/4.
     *
     * @param context 上下文
     * @param uid     已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos) {
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取设备唯一标识（稍微加过密）
     *
     * @param context
     * @return
     */
    public static String getDeviceUuidFactory(Context context) {
        final String PREFS_FILE = "device_id.xml";
        final String PREFS_DEVICE_ID = "device_id";
        UUID deviceUuid = null;
        String uniqueId = "";
        if (deviceUuid == null) {
            synchronized (AppUtils.class) {
                if (deviceUuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uniqueId = id;

                    } else {
                        final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                        String deviceId;
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                deviceUuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf-8"));
                            } else {
                                deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                if (TextUtils.isEmpty(deviceId)) {
                                    deviceId = DeviceUtil.getMac(context);
                                }
                                String tmSerial = "" + ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
                                deviceUuid = new UUID(androidId.hashCode(), ((long) deviceId.hashCode() << 32) | tmSerial.hashCode());
                            }
                            uniqueId = deviceUuid.toString();
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    prefs.edit().putString(PREFS_DEVICE_ID, uniqueId).commit();
                }
            }
        }
        return uniqueId;

    }

    public static void quitApp() {
        System.exit(0);
    }

    /**
     * SDK 版本号
     *
     * @return
     */
    public static String getSdkVersion() {
        String sdk = Build.VERSION.SDK;
        if (TextUtils.isEmpty(sdk)) {
            sdk = "sdk";
        }
        return sdk;
    }

    /**
     * android 版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        String system = Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(system)) {
            system = "os_info";
        }
        return system;
    }

    /**
     * 手机版本号
     *
     * @return
     */
    public static String getModelVersion() {
        String model = Build.MODEL;
        if (TextUtils.isEmpty(model)) {
            model = "model";
        }
        return model;
    }

    /**
     * App版本号
     *
     * @return
     */
    public static String getAppVersion(Context context) {
        String version;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            version = "version";
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取app的versionCode
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();

            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            String versionName = pinfo.versionName;
            int versionCode = pinfo.versionCode;

            return versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getAppPackage(Context context) {
        return context.getPackageName();
//        try {
//            String pkName = this.getPackageName();
//            String versionName = this.getPackageManager().getPackageInfo(
//                    pkName, 0).versionName;
//            int versionCode = this.getPackageManager()
//                    .getPackageInfo(pkName, 0).versionCode;
//            return pkName + "   " + versionName + "  " + versionCode;
//        } catch (Exception e) {
//        }
//        return null;
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    /**
     * 获取屏幕宽高信息
     *
     * @param activity
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenDisplay(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕宽高信息
     *
     * @param context
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenDisplay(Context context) {

        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取屏幕尺寸
     *
     * @return
     */
    public static String getScreen(Activity activity) {
        DisplayMetrics dm = getScreenDisplay(activity);
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        String screen = dm.widthPixels + "x" + dm.heightPixels;
        if (TextUtils.isEmpty(screen)) {
            screen = "screen";
        }
        return screen;
    }

    /**
     * 手机运营商
     *
     * @return
     */
    public static String getOp(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String providersName = null;
        String IMSI = tm.getSubscriberId();
        try {
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                providersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                providersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                providersName = "中国电信";
            } else {
                providersName = "op";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(providersName)) {
            providersName = "op";
        }
        return providersName;
    }

    /**
     * 获取手机号(一般拿不到)
     *
     * @param context
     * @return
     */
    public static String getNativePhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber = null;
        NativePhoneNumber = tm.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * 获取产品名
     *
     * @return
     */
    public static String getProduct() {

        return "huijiayou_android";
    }

    /**
     * 获取网络状态
     *
     * @return
     */
    public static String getNetType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return "net_type";
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {

            return "wifi";

        } else {
            return "cellular";
        }
    }

    /**
     * 操作系统
     *
     * @return
     */
    public static String getOs() {
        return "android";
    }

    /**
     * 魅族的SmartBar
     *
     * @return
     */
    public static boolean hasSmartBar() {
        try {
            // 新型号可用反射调用Build.hasSmartBar()
            Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        // 反射不到Build.hasSmartBar()，则用Build.DEVICE判断
        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }

        return false;
    }

    public static boolean isFlym3() {
        if (Build.DEVICE.equals("mx2") || Build.DEVICE.equals("mx3")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        return false;
    }

    public static boolean isMi3OrMi4OS() {
        return "cancro".equals(Build.DEVICE);
    }

    public static void setConfigValue(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("setting_config", Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();
    }

    public static String getConfigValue(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("setting_config", Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static boolean isAppExist(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = am.getRunningTasks(20);
        int numActivities = 0;
        for (RunningTaskInfo runningTaskInfo : runningTasks) {

            if (runningTaskInfo.topActivity.getPackageName().contains("com.jiayou.qianheshengyun.app")) {
                LogUtils.i(TAG, "发现栈中有本应用activity");
                numActivities += runningTaskInfo.numActivities;
            }
            LogUtils.i(TAG, "runningTaskInfo.topActivity.getClassName()：：" + runningTaskInfo.topActivity.getClassName());
        }
        LogUtils.i(TAG, "numActivities：：" + numActivities);
        return numActivities > 1;
    }

    /**
     * 屏幕是否是亮的
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    protected static boolean isTopActivity(Activity activity, String packageName) {
//        String packageName = "xxxxx";
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            System.out.println("---------------包名-----------" + tasksInfo.get(0).topActivity.getPackageName());
            //应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * 该应用是否正在运行///会有版本问题
//     */
//    @Deprecated
//    public static boolean isTopApp(Context context, String packageName) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
//        if (!tasks.isEmpty()) {
//            ComponentName topActivity = tasks.get(0).topActivity;
//            if (topActivity.getPackageName().equals(packageName)) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 判断最顶层的 Activity是不是需要的
     */
    public static boolean isTopActivity(Context context, String className) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            LogUtils.i(TAG, "topActivity=" + topActivity.getClassName());
            LogUtils.i(TAG, "className=" + className);
            if (topActivity.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断应用前台应用中是否包含顶部activity是否是 className
     *
     * @return
     */
    public static boolean isTopActivityInProscenium(Context context, String className) {
        LogUtils.i(TAG, "className=" + className);
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(2);
        for (RunningTaskInfo runningTaskInfo : tasks) {
            LogUtils.i(TAG, "runningTaskInfo=" + runningTaskInfo.topActivity.getClassName());
            if (runningTaskInfo.topActivity.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取手机ip地址
     *
     * @return
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    // 判断是否安装某个客户端
    public static boolean AHAappInstalledOrNot(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static boolean isFirstRun(Context context) {
        boolean isFirstRun = false;
        String isFirstRunStr = context.getSharedPreferences("configure", Context.MODE_PRIVATE).getString("apkVersion", "");
        isFirstRun = !getAppVersion(context).equals(isFirstRunStr);
        context.getSharedPreferences("configure", Context.MODE_PRIVATE).edit().putString("apkVersion", getAppVersion(context)).commit();
        return isFirstRun;
    }

    public static boolean isExistGuidePage(Context context, String currentGuideVersion) {
        String pageUrl = "yindaoye01_" + currentGuideVersion.replace(".", "_");
        return context.getResources().getIdentifier(pageUrl, "drawable", context.getApplicationInfo().packageName) != 0;
    }

    public static boolean isFirstRunCurrentVersionGuidePage(Context context) {
        String lastGuideVersion = context.getSharedPreferences("configure", Context.MODE_PRIVATE).getString("apkVersion", "");
        String currentGuideVersion = getAppVersion(context);
        if (currentGuideVersion.equals(lastGuideVersion)) {
            return false;
        }
        if (isExistGuidePage(context, currentGuideVersion)) {
            context.getSharedPreferences("configure", Context.MODE_PRIVATE).edit().putString("apkVersion", currentGuideVersion).apply();
            return true;
        }
        context.getSharedPreferences("configure", Context.MODE_PRIVATE).edit().putString("apkVersion", lastGuideVersion).apply();
        return false;
    }

    /**
     * 获取当前context的进程
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
}
