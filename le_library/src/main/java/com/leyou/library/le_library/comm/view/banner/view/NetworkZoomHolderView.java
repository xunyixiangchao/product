package com.leyou.library.le_library.comm.view.banner.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.leyou.library.le_library.comm.helper.ImageHelper;
import com.leyou.library.le_library.comm.view.photoview.PhotoView;

/**
 * Created by ss on 2016/8/22.
 */
public class NetworkZoomHolderView implements Holder<String> {
	private PhotoView imageView;
	private int defaultImg;

	public NetworkZoomHolderView(int defaultBackgroundImg) {
		defaultImg = defaultBackgroundImg;
	}

	@Override
	public View createView(final Context context) {
		imageView = new PhotoView(context);
		imageView.setAdjustViewBounds(true);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) context).finish();
			}
		});
		imageView.enable();
		return imageView;
	}

	@Override
	public void UpdateUI(Context context, int position, String data) {
		ImageHelper.with(context).load(data, defaultImg).centerCrop(false).into(imageView);
	}
}
