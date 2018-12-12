package com.ichsy.libs.core.comm.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 运行时权限管理类
 * Created by liuyuhang on 2016/12/19.
 */

public class PermissionManager {
    //PackageManager.PERMISSION_GRANTED
    //PackageManager.PERMISSION_DENIED
    private static PermissionManager instance;

    private HashMap<String, PermissionRequestObject> requestPermissionQueue;

    public interface PermissionHandler {
        /**
         * 用户通过了所有申请权限
         */
        void onAllPermissionGranted();

        /**
         * 用户有拒绝部分权限
         *
         * @param permission 被拒绝的权限
         */
        void onPermissionDenied(String[] permission);

        /**
         * 所有权限已经都永久拒绝
         */
        void onAllPermissionDenied();

//        /**
//         * 用户有拒绝部分权限
//         */
//        void onPermissionDenied();
//        /**
//         * @param shouldShowRequestPermissionRationale true:表明用户没有彻底禁止弹出权限请求
//         *                                             false:表明用户已经彻底禁止弹出权限请求
//         * @param permission
//         */
//        void onShowRequestPermissionRationale(boolean shouldShowRequestPermissionRationale, String permission);
    }

    public class PermissionRequestObject {
        public String activityClassName;
        public String permission;
        public PermissionHandler handler;
    }

    private PermissionManager() {
        requestPermissionQueue = new HashMap<>();
    }

    public static PermissionManager getInstance() {
        if (null == instance) {
            instance = new PermissionManager();
        }
        return instance;
    }

    public static String[] getDefaultPermission() {
        return new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    /**
     * 检查权限是否通过
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < 23) {
            //M以下版本直接成功
            return true;
        } else {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * 批量检查权限是否申请通过，如果有一个未授权，就返回false
     *
     * @param context
     * @param permissions
     * @return 返回未授权的权限
     */
    public static String[] checkPermission(Context context, String[] permissions) {
        List<String> deniedPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[0]);
    }

    /**
     * 权限是否被拒绝
     *
     * @param result
     * @return
     */
    public static boolean isDenied(int result) {
        return result == PackageManager.PERMISSION_DENIED;
    }

    /**
     * 检查权限是否被永久拒绝
     *
     * @return true:表明用户没有彻底禁止弹出权限请求 false:表明用户已经彻底禁止弹出权限请求
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public static void requestPermission(Context context, String[] permission) {
        requestPermission(context, permission, 0);
    }

    public static void requestPermission(Context context, String[] permission, int requestCode) {
        if (context instanceof Activity) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
//            }

            ActivityCompat.requestPermissions((Activity) context, permission, requestCode);
        }
    }

    /**
     * 检测并且发送权限申请，如果权限已经申请通过，不执行任何方法
     *
     * @param activity
     * @param permissions
     */
    public static void checkAndRequestPermission(Activity activity, String[] permissions) {
        if (0 != checkPermission(activity, permissions).length) {
            requestPermission(activity, permissions);
        }
    }

//    public void findPermission(final Activity activity, final String[] permissions, String permission, int[] grantResults) {
//        for (int i = 0; i < permissions.length; i++) {
//            if (permission.equals(permissions[i])) {
//                if (PermissionManager.isDenied(grantResults[i])) {
//                    boolean shouldShow = PermissionManager.shouldShowRequestPermissionRationale(activity, permissions[i]);
//                    if (shouldShow) {
//                        AlertDialog.Builder dialog = DialogBuilder.buildAlertDialog(activity, "提示", "请允许我们获得您的电话权限，拒绝后将无法正常支付，客官请三思。");
//                        dialog.setPositiveButton("去允许", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                PermissionManager.requestPermission(activity, permissions);
//                            }
//                        });
//
//                        dialog.setNegativeButton("取消", null);
//                        dialog.show();
//                    } else {
//                        AlertDialog.Builder dialog = DialogBuilder.buildAlertDialog(activity, "提示", "获取电话权限失败，无法正常支付，您可前往应用权限设置中打开权限");
//                        dialog.setNegativeButton("我知道了", null);
//                        dialog.show();
//                    }
//
//                } else {
//                    requestPayInfo("unionpay");
//                }
//            }
//        }
//    }

    /**
     * 一条龙式权限申请处理
     *
     * @param activity
     * @param permissions
     * @param permissionHandler
     */
    public void checkAndRequestPermission(Activity activity, String[] permissions, PermissionHandler permissionHandler) {

        String[] deniedPermissions = checkPermission(activity, permissions);
        if (0 == deniedPermissions.length) {
            permissionHandler.onAllPermissionGranted();
        } else {
            requestPermission(activity, deniedPermissions);

            PermissionRequestObject permissionRequestObject = new PermissionRequestObject();
//            permissionRequestObject.activityClassName = activity.getClass().getName();
            permissionRequestObject.handler = permissionHandler;
//            requestPermissionQueue.add(permissionRequestObject);
            requestPermissionQueue.put(activity.getClass().getName(), permissionRequestObject);
        }

    }


    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        PermissionRequestObject permissionRequestObject;
        if (null == requestPermissionQueue || null == (permissionRequestObject = requestPermissionQueue.get(activity.getClass().getName()))) {
            return;
        }

        final List<String> deniedPermission = new ArrayList<>();
        int tipsCount = 0;
        for (int i = 0; i < grantResults.length; i++) {
            if (isDenied(grantResults[i])) {
                boolean isTip = PermissionManager.shouldShowRequestPermissionRationale(activity, permissions[i]);

                if (isTip) {//表明用户没有彻底禁止弹出权限请求
                    deniedPermission.add(permissions[i]);
//                    PermissionManager.requestPermission(getActivity(), permissions);
                } else {//表明用户已经彻底禁止弹出权限请求
                    //   PermissionMonitorService.start(this);//这里一般会提示用户进入权限设置界面
                    tipsCount++;
                }
            }
        }

        final int size = deniedPermission.size();
        if (size > 0 && tipsCount != permissions.length) {
            permissionRequestObject.handler.onPermissionDenied(deniedPermission.toArray(new String[size]));
        } else if (size > 0 && tipsCount == permissions.length) {
            permissionRequestObject.handler.onAllPermissionDenied();
        } else {
            permissionRequestObject.handler.onAllPermissionGranted();
        }
        requestPermissionQueue.remove(activity.getClass().getName());
    }


}
