package com.ichsy.libs.core.comm.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ichsy.libs.core.config.CoreConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * 文件工具类
 *
 * @author xingchun
 */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    private final static String FOLDER_NAME = "/leyou/";

    private static String imageFolder() {
        String folder = Environment.getExternalStorageDirectory() + FOLDER_NAME + "image/";
        mkDir(folder);
        return folder;
    }

    private static boolean mkDir(String folder) {
        File file = new File(folder);

        return file.exists() || file.mkdirs();
    }


    public static boolean saveBitmap(Bitmap mBitmap, String path) {
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            return false;
        }
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return true;
    }

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    public static String getSdCardPath() {
        if (isSdCardExist()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return "";
        }
    }

    /**
     * 用bufferReader读取文件
     *
     * @param path
     * @param fileName
     * @return
     */
    public static String readByBufferReader(String path, String fileName) {
        File file = new File(path, fileName);
        if (!file.exists()) return null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
//                System.out.println("readline:" + readline);
                sb.append(line);
            }
            br.close();
//            System.out.println("读取成功：" + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用BufferWriter写文件
     *
     * @param path     路径
     * @param fileName 名字
     * @param text     内容
     * @param isAppend 复写还是追加
     * @return
     */
    public static boolean writeByBufferWriter(String path, String fileName, String text, boolean isAppend) {
        BufferedWriter bw = null;
        try {
            new File(path).mkdirs();
            File file = new File(path, fileName);
            //第二个参数意义是说是否以append方式添加内容
            bw = new BufferedWriter(new FileWriter(file, isAppend));
            bw.write(text);
            bw.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 保存Bitmap
     *
     * @param bitmap
     * @param tempFile
     */
    public static void saveBitmap(Bitmap bitmap, File tempFile) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return degree;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();

        return data;
    }

    /**
     * @param filePath
     * @return byte[]
     * @throws FileNotFoundException
     */
    public static byte[] readFileToBytes(String filePath) throws FileNotFoundException {
        byte[] fileBytes = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            int length = (int) file.length();
            fileBytes = new byte[length];
            in = new FileInputStream(filePath);
            in.read(fileBytes, 0, length);
            in.close();
            in = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                    in = null;
                } catch (Exception ex) {
                }
            }
        }
        return fileBytes;
    }

    public static Bitmap getBitmapFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null) {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }

        return null;
    }

    /**
     * 保存一个字符串到文件, 如果文件存在，则清除之前的数据，保存新的数据
     *
     * @param str
     * @param path
     */
    public static boolean saveString(String str, String path) {
        File f = new File(path);
        if (!f.getParentFile().exists())
            f.mkdirs();
        if (f.exists())
            f.delete();
        try {
            f.createNewFile();
        } catch (IOException e) {
            return false;
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            fOut.write(str.getBytes());
            fOut.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * 获取String
     *
     * @param path
     * @return
     */
    public static String getString(String path) {
        if (path == null)
            return null;
        File f = new File(path);
        if (!f.exists())
            return null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String temp = null;
            StringBuffer sb = new StringBuffer();
            temp = br.readLine();
            while (temp != null) {
                sb.append(temp);
                temp = br.readLine();
            }
            LogUtils.i(TAG, "url=" + sb.toString());
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件 语音、图片
     *
     * @param urlPath
     * @return
     */
    public static byte[] getFileByte(String urlPath) {
        InputStream in = null;
        byte[] result = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();
            httpURLconnection.setDoInput(true);
            httpURLconnection.connect();
            if (httpURLconnection.getResponseCode() == 200) {
                in = httpURLconnection.getInputStream();
                result = readAll(in);
                in.close();
            } else {
                Log.e(TAG, "下载文件失败，状态码是：" + httpURLconnection.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "下载文件失败，原因是：" + e.toString());
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static byte[] readAll(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        byte[] buf = new byte[1024];
        int c = is.read(buf);
        while (-1 != c) {
            baos.write(buf, 0, c);
            c = is.read(buf);
        }
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }


    public static boolean saveInfo2File(File file, String content) {
        LogUtils.e(CoreConfig.LOG_TAG, content);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.e(CoreConfig.LOG_TAG, file.getPath());
        return true;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
        } else {
            if (file.isFile()) {
                file.deleteOnExit();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.deleteOnExit();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.deleteOnExit();
            }
        }
    }

    public static @NonNull
    String getFileNameByUrl(@NonNull String url) {
        if (isEmpty(url)) {
            return null;
        }
        int index = url.lastIndexOf('?');
        int index2 = url.lastIndexOf("/");
        if (index > 0 && index2 >= index) {
            return UUID.randomUUID().toString();
        }
        return url.substring(index2 + 1, index < 0 ? url.length() : index);

    }

    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 将二维码图片保存到文件夹
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String externalStorageState = Environment.getExternalStorageState();
        //判断sd卡是否挂载
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
        /*外部存储可用，则保存到外部存储*/
            //创建一个文件夹
            File appDir = new File(Environment.getExternalStorageDirectory(), "syy");
            //如果文件夹不存在
            if (!appDir.exists()) {
                //则创建这个文件夹
                appDir.mkdir();
            }
            //将bitmap保存
            save(context, bmp, appDir);
        } else {
            //外部不可用，将图片保存到内部存储中，获取内部存储文件目录
            File filesDir = context.getFilesDir();
            //保存
            save(context, bmp, filesDir);
        }
    }

    private static void save(Context context, Bitmap bmp, File appDir) {
        //命名文件名称
        String fileName = System.currentTimeMillis() + ".jpg";
        //创建图片文件，传入文件夹和文件名
        File imagePath = new File(appDir, fileName);
        try {
            //创建文件输出流，传入图片文件，用于输入bitmap
            FileOutputStream fos = new FileOutputStream(imagePath);
            //将bitmap压缩成png，并保存到相应的文件夹中
            drawBg4Bitmap(Color.WHITE, bmp).compress(Bitmap.CompressFormat.PNG, 100, fos);
            //冲刷流
            fos.flush();
            //关闭流
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    imagePath.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imagePath.getAbsolutePath())));
    }

    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }
}
