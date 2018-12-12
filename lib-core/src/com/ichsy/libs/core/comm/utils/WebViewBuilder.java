package com.ichsy.libs.core.comm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 封装好的针对webview处理的工具类
 * Created by liuyuhang on 16/8/12.
 */
public class WebViewBuilder {

    public interface UrlLoadedCallback {
        void onUrlLoaded(String url);
    }

    public static void buildSetting(final WebView webView, String agent) {

        webView.setDownloadListener(new DownloadListener() {

                                        @Override
                                        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                                            try {
                                                Context context = webView.getContext();
//                                                Uri uri = Uri.parse(url);
//                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//
//                                                if (!(context instanceof Activity)) {
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                }
//                                                context.startActivity(intent);

                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                                intent.setData(Uri.parse(url));
                                                context.startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                ToastUtils.showMessage(webView.getContext(), "无法下载：" + e.getMessage());
                                            }

                                        }
                                    }

        );

        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(webSettings.getUserAgentString() + agent);

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setBuiltInZoomControls(true);// 设置显示缩放按钮
//        webSettings.setSupportZoom(true);// 支持缩放
//        webSettings.setJavaScriptEnabled(true);// 支持JS
//
//        webSettings.setUseWideViewPort(true);
//
//        webSettings.setBlockNetworkImage(false);// 把图片加载放在最后来加载渲染
        webSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
//
//        if (Build.VERSION.SDK_INT >= 19) {
//            webView.getSettings().setLoadsImagesAutomatically(true);
//        } else {
//            webView.getSettings().setLoadsImagesAutomatically(false);
//        }
//
//        if (Build.VERSION.SDK_INT >= 11) {
//            webSettings.setDisplayZoomControls(false);// 设定缩放控件隐藏
//        } else {
//            ZoomButtonsController zbc = new ZoomButtonsController(webView);
//            zbc.getZoomControls().setVisibility(View.GONE);
//        }

// User settings

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点

//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        webSettings.setLoadWithOverviewMode(true);

//        DisplayMetrics metrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int mDensity = metrics.densityDpi;
//        if (mDensity == 240) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        } else if (mDensity == 160) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        } else if(mDensity == 120) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
//        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        }else if (mDensity == DisplayMetrics.DENSITY_TV){
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        }else{
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        }

        if (Build.VERSION.SDK_INT > 19) {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        }
//        else {
//            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        }

        webSettings.setLoadWithOverviewMode(true);
    }


    public static void fixScroll(WebView webView) {
        final float[] startx = new float[1];
        final float[] starty = new float[1];
        final float[] offsetx = new float[1];
        final float[] offsety = new float[1];

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        startx[0] = event.getX();
                        starty[0] = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetx[0] = Math.abs(event.getX() - startx[0]);
                        offsety[0] = Math.abs(event.getY() - starty[0]);
                        if (offsetx[0] > offsety[0]) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    /**
     * 获取浏览器UserAgent
     *
     * @param context
     * @return
     */
    public static String getUserAgent(Context context) {
        WebView webview;
        webview = new WebView(context);
        webview.layout(0, 0, 0, 0);
        WebSettings settings = webview.getSettings();
        return settings.getUserAgentString();
    }

    /**
     * 执行js方法
     *
     * @param webView
     * @param function js方法
     */
    public static void evaluateJavascript(final WebView webView, String function) {
        final String jsScript = "javascript:" + function;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(jsScript, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });
        } else {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(jsScript);
                }
            });
        }
    }
}