package com.ichsy.libs.core.comm.update;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.bus.BusEventObserver;
import com.ichsy.libs.core.comm.permission.PermissionManager;
import com.ichsy.libs.core.comm.utils.AppUtils;
import com.ichsy.libs.core.comm.utils.DialogBuilder;
import com.ichsy.libs.core.comm.utils.FileUtil;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.NetWorkUtils;
import com.ichsy.libs.core.comm.utils.NotifyHelper;
import com.ichsy.libs.core.comm.utils.ToastUtils;
import com.ichsy.libs.core.comm.view.dialog.UpdateDialog;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * 检测更新的工具类，负责执行检测更新
 * <p/>
 * Created by liuyuhang on 16/4/29.
 */
public class UpdateHelper {
    private static UpdateHelper instance;
    private final String UPDATE_DOWNLOAD_STATUS_KEY = "update_files_download_status_version_";
    private final String DOWNLOAD_STATUS_OK = "download_ok";
    private final String DOWNLOAD_STATUS_NOT_OK = "download_not_ok";
    private boolean mSilentDownload = false;
    private UpdateVo mUpdateVo;

    private String mBasePath;

    private BusEventObserver busEventObserver;

    private UpdateHelper() {
    }

    public static UpdateHelper getInstance() {
        if (null == instance) {
            instance = new UpdateHelper();
        }
        return instance;
    }

    /**
     * 是否打开静默下载apk，如果打开，会在wifi下自动下载apk
     *
     * @param silent
     */
    public void setAutoDownloadAPK(boolean silent) {
        mSilentDownload = silent;
    }

    /**
     * 执行update操作
     *
     * @param activity
     * @param operator 操作update的抽象类
     * @param listener update的监听类
     */
    public void update(final Activity activity, UpdateOperator operator, final UpdateListener listener) {
        if (null == operator) {
            throw new NullPointerException("operator must be not null");
        }
        UpdateOperation operation = new UpdateOperation(activity, operator, listener);
        operation.execute();
    }

    public void setBasePath(String path) {
        mBasePath = path;
    }

    /**
     * 执行检测更新的逻辑
     *
     * @param activity
     * @param listener
     */
    private void invokeUpdate(Activity activity, UpdateListener listener) {
        switch (mUpdateVo.updateStatus) {
            case FREE:
                if (checkAPK(activity)) {
                    showUpdateDialog(activity, listener);
                } else {
                    listener.onUpdateSkip();
                    autoSilentDownload(activity, null);
                }
                break;
            case UPDATE:
                showUpdateDialog(activity, listener);
                break;
            case FORCE:
                showUpdateDialog(activity, listener);
                break;
            case SILENCE:
            case NONE:
                listener.onUpdateSkip();
                break;
        }
    }

    /**
     * 显示检测更新dialog
     *
     * @param activity
     * @param listener
     */
    public void showUpdateDialog(final Activity activity, final UpdateListener listener) {
        String confirmText;

        if (mUpdateVo.updateStatus == UpdateVo.UpdateStatus.FREE) {
            confirmText = "免流量更新";
        } else {
            confirmText = "马上更新";
        }

        /*

        new LeDialog(context).show(new LeDialog.UiBuilder() {
            @Override
            public View onViewCreate(LayoutInflater inflater) {
                return inflater.inflate(com.ichsy.core_library.R.layout.frame_dialog_update_layout, null);
            }

            @Override
            public void onViewDraw(final Dialog dialog, View view) {
                ((TextView) view.findViewById(com.ichsy.core_library.R.id.tv_title)).setText(mUpdateVo.title);
                ((TextView) view.findViewById(com.ichsy.core_library.R.id.tv_content)).setText(mUpdateVo.description);
                ((TextView) view.findViewById(com.ichsy.core_library.R.id.tv_confirm)).setText(confirmText);

                view.findViewById(R.id.iv_cancel).setVisibility(mUpdateVo.updateStatus == UpdateVo.UpdateStatus.FORCE
                        ? View.GONE : View.VISIBLE);

                ViewHelper.get(context).id(com.ichsy.core_library.R.id.iv_cancel).listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        listener.onUpdateSkip();
                    }
                });
                ViewHelper.get(context).id(com.ichsy.core_library.R.id.tv_confirm).listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (checkAPK(context)) {
                            listener.onUpdateCompleteAndInstall(UpdateDownloadProvider.getDownloadFile(mBasePath, mUpdateVo).getPath());
                        } else {
                            downloadUpdateFiles(context, false, listener);
                        }
                    }
                });
            }
        });

        */

        UpdateDialog.Builder builder = new UpdateDialog.Builder(activity);
        builder.setForce(mUpdateVo.updateStatus == UpdateVo.UpdateStatus.FORCE)
                .setMessage(mUpdateVo.description).setTitle(mUpdateVo.title)
                .setConfirmButton(confirmText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (v.getId() == R.id.iv_cancel) {
                            listener.onUpdateSkip();
                        } else if (v.getId() == R.id.tv_confirm) {
                            //权限SD卡权限
                            boolean isGranted = PermissionManager.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

                            if (isGranted) {
//                                if (checkAPK(activity)) {
//                                    listener.onUpdateCompleteAndInstall(UpdateDownloadProvider.getDownloadFile(mBasePath, mUpdateVo).getPath());
//                                } else {
                                downloadUpdateFiles(activity, false, listener);
//                                }
                            } else {
                                AlertDialog.Builder dialog = DialogBuilder.buildAlertDialog(activity, "权限错误", "获取SD卡权限失败，无法下载更新文件");
                                dialog.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AppUtils.quitApp();
                                    }
                                });
                                dialog.show();
                            }


                        }
                    }
                });
        builder.create().show();

//        //需要更新，选择更新模式
//        AlertDialog.Builder builder = DialogBuilder.buildAlertDialog(context, mUpdateVo.title, mUpdateVo.description);
//
//        if (mUpdateVo.updateStatus == UpdateVo.UpdateStatus.FREE) {
//            builder.setNegativeButton("免流量安装", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    listener.onUpdateCompleteAndInstall(UpdateDownloadProvider.getDownloadFile(mBasePath, mUpdateVo).getPath());
//                }
//            });
//        } else {
//            builder.setNegativeButton("马上更新", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (checkAPK(context)) {
//                        listener.onUpdateCompleteAndInstall(UpdateDownloadProvider.getDownloadFile(mBasePath, mUpdateVo).getPath());
//                    } else {
//                        downloadUpdateFiles(context, false, listener);
//                    }
//                }
//            });
//        }
//
//        if (mUpdateVo.updateStatus != UpdateVo.UpdateStatus.FORCE) {
//            builder.setPositiveButton("下次再说", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    listener.onUpdateSkip();
//                }
//            });
//        }
//        builder.show();
    }

    public interface UpgradeInterface {
        void onUpgradeStart();
    }

    /**
     * 下载更新文件
     *
     * @param activity
     * @param silent   是否显示进度条
     * @param listener
     */
    public void downloadUpdateFiles(final Activity activity, final boolean silent, final UpdateListener listener) {
//        LogUtils.i("update", "downloadUpdateFiles...");
//        final ProgressDialog downloadProgressDialog = DialogBuilder.buildProgressDialog(activity, "正在下载更新\n" + mUpdateVo.description, 0);
//
//        if (null == busEventObserver) {
//            busEventObserver = new BusEventObserver() {
//                @Override
//                public void onBusEvent(String event, Object message) {
//                    DownloaderService.UpdateDownloadStatus updateDownloadStatus = (DownloaderService.UpdateDownloadStatus) message;
//
//                    if (updateDownloadStatus.status == DownloaderService.EVENT_UPDATE_DOWNLOAD_START) {
//                        if (isNotifyProgressDialog()) {
//                            //如果是普通更新，跳过页面在后台更新
//                            listener.onUpdateSkip();
//                        } else {
//                            if (!silent && null != downloadProgressDialog) {
//                                downloadProgressDialog.show();
//                            }
//                        }
//
//                    } else if (updateDownloadStatus.status == DownloaderService.EVENT_UPDATE_DOWNLOAD_PROGRESS) {
//                        LogUtils.v("update", "download total-->>>" + updateDownloadStatus.max + "  current-->>>"
//                                + updateDownloadStatus.current + " (progress: " + updateDownloadStatus.progress + "%)");
//
//                        if (isNotifyProgressDialog()) {
////                            int pro = (int) (((float) updateDownloadStatus.current / (float) updateDownloadStatus.max) * 100);
////                        NotifyHelper.notify(context, R.drawable.ic_launcher, "下载更新", "正在下载: " + pro, new Intent());
//
//                            PendingIntent emptyIntent = NotifyHelper.getDefaultIntent(activity, new Intent());
//                            NotifyHelper.notify(activity, mUpdateVo.iconResId, "下载更新", "正在下载: " + updateDownloadStatus.progress + "%", null, false, emptyIntent);
//
////                            //获取状态通知栏管理
////                            NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Activity.NOTIFICATION_SERVICE);
////                            //实例化通知栏构造器NotificationCompat.Builder
////                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity);
////                            //对Builder进行配置
////                            mBuilder.setContentTitle("下载更新") //设置通知栏标题
////                                    .setContentText("正在下载: " + updateDownloadStatus.progress + "%") //设置通知栏显示内容
//////                .setContentIntent(getDefaultIntent(context, intent, Notification.FLAG_UPDATE_CURRENT)) //设置通知栏点击意图
////                                    .setContentIntent(NotifyHelper.getDefaultIntent(activity, new Intent())) //设置通知栏点击意图
//////                .setNumber(2) //设置通知集合的数量
//////                                .setTicker(content) //通知首次出现在通知栏，带上升动画效果的
////                                    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
////                                    .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
////                                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
////                                    .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//////                                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
////                                    //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
////                                    .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
////                            mNotificationManager.notify(NotifyHelper.notification_request_code, mBuilder.build());
//
//                        } else {
//                            if (null != downloadProgressDialog) {
//                                downloadProgressDialog.setMax(updateDownloadStatus.max);
//                                downloadProgressDialog.setProgress(updateDownloadStatus.current);
//                            }
//                        }
//
//                    } else if (updateDownloadStatus.status == DownloaderService.EVENT_UPDATE_DOWNLOAD_SUCCESS) {
//                        BusManager.getInstance().unRegister(DownloaderService.EVENT_UPDATE_STATUS, busEventObserver);
//
//                        setAPKDownloadStatus(activity, DOWNLOAD_STATUS_OK);
//
//                        if (isNotifyProgressDialog()) {
//                            /**
//                             * *******
//                             * 下载完成，点击安装**********
//                             **/
////                            Uri uri = Uri.fromFile(new File(updateDownloadStatus.filePath));
////                            Intent intent = new Intent(Intent.ACTION_VIEW);
////                            intent.setDataAndType(uri, "application/vnd.android.package-archive");
////                        pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);
//
////                            PendingIntent defaultIntent = NotifyHelper.getDefaultIntent(activity, AppUtils.getInstallApplicationIntent(activity, updateDownloadStatus.filePath));
////                            NotifyHelper.notify(activity, mUpdateVo.iconResId, "下载完成", "更新包下载完成，请点击安装", defaultIntent);
//                            NotifyHelper.notifyClear(activity);
//                            AppUtils.installApplication(activity, updateDownloadStatus.filePath);
//                        } else {
//                            if (null != downloadProgressDialog) {
//                                downloadProgressDialog.dismiss();
//                            }
//                            if (null != listener) {
//                                listener.onUpdateCompleteAndInstall(updateDownloadStatus.filePath);
//                            }
//                        }
//
//
//                    } else if (updateDownloadStatus.status == DownloaderService.EVENT_UPDATE_DOWNLOAD_FAILED) {
//                        BusManager.getInstance().unRegister(DownloaderService.EVENT_UPDATE_STATUS, busEventObserver);
//
//                        setAPKDownloadStatus(activity, DOWNLOAD_STATUS_NOT_OK);
//                        LogUtils.i("update", "error: " + updateDownloadStatus.message);
//
//                        if (isNotifyProgressDialog()) {
//                            NotifyHelper.notify(activity, mUpdateVo.iconResId, "下载失败", "下载更新文件失败", new Intent());
//                        } else {
//                            if (null != downloadProgressDialog && downloadProgressDialog.isShowing()) {
//                                downloadProgressDialog.dismiss();
//                            }
//                            AlertDialog.Builder dialog = DialogBuilder.buildAlertDialog(activity, "错误", "网络不佳，请重试");
//                            dialog.setPositiveButton("重试", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    downloadUpdateFiles(activity, silent, listener);
//                                }
//                            });
//                            dialog.setNegativeButton("退出", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    AppUtils.quitApp();
//                                }
//                            });
//                            dialog.show();
//                        }
//
//                    }
//                }
//            };
//        }
////
//        Intent intent = new Intent(activity, DownloaderService.class);
//        intent.putExtra(DownloaderService.INTENT_UPDATE_OBJECT, mUpdateVo);
//        intent.putExtra(DownloaderService.INTENT_UPDATE_BASE_PATH, mBasePath);
//
//        BusManager.getInstance().register(DownloaderService.EVENT_UPDATE_STATUS, busEventObserver);
//        activity.startService(intent);

        final ProgressDialog downloadProgressDialog = DialogBuilder.buildProgressDialog(activity, null, 0);
        downloadProgressDialog.setTitle("正在下载安装包");
        String url = mUpdateVo.url;
        final String apkPath = mBasePath + "release/" + FileUtil.getFileNameByUrl(url);
        FileDownloader.getImpl().create(url).setPath(apkPath).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask baseDownloadTask, int i, int i1) {
                LogUtils.i("update", "downloadUpdateFiles...");

                if (isNotifyProgressDialog()) {
                    //如果是普通更新，跳过页面在后台更新
                    ToastUtils.showMessage(activity, "安装包正在后台下载中...");
                    listener.onUpdateSkip();
                } else {
                    if (!silent) {
                        downloadProgressDialog.show();
                    }
                }
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                downloadProgressDialog.setMax(totalBytes);
            }

            @Override
            protected void progress(BaseDownloadTask baseDownloadTask, int current, int max) {
                int progress = (int) (((float) current / (float) max) * 100);
                LogUtils.v("update", "download total-->>>" + max + "  current-->>>" + current + " (progress: " + progress + "%)");
                if (isNotifyProgressDialog()) {
                    PendingIntent emptyIntent = NotifyHelper.getDefaultIntent(activity, new Intent());
                    NotifyHelper.notify(activity, mUpdateVo.iconResId, "下载安装包", "正在下载: " + progress + "%", null, false, emptyIntent);
                } else {
//                    downloadProgressDialog.setMax(max);
                    downloadProgressDialog.setProgress(current);
                    downloadProgressDialog.setProgressNumberFormat(String.format("%.2f M/%.2f M", (float) current / 1024 / 1024, (float) max / 1024 / 1024));
                }
            }

            @Override
            protected void completed(BaseDownloadTask baseDownloadTask) {
                LogUtils.v("update", "download complete");
                setAPKDownloadStatus(activity, DOWNLOAD_STATUS_OK);
                if (isNotifyProgressDialog()) {
                    //** 下载完成，点击安装 **/
                    NotifyHelper.notifyClear(activity);
//                    AppUtils.installApplication(activity, baseDownloadTask.getTargetFilePath());
                } else {
                    downloadProgressDialog.dismiss();
                }
                if (null != listener) {
                    listener.onUpdateCompleteAndInstall(baseDownloadTask.getTargetFilePath());
                }
            }

            @Override
            protected void paused(BaseDownloadTask baseDownloadTask, int i, int i1) {

            }

            @Override
            protected void error(BaseDownloadTask baseDownloadTask, Throwable throwable) {
                LogUtils.v("update", "download error:" + throwable.getMessage());

                setAPKDownloadStatus(activity, DOWNLOAD_STATUS_NOT_OK);

                if (isNotifyProgressDialog()) {
                    NotifyHelper.notify(activity, mUpdateVo.iconResId, "下载失败", "下载安装包失败", new Intent());
                } else {
                    if (downloadProgressDialog.isShowing()) {
                        downloadProgressDialog.dismiss();
                    }
                    AlertDialog.Builder dialog = DialogBuilder.buildAlertDialog(activity, "错误", "网络不佳，请重试");
                    dialog.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadUpdateFiles(activity, silent, listener);
                        }
                    });
                    dialog.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppUtils.quitApp();
                        }
                    });
                    dialog.show();
                }

            }

            @Override
            protected void warn(BaseDownloadTask baseDownloadTask) {

            }
        }).start();
    }

    /**
     * 是否是通知栏进度显示
     *
     * @return
     */
    public boolean isNotifyProgressDialog() {
        return mUpdateVo.updateStatus == UpdateVo.UpdateStatus.UPDATE;
    }

