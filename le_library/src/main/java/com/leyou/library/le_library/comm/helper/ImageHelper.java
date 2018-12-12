package com.leyou.library.le_library.comm.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ichsy.libs.core.comm.utils.ThreadPoolUtil;

import java.util.concurrent.ExecutionException;

/**
 * 图片下载的中转类
 * Created by liuyuhang on 16/5/23.
 */
public class ImageHelper {

//    private static ImageHelper instance;

	private Context context;
	private String url;
	private int defaultImageId;
	private ImageLoaderListener listener;
	private boolean isCenterCrop = true;
	private BitmapTransformation[] transformations;
	private boolean isCache = true;
	private boolean asBitmap;

	public ImageHelper(Context context) {
		this.context = context;
	}

	public static ImageHelper with(Context context) {
		return new ImageHelper(context);
	}

	/**
	 * 加载图片
	 *
	 * @param url            图片url
	 * @param placeHolderRid 占位图
	 * @return
	 */
	public ImageHelper load(String url, int placeHolderRid) {
		this.url = url;
		this.defaultImageId = placeHolderRid;
		return this;
	}

	public ImageHelper load(String url, int placeHolderRid, boolean isCache) {
		this.url = url;
		this.defaultImageId = placeHolderRid;
		this.isCache = isCache;
		return this;

	}

	public ImageHelper listener(ImageLoaderListener listener) {
		this.listener = listener;
		return this;
	}

	public ImageHelper centerCrop(boolean centerCrop) {
		this.isCenterCrop = centerCrop;
		return this;
	}

	public ImageHelper asBitmap(boolean asBitmap) {
		this.asBitmap = asBitmap;
		return this;
	}

	public ImageHelper transform(BitmapTransformation... transformations) {
		this.transformations = transformations;
		return this;
	}

	/**
	 * 加载图片到view上
	 *
	 * @param rid view的id
	 */
	public void into(int rid) {
		ImageView imageView = (ImageView) ((Activity) context).findViewById(rid);
		into(imageView);
	}

	public TextView into(ImageView imageView) {
		DrawableTypeRequest<String> request = Glide.with(context.getApplicationContext()).load(url);
		if (isCache) {
			request.diskCacheStrategy(DiskCacheStrategy.ALL);
			request.skipMemoryCache(false);
		} else {
			request.diskCacheStrategy(DiskCacheStrategy.NONE);
			request.skipMemoryCache(true);
		}
		request.crossFade()
				.error(defaultImageId)
				.placeholder(defaultImageId);

		if (listener != null) {
			request.listener(new RequestListener<String, GlideDrawable>() {
				@Override
				public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
					listener.onLoadError();
					return false;
				}

				@Override
				public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
					listener.onLoadOk();
					return false;
				}
			});
		}

		if (isCenterCrop) {
			request.centerCrop();
		}

		if (transformations != null) {
			request.transform(transformations);
		}
		if (asBitmap) {
			request.asBitmap().into(imageView);
		} else {
			request.into(imageView);
		}

        return null;
    }

	public static void downloadFromUrl(final Context context, final String url, final int width, final int height, final ImageDownloadCallback callback) {
		ThreadPoolUtil.getInstance().fetchData(new Runnable() {
			@Override
			public void run() {
				try {
					Bitmap myBitmap = Glide.with(context)
							                  .load(url)
							                  .asBitmap()
							                  .centerCrop()
							                  .into(width, height)
							                  .get();
//                    bottomLayout.setBackgroundDrawable(new BitmapDrawable(myBitmap));
					callback.onImageDownload(myBitmap);
				} catch (InterruptedException | ExecutionException e) {
					callback.onImageDownload(null);
					e.printStackTrace();
				}
			}
		});
	}

	public interface ImageDownloadCallback {
		void onImageDownload(Bitmap drawable);
	}

	public interface ImageLoaderListener {
		void onLoadOk();

		void onLoadError();
	}
}
