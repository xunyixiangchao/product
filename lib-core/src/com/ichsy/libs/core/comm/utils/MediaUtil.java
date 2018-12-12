package com.ichsy.libs.core.comm.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 媒体库工具类
 * Created by liuyuhang on 2017/9/20.
 */

public class MediaUtil {

    private static final String[] STORE_IMAGES = {MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media._ID,};

    public interface MediaFileIterator {
        /**
         * 找到媒体文件的处理
         *
         * @param id
         * @param data        完整的文件夹+文件名
         * @param displayName
         */
        void onMediaFileFound(String id, String data, String path, String displayName);

        /**
         * 出现错误
         *
         * @param message
         */
        void onError(String message);
    }

    public interface MediaCateIterator {
        /**
         * 目录归类
         *
         * @param cate
         * @param files 目录中包含的文件
         */
        void onMediaCateFound(String cate, List<HashMap<String, String>> files);

        /**
         * 出现错误
         *
         * @param message
         */
        void onError(String message);
    }

    /**
     * 迭代媒体目录
     *
     * @param context
     * @param iterator
     */
    public static void iteratorMediaCate(Context context, final MediaCateIterator iterator) {
        final String SDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        final HashMap<String, List<HashMap<String, String>>> cateMap = new HashMap<>();//目录的map

        iteratorMediaFile(context, new MediaFileIterator() {

            @Override
            public void onMediaFileFound(String id, String data, String path, String displayName) {
                //目录归类
                String cate = path.replace(SDCardPath, "");

                List<HashMap<String, String>> list;

                if (cateMap.get(cate) == null) {
                    list = new ArrayList<>();
                    cateMap.put(cate, list);
                } else {
                    list = cateMap.get(cate);
                }

                HashMap<String, String> fileMap = new HashMap<>();
                fileMap.put("id", id);
                fileMap.put("path", path);
                fileMap.put("displayName", displayName);
                list.add(fileMap);
            }

            @Override
            public void onError(String message) {
                iterator.onError(message);
            }
        });

        for (Map.Entry<String, List<HashMap<String, String>>> next : cateMap.entrySet()) {
            iterator.onMediaCateFound(next.getKey(), next.getValue());
        }

    }

    /**
     * 迭代媒体wenj
     *
     * @param context
     * @param callback
     */
    public static void iteratorMediaFile(Context context, MediaFileIterator callback) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, null, null, MediaStore.Images.Media.DATE_ADDED);

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            int counter = cursor.getCount();


            for (int i = 0; i < counter; i++) {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

                String path = data.substring(0, data.lastIndexOf("/"));

                callback.onMediaFileFound(id, data, path, displayName);


                cursor.moveToNext();
            }

            cursor.close();
        } else {
            callback.onError("files no found");
        }

    }
}