//    private BusEventObserver updateStatusObserver = new BusEventObserver() {
//
//        @Override
//        public void onBusEvent(String event, Object message) {
//            if (DownloaderService.EVENT_UPDATE_DOWNLOAD_START.equals(event)) {
//
//            } else if (DownloaderService.EVENT_UPDATE_DOWNLOAD_PROGRESS.equals(event)) {
//
//            } else if (DownloaderService.EVENT_UPDATE_DOWNLOAD_SUCCESS.equals(event)) {
//
//            } else if (DownloaderService.EVENT_UPDATE_DOWNLOAD_FAILED.equals(event)) {
//
//            }
//        }
//    };

    /**
     * 检测apk是否已经下载完毕
     *
     * @return 返回是否下载完毕并且大小正确
     */
    public boolean checkAPK(Context context) {
        File apk = UpdateDownloadProvider.getDownloadFile(mBasePath, mUpdateVo);

        LogUtils.i("update", "check download apk...");
        LogUtils.i("update", "apk.exists(): " + apk.exists());
        LogUtils.i("update", "isAPKDownloadOk(context): " + isAPKDownloadOk(context));
        LogUtils.i("update", "apk.length(): " + apk.length() + "  vo.size: " + mUpdateVo.size);

        return apk.exists() && isAPKDownloadOk(context);
    }

    /**
     * 判断apk是否下载完毕
     *
     * @return
     */
    public boolean isAPKDownloadOk(Context context) {
        SharedPreferencesProvider provider = new SharedPreferencesProvider();
        String status = provider.getProvider(context).getCache(UPDATE_DOWNLOAD_STATUS_KEY + mUpdateVo.version);
        return DOWNLOAD_STATUS_OK.equals(status);
    }

    /**
     * 设置apk下载状态
     *
     * @param context
     * @param status
     */
    public void setAPKDownloadStatus(Context context, String status) {
        SharedPreferencesProvider provider = new SharedPreferencesProvider();
        provider.getProvider(context).putCache(UPDATE_DOWNLOAD_STATUS_KEY + mUpdateVo.version, status);
    }

    /**
     * 满足以下条件才可以开启静默下载
     * 1:当前是wifi状态，静默下载
     * 2:当前没有下载完成的完整文件（比对文件大小）
     * 3:已经开启了静默下载
     *
     * @param activity
     * @param listener
     */
    public void autoSilentDownload(Activity activity, UpdateListener listener) {
        if (mSilentDownload && !checkAPK(activity) && NetWorkUtils.getNetworkType(activity) == NetWorkUtils.NETTYPE_WIFI) {
            downloadUpdateFiles(activity, true, listener);
        }
    }

    /**
     * 操作检测更新逻辑的异步类
     * <p/>
     * Created by liuyuhang on 16/5/3.
     */
    private class UpdateOperation extends AsyncTask<Void, Void, UpdateVo> {
        private Activity activity;
        private UpdateOperator operator;
        private UpdateListener listener;

        public UpdateOperation(Activity activity, UpdateOperator operator, UpdateListener listener) {
            this.activity = activity;
            this.operator = operator;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.listener.onUpdatePre();
        }

        @Override
        protected UpdateVo doInBackground(Void... params) {
            String url = operator.onUpdateRequest();
            String header = operator.onUpdateRequestHeader();
            String param = operator.onUpdateRequestParams();

            String result = operator.doRequest(url, header, param);
            //发送更新请求
            return operator.parserUpdateJson(result);
        }

        @Override
        protected void onPostExecute(final UpdateVo updateObject) {
            super.onPostExecute(updateObject);
//            UpdateVo.UpdateStatus status = updateObject.updateStatus;

            mUpdateVo = updateObject;
            if (null == mUpdateVo) {
                mUpdateVo = new UpdateVo();
            }

            invokeUpdate(activity, listener);

//            if (status == UpdateVo.UpdateStatus.NONE) {
//                //不需要更新
//                listener.onUpdateSkip();
//            } else {
//                //需要更新，选择更新模式
//                AlertDialog.Builder builder = DialogBuilder.buildAlertDialog(context, updateObject.title, updateObject.description);
//
//                if (checkAPK(updateObject)) {
//                    builder.setNegativeButton("马上更新（免流量）", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                        downloadUpdateFiles(context, updateObject.url, false, listener);
//                            listener.onUpdateCompleteAndInstall(UpdateDownloadProvider.getDownloadFile(updateObject).getPath());
//                        }
//                    });
//                } else {
//                    builder.setNegativeButton("马上更新", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            downloadUpdateFiles(context, updateObject.url, false, listener);
////                          listener.onUpdateComplete();
//                        }
//                    });
//                }
//
//                if (status != UpdateVo.UpdateStatus.FORCE) {//如果是强制更新，不需要显示取消按钮（也就是下次再说）
//                    builder.setPositiveButton("下次再说", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            listener.onUpdateSkip();
//
//                            //静默更新的逻辑
//                            autoSilentDownload(context, updateObject, listener);
//                        }
//                    });
//                }
//
//                builder.show();
//            }
        }
    }
}