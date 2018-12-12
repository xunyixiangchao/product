package com.ichsy.libs.core.comm.hotfix;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.exceptions.ExceptionUtil;
import com.ichsy.libs.core.comm.logwatch.LogWatcher;
import com.ichsy.libs.core.comm.utils.AppUtils;
import com.ichsy.libs.core.comm.utils.FileUtil;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.ThreadPoolUtil;
import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.ichsy.libs.core.net.http.downloader.HttpDownloader;

import java.io.File;
import java.util.HashMap;


/**
 * 保护hotFix在修复代码的过程中出现启动程序崩溃，
 * <p/>
 * 原理是在程序启动的时候做一次标识，然后如果启动失败设置这个标示为false
 *
 * @author liuyuhang
 */
public class HotFixProtecter {
    private static final String TAG = "HotFixProtecter";

    private static final String HOTFIX_KEY = "hotfix_state";

    private static final String HOTFIX_TAG = "hotfix_tag";

    private static HotFixProtecter instance;

    private String mBasePath;

    public static final int HOTFIX_STEP_INIT = 1;
    public static final int HOTFIX_STEP_FAILED = 2;
    public static final int HOTFIX_STEP_SUCCESS = 3;

    //    private Context context;
//    private SharedPreferences mPreferences;
//	private PatchManager mPatchManager;

    public interface FileReadCallBack {
        void onCallBack(Context context, String absolutePath, String fileName);
    }

    public static HotFixProtecter getInstance() {
        if (instance == null) {
            instance = new HotFixProtecter();
        }

        return instance;
    }

    public void setBasePath(String path) {
        this.mBasePath = path;
    }

//    /**
//     * 初始化程序，并且在这个时候做第一次校验
//     *
//     * @param context
//     */
//    public void init(Context context) {
//        this.context = context;
//
////		/**
////		 * hotFix
////		 */
////		mPatchManager = new PatchManager(context);
////		mPatchManager.init(AppUtils.getAppVersionCode(context) + "");
////		LogUtils.i(TAG, "inited.");
////
////		// HotFixProtecter.getInstance().init(this);
////		// // 运行代码之前要先检查上一次是否运行成功，如果没有成功 需要删除所有补丁
////		// if (!HotFixProtecter.getInstance().checkRunSuccess()) {
////		// mPatchManager.removeAllPatch();
////		// }
////		// load patch
////		mPatchManager.loadPatch();
////		LogUtils.i(TAG, "apatch loaded.");
//    }

//    public void findHotfix(String url) {
//        if (!TextUtils.isEmpty(url)) {
//            HttpHelper helper = new HttpHelper(context);
//            helper.doPost(url, null, VersionInfo.class, listener);
//
//            LogWatcher.getInstance().putMessage("find hotfix url: " +
//                    andFixUpdateUrl);
//        }
//    }

    /**
     * 下载并加载热修复补丁
     *
     * @param hotfixMap
     */
    public void downloadHotfix(Context context, HashMap<String, String> hotfixMap) {
        if (!hotfixMap.isEmpty()) {
            String apathUrl = hotfixMap.get("url");

            String path = mBasePath + AppUtils.getAppVersionCode(context) + "/";
            String fileName = FileUtil.getFileNameByUrl(apathUrl);
            LogUtils.i("hotfix", "verfy path is:" + path + fileName);
            if (!(path + fileName).equals(HotFixProtecter.getInstance().getHotFixTag(context))) {
                // 当前热修复需要更新
                File saveFiles = new File(path + fileName);// saveFile
                if (saveFiles.exists()) {
                    FileUtil.deleteFile(saveFiles);
                }

                LogWatcher.getInstance().putMessage("start download new andFix...\n" + apathUrl);
//				DownLoadManager downLoadManager = new DownLoadManager();
//				downLoadManager.init();

//                DownLoadTask task = new DownLoadTask();
//                task.setId(apathUrl);
//                task.setDlSavePath(saveFiles.getPath());
//                task.setUrl(apathUrl);
//                DownLoadManager.getInstance().addDLTask(task, hotFixDownloadListener);

                HttpDownloader.addDownloaderTask(apathUrl, path, new HttpDownloader.HttpDownloaderListener() {
                    @Override
                    public void onDownloadFailure(String message) {
                        LogUtils.i("hotfix", "hotfix4 onDownloadFailure:" + message);
                    }

                    @Override
                    public void onDownloadProgress(long max, long position, int progress) {

                    }

                    @Override
                    public void onDownloadSuccess(String filePath, String fileName) {
                        LogUtils.i("hotfix", "hotfix4 path download ok sd is:" + filePath + fileName);
                    }
                });

                LogUtils.i("hotfix", "start download new andFix...\n" + apathUrl);
            } else {
                LogWatcher.getInstance().putMessage("apatch: " + apathUrl + "  already added");
                LogUtils.i("hotfix", "apatch: " + apathUrl + "  already added");
            }
        } else {
            LogWatcher.getInstance().putMessage("apatch: must remove all appatch");
            LogUtils.i("hotfix", "apatch: must remove all appatch");

//			mPatchManager.removeAllPatch();

            saveHotFixTag(context, "");
        }
    }


    /**
     * 加载sd卡目录下的所有补丁文件，补丁必须以.apatch结尾
     *
     * @param sdPath sd卡目录，e.g /huijiayou/hotfix
     *               //	 * @param needVerfy
     *               //	 *            补丁校验是否通过，true表示校验通过，false表示校验未通过
     */
    public void loadHotFixPaths(final Context context, String sdPath, final FileReadCallBack callBack) {
        if (!sdPath.endsWith("/")) {
            sdPath = sdPath + "/";
//            sdPath += sdPath;
        }

        final String finalSdPath = sdPath;
        ThreadPoolUtil.getInstance().fetchData(new Runnable() {
            @Override
            public void run() {
                // if (needVerfy &&
                // !TextUtils.isEmpty(HotFixProtecter.getInstance().getHotFixTag())) {
                // PatchManager mPatchManager = ((AppApplication)
                // context).mPatchManager;
                // add patch at runtime
                try {
                    File pathFile = new File(finalSdPath + AppUtils.getAppVersionCode(context) + "/");

                    LogUtils.i("hotfix", "start search hotfix jars... path: " + pathFile);

                    if (pathFile.exists()) {
                        File[] paths = pathFile.listFiles();
                        for (final File path : paths) {
                            final String pathFilesString = path.getAbsolutePath();

                            LogUtils.i("hotfix", "search --->>>" + pathFilesString);
                            if (pathFilesString.endsWith(".apk")) {
                                LogUtils.i("hotfix", "hot fix find....->>>>" + pathFilesString);

                                ThreadPoolUtil.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.onCallBack(context, pathFilesString, path.getName());
                                    }
                                });
                                // .apatch file path
//					mPatchManager.addPatch(pathFilesString);
//                    RocooFix.applyPatch(this, pathFilesString);
                            }
                        }
                    } else {
                        LogUtils.i("hotfix", "not found any hotfix files...");
                    }

                } catch (Exception e) {
                    String error = ExceptionUtil.getException(e);
                    LogUtils.i(TAG, error);
                    LogWatcher.getInstance().putMessage("apatch load failed: " + error);
                }

            }
        });

    }

    /**
     * 查到补丁文件
     *
     * @param context
     * @param path
     * @param callBack
     */
    private void findHotfixFiles(final Context context, final String path, final FileReadCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //定位当前目录
                try {
                    File pathFile = new File(path + AppUtils.getAppVersionCode(context) + "/");

                    if (pathFile.exists()) {
                        File[] hotfixFiles = pathFile.listFiles();
                        for (final File hotfixFile : hotfixFiles) {
                            final String hotfixFileAbsPath = hotfixFile.getAbsolutePath();
                            LogUtils.i("hotfix", "search --->>>" + hotfixFileAbsPath);
                            if (hotfixFileAbsPath.endsWith(".jar")) {
                                LogUtils.i("hotfix", "hot fix find....->>>>" + hotfixFileAbsPath);

                                ThreadPoolUtil.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.onCallBack(context, hotfixFileAbsPath, hotfixFile.getName());
                                    }
                                });
                            }
                        }

                    } else {
                        LogUtils.i("hotfix", "not found any hotfix files...");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    String error = ExceptionUtil.getException(e);
                    LogUtils.i(TAG, error);
                    LogWatcher.getInstance().putMessage("apatch load failed: " + error);
                }

            }
        };
        ThreadPoolUtil.getInstance().fetchData(runnable);
    }

    public static int getHotFixStep(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        String cacheStep = provider.getCache("hotfix_step");
        if (TextUtils.isEmpty(cacheStep)) {
            return HOTFIX_STEP_INIT;
        } else {
            return Integer.valueOf(cacheStep);
        }
    }

    public static void setHotfixStep(Context context, int step) {
        new SharedPreferencesProvider().getProvider(context).putCache("hotfix_step", step);
    }


    /**
     * 保存当前使用hotfix的tag
     *
     * @param context
     * @param tag
     */
    public void saveHotFixTag(Context context, String tag) {
        getProvider(context).edit().putString(HOTFIX_TAG, tag).apply();
    }

    /**
     * 获取当前hotfix的tag
     */
    public String getHotFixTag(Context context) {
        return getProvider(context).getString(HOTFIX_TAG, "");
    }

    private SharedPreferences getProvider(Context context) {
        return context.getSharedPreferences(HOTFIX_KEY, Activity.MODE_PRIVATE);
    }

}
