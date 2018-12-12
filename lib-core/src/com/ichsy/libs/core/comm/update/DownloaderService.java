package com.ichsy.libs.core.comm.update;

import android.app.Service;
import android.content.Intent;

import com.ichsy.libs.core.comm.bus.BusManager;
import com.ichsy.libs.core.frame.sevice.BaseFrameService;

/**
 * 下载用的service
 * Created by liuyuhang on 16/8/17.
 */
public class DownloaderService extends BaseFrameService {
    public static final String INTENT_UPDATE_OBJECT = "INTENT_UPDATE_OBJECT";
    public static final String INTENT_UPDATE_BASE_PATH = "INTENT_UPDATE_BASE_PATH";

    public static final String EVENT_UPDATE_STATUS = "EVENT_UPDATE_STATUS";

    public static final int EVENT_UPDATE_DOWNLOAD_START = 1;
    public static final int EVENT_UPDATE_DOWNLOAD_PROGRESS = 2;
    public static final int EVENT_UPDATE_DOWNLOAD_SUCCESS = 3;
    public static final int EVENT_UPDATE_DOWNLOAD_FAILED = 4;


//    private UpdateDownloadStatus mUpdateDownloadStatus;

    public static class UpdateDownloadStatus {
        public int status;//下载状态，开始下载，正在下载，下载完毕
        public String url;//
        public int max;//最大数量
        public int current;//当前数量
        /**
         * 当前进度百分比0-100
         */
        public int progress;

        public String filePath;//下载的路径
        public String message;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        UpdateVo updateVo = (UpdateVo) intent.getSerializableExtra(INTENT_UPDATE_OBJECT);
        String basePath = intent.getStringExtra(INTENT_UPDATE_BASE_PATH);

        if(null != updateVo) {
            UpdateDownloadProvider.httpDownload(updateVo.url, basePath, new UpdateDownloadProvider.DownloadListener() {

                @Override
                public void onDownloadStart(String url) {
                    UpdateDownloadStatus mUpdateDownloadStatus = new UpdateDownloadStatus();
                    mUpdateDownloadStatus.status = EVENT_UPDATE_DOWNLOAD_START;
                    mUpdateDownloadStatus.current = 0;

                    BusManager.getInstance().postEvent(EVENT_UPDATE_STATUS, mUpdateDownloadStatus);
                }

                @Override
                public void onDownloadProgress(int max, int current) {
                    UpdateDownloadStatus mUpdateDownloadStatus = new UpdateDownloadStatus();
                    mUpdateDownloadStatus.status = EVENT_UPDATE_DOWNLOAD_PROGRESS;
                    mUpdateDownloadStatus.max = max;
                    mUpdateDownloadStatus.current = current;
                    mUpdateDownloadStatus.progress = (int) (((float) current / (float) max) * 100);
                    BusManager.getInstance().postEvent(EVENT_UPDATE_STATUS, mUpdateDownloadStatus);
                }

                @Override
                public void onDownloadSuccess(String path) {
                    UpdateDownloadStatus mUpdateDownloadStatus = new UpdateDownloadStatus();
                    mUpdateDownloadStatus.status = EVENT_UPDATE_DOWNLOAD_SUCCESS;
                    mUpdateDownloadStatus.filePath = path;
                    BusManager.getInstance().postEvent(EVENT_UPDATE_STATUS, mUpdateDownloadStatus);

                    stopSelf();
                }

                @Override
                public void onDownloadFailed(String message) {
                    UpdateDownloadStatus mUpdateDownloadStatus = new UpdateDownloadStatus();
                    mUpdateDownloadStatus.status = EVENT_UPDATE_DOWNLOAD_FAILED;
                    mUpdateDownloadStatus.message = message;
                    BusManager.getInstance().postEvent(EVENT_UPDATE_STATUS, mUpdateDownloadStatus);

                    stopSelf();
                }
            });
//        UpdateDownloadProvider.httpDownload(updateVo.url, basePath, new UpdateDownloadProvider.DownloadListener() {
//            @Override
//            public void onDownloadStart(String url) {
//                if (!silent && null != downloadProgressDialog) {
//                    downloadProgressDialog.show();
//                }
//            }
//
//            @Override
//            public void onDownloadProgress(int max, int progress) {
//                LogUtils.v("update", "download total-->>>" + max + "  progress-->>>" + progress);
//
//                if (null != downloadProgressDialog) {
//                    downloadProgressDialog.setMax(max);
//                    downloadProgressDialog.setProgress(progress);
//                }
//            }
//
//            @Override
//            public void onDownloadSuccess(String path) {
//                setAPKDownloadStatus(context, DOWNLOAD_STATUS_OK);
//
//                if (null != listener) {
//                    listener.onUpdateCompleteAndInstall(path);
//                }
//                if (null != downloadProgressDialog) {
//                    downloadProgressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onDownloadFailed(String message) {
//                setAPKDownloadStatus(context, DOWNLOAD_STATUS_NOT_OK);
//
//                LogUtils.i("update", "error: " + message);
//
//                if (null != downloadProgressDialog) {
//                    downloadProgressDialog.dismiss();
//                }
//            }
//        });
        }

        return Service.START_REDELIVER_INTENT;
    }

}
