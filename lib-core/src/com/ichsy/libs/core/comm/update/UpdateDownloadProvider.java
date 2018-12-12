package com.ichsy.libs.core.comm.update;

import com.ichsy.libs.core.comm.utils.FileUtil;
import com.ichsy.libs.core.net.http.downloader.HttpDownloader;

import java.io.File;

/**
 * 检测更新 - 下载文件的工具类
 * Created by liuyuhang on 16/5/3.
 */
public class UpdateDownloadProvider {
    public static final String FILE_DOWNLOAD_PATH = "/release/";

    public interface DownloadListener {
        /**
         * 文件准备下载
         *
         * @param url
         */
        void onDownloadStart(String url);

        /**
         * 文件正在下载
         *
         * @param max
         * @param current
         */
        void onDownloadProgress(int max, int current);

        /**
         * 文件下载完成
         *
         * @param path
         */
        void onDownloadSuccess(String path);

        void onDownloadFailed(String message);
    }

    /**
     * 执行检测更新的文件下载
     *
     * @param url
     * @param listener
     */
    public static void httpDownload(String url, String path, final DownloadListener listener) {
        listener.onDownloadStart(url);

        final int[] lastProgressPosition = {-1};

        HttpDownloader.addDownloaderTask(url, path + FILE_DOWNLOAD_PATH, new HttpDownloader.HttpDownloaderListener() {
            @Override
            public void onDownloadFailure(String message) {
                listener.onDownloadFailed("下载失败: " + message);
            }

            @Override
            public void onDownloadProgress(long max, long position, int progress) {
                int maxKB = (int) max / 1024;
                int progressKB = (int) position / 1024;

                int pro = (int) (((float) progressKB / (float) maxKB) * 100);

                if (lastProgressPosition[0] != pro) {
                    listener.onDownloadProgress(maxKB, progressKB);
                }
                lastProgressPosition[0] = pro;
            }

            @Override
            public void onDownloadSuccess(String filePath, String fileName) {
                listener.onDownloadSuccess(filePath + fileName);
            }
        });

    }

    /**
     * 执行检测更新的文件下载
     *
     * @param url
     * @param listener
     */
//    public static void download(String url, final DownloadListener listener) {
//        listener.onDownloadStart(url);
//
////        HttpDownloader.addDownloaderTask(url, FILE_DOWNLOAD_PATH);
//
//        File savePath = new File(FILE_DOWNLOAD_PATH);// saveFile
//        DownLoadTask task = new DownLoadTask();
//        task.setId(url);
//        task.setDlSavePath(savePath.getPath());
//        task.setUrl(url);
//        DownLoadManager.getInstance().addDLTask(task, new DownLoadListener() {
//            @Override
//            public void onDownLoadStart(DownLoadTask task) {
//            }
//
//            @Override
//            public void onDownLoadUpdated(DownLoadTask task, long finishedSize, long trafficSpeed) {
//                int maxKB = (int) task.getDlTotalSize() / 1024;
//                int progressKB = (int) finishedSize / 1024;
//                listener.onDownloadProgress(maxKB, progressKB);
//            }
//
//            @Override
//            public void onDownLoadPaused(DownLoadTask task) {
//
//            }
//
//            @Override
//            public void onDownLoadResumed(DownLoadTask task) {
//
//            }
//
//            @Override
//            public void onDownLoadSuccess(DownLoadTask task) {
//                listener.onDownloadSuccess(task.getDlSavePath());
//            }
//
//            @Override
//            public void onDownLoadCanceled(DownLoadTask task) {
//
//            }
//
//            @Override
//            public void onDownLoadFailed(DownLoadTask task) {
//                listener.onDownloadFailed("下载失败");
//            }
//
//            @Override
//            public void onDownLoadRetry(DownLoadTask task) {
//
//            }
//        });
//    }


    /**
     * 获取更新文件的File
     *
     * @param vo Update实体类
     * @return 返回更新的文件保存地址的File文件
     */
    public static File getDownloadFile(String basePath, UpdateVo vo) {
        String path = basePath + FILE_DOWNLOAD_PATH;
        String name = FileUtil.getFileNameByUrl(vo.url);
        return new File(path, name);
    }
}
