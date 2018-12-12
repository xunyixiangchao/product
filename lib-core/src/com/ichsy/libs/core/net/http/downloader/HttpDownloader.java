package com.ichsy.libs.core.net.http.downloader;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.FileUtil;
import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.ThreadPoolUtil;
import com.ichsy.libs.core.net.http.OkHttpClientHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 基于okhttp的文件下载工具类
 * Created by liuyuhang on 16/8/4.
 */
public class HttpDownloader {
    public interface HttpDownloaderListener {
        void onDownloadFailure(String message);

        void onDownloadProgress(long max, long position, int progress);

        void onDownloadSuccess(String filePath, String fileName);
    }

//    //每次下载需要新建新的Call对象
//    private static Call newCall(String url, long startPoints) {
//        Request request = new Request.Builder()
//                .url(url)
//                .header("RANGE", "bytes=" + startPoints + "-")//断点续传要用到的，指示下载的区间
//                .build();
//        return new OkHttpClient().newCall(request);
//    }

    /**
     * @param url
     * @param filePath 绝对路径，不需要后缀名
     * @param listener
     */
    public static void addDownloaderTask(final String url, final String filePath, final HttpDownloaderListener listener) {


        Request.Builder requestBuilder = new Request.Builder().url(url);

        final String fileName = FileUtil.getFileNameByUrl(url);
        //读取断点续传位置
        String extJson = FileUtil.readByBufferReader(filePath, fileName + "_extTmp");
        if (!TextUtils.isEmpty(extJson)) {
            DownloaderExtVo extVo = GsonHelper.build().fromJson(extJson, DownloaderExtVo.class);
            if (null != extVo) {
//                startPoint = extVo.current;
//                requestBuilder.header("RANGE", "bytes=" + extVo.current + "-");
            }
        }
//        Request request = new Request.Builder().addHeader("Range", "").url(url).build();

        OkHttpClientHelper.INSTANCE.getOkHttpClient().newCall(requestBuilder.build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDownloadFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String fileName = FileUtil.getFileNameByUrl(url);

                InputStream is = null;
                byte[] buffer = new byte[2048];
                int len;
                FileOutputStream fos = null;

                ResponseBody body = response.body();
                InputStream in = body.byteStream();
                FileChannel channelOut = null;
                // 随机访问文件，可以指定断点续传的起始位置
                RandomAccessFile randomAccessFile = null;

                long sum = 0;
                final long total = response.body().contentLength();

                try {
                    is = response.body().byteStream();
                    if (!validateFile(url, filePath, response.body())) {
                        File file = new File(filePath, fileName + "_tmp");

                        new File(filePath).mkdirs();


//                        fos = new FileOutputStream(extFile);
//                        fos.write(buffer, 0, );
//                        fos.flush();

//                        fos = new FileOutputStream(file);
//                        long sum = 0;
//                        while ((len = is.read(buf)) != -1) {
//                            fos.write(buf, 0, len);
//                            sum += len;
//                            final int progress = (int) (sum * 1.0f / total * 100);
//                            final long finalSum = sum;
//                            ThreadPoolUtil.runOnMainThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    listener.onDownloadProgress(total, finalSum, progress);
//                                }
//                            });
//                        }
//                        fos.flush();

                        long startPoint = 0L;

                        randomAccessFile = new RandomAccessFile(file, "rwd");
                        //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
                        channelOut = randomAccessFile.getChannel();
                        // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
                        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startPoint, body.contentLength());
//                        byte[] buffer = new byte[1024];
//                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            mappedBuffer.put(buffer, 0, len);
                            sum += len;
                            final long finalLen = sum;
                            final int progress = (int) (finalLen * 1.0f / response.body().contentLength() * 100);
                            ThreadPoolUtil.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
//                                    int progress = (int) (finalLen * 1.0f / response.body().contentLength() * 100);
                                    listener.onDownloadProgress(total, finalLen, progress);
                                }
                            });
                        }

                        file.renameTo(new File(filePath, fileName));
                    } else {
                        LogUtils.i("downloader", "file is exits... ");
                    }
                    ThreadPoolUtil.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDownloadSuccess(filePath, fileName);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onDownloadFailure(e.getMessage());

                    DownloaderExtVo extVo = new DownloaderExtVo();
                    extVo.current = sum;
                    extVo.max = total;
                    FileUtil.writeByBufferWriter(filePath, fileName + "_extTmp", GsonHelper.build().toJson(extVo), false);

                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                        in.close();
                        if (channelOut != null) {
                            channelOut.close();
                        }
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }

    /**
     * 检查文件是否下载完毕
     *
     * @param url
     * @param filePath
     * @param body
     * @return
     */
    private static boolean validateFile(@NonNull String url, String filePath, ResponseBody body) {
        String fileName = FileUtil.getFileNameByUrl(url);
        File file = new File(filePath, fileName);
        return file.length() == body.contentLength();
    }


}
