package com.leyou.library.le_library.comm.view.banner.view;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.holder.Holder;
import com.ichsy.libs.core.comm.bus.BusManager;
import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.leyou.library.le_library.comm.helper.ImageHelper;
import com.leyou.library.le_library.config.EventKeys;

import library.liuyh.com.lelibrary.R;


public class VideoAndImageViewHolderView implements Holder<String> {

    private RelativeLayout root;
    private String videoUrl; //视频连接
    private boolean isVideo; //是否有视频
    private String flagUrl;// 图片标记链接
    private ViewHolder holder;
    private ImageLoaderCallback callback;


    public VideoAndImageViewHolderView(String videoUrl, boolean isVideo, String flagUrl, ImageLoaderCallback callback) {
        this.videoUrl = videoUrl;
        this.isVideo = isVideo;
        this.flagUrl = flagUrl;
        this.callback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View createView(Context context) {
        if (root == null) {
            root = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_product_video_banner, null);
            holder = new ViewHolder();
            holder.mLayoutVideoCover = (RelativeLayout) root.findViewById(R.id.ll_banner_video_cover);
            holder.mLayoutVideo = (LinearLayout) root.findViewById(R.id.ll_banner_video);
            holder.mImageCover = (ImageView) root.findViewById(R.id.image_banner_video_cover);
            holder.mImagePlay = (ImageView) root.findViewById(R.id.image_banner_video_play);
            holder.mImageBanner = (ImageView) root.findViewById(R.id.image_view_banner);
            holder.mImageFlag = (ImageView) root.findViewById(R.id.image_view_flag);
            root.setTag(holder);
        } else {
            holder = (ViewHolder) root.getTag();
        }
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void UpdateUI(final Context context, int position, final String coverUrl) {
        if (position == 0 && isVideo) {
            ViewUtil.swapView(holder.mImageBanner, holder.mLayoutVideoCover);
            ImageHelper.with(context).load(coverUrl, R.drawable.seat_goods1080x1080).into(holder.mImageCover);
            holder.mImagePlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.videoView == null) {
                        holder.videoView = new SimpleVideoView(context);
                    }
                    holder.mLayoutVideo.setVisibility(View.VISIBLE);
                    holder.mLayoutVideo.removeAllViews();
                    holder.mLayoutVideo.addView(holder.videoView);
                    holder.mLayoutVideoCover.setVisibility(View.GONE);
                    startVideo(coverUrl);
                }
            });
            if (callback != null) {
                callback.loadFinish();
            }
        } else {
            if (isVideo) {
                BusManager.getDefault().postEvent(EventKeys.PRODUCT_BANNER_VIDEO_PAUSE, 0);
            }
            ViewUtil.swapView(holder.mLayoutVideoCover, holder.mImageBanner);
            ImageHelper.with(context).load(coverUrl, R.drawable.seat_goods1080x1080).listener(new ImageHelper.ImageLoaderListener() {

                @Override
                public void onLoadOk() {
                    if (callback != null) {
                        callback.loadFinish();
                    }
                }

                @Override
                public void onLoadError() {
                    if (callback != null) {
                        callback.loadFinish();
                    }
                }
            }).into(holder.mImageBanner);
        }

        if (!isVideo) {
            if (position == 0 && !TextUtils.isEmpty(flagUrl)) {
                holder.mImageFlag.setVisibility(View.VISIBLE);
                ImageHelper.with(context).load(flagUrl, R.drawable.seat_goods1080x1080).into(holder.mImageFlag);
            } else {
                holder.mImageFlag.setVisibility(View.GONE);
            }
        } else {
            if (position == 1 && !TextUtils.isEmpty(flagUrl)) {
                holder.mImageFlag.setVisibility(View.VISIBLE);
                ImageHelper.with(context).load(flagUrl, R.drawable.seat_goods1080x1080).into(holder.mImageFlag);
            } else {
                holder.mImageFlag.setVisibility(View.GONE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void startVideo(String coverUrl) {
        holder.videoView.setInitCover(coverUrl);
        holder.videoView.setVideoUri(Uri.parse(videoUrl));
    }

    public interface ImageLoaderCallback {
        void loadFinish();
    }

    public class ViewHolder {
        private ImageView mImageBanner;
        private SimpleVideoView videoView;
        private RelativeLayout mLayoutVideoCover;
        private LinearLayout mLayoutVideo;
        private ImageView mImageCover;
        private ImageView mImagePlay;

        private ImageView mImageFlag;
    }

}
