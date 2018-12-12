package com.leyou.library.le_library.comm.view.banner.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by lis on 2017/2/24.
 */

public class NetworkBabyDressHolderView implements Holder<Integer> {
	private ImageView imageView;
	private int defaultImg;

	public NetworkBabyDressHolderView(int defaultBackgroundImg) {
		defaultImg = defaultBackgroundImg;
	}

	@Override
	public View createView(Context context) {
		imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		return imageView;
	}

	@Override
	public void UpdateUI(Context context, int position, Integer data) {
		imageView.setBackgroundResource(data);
		//ImageHelper.with(context).load(data.url, defaultImg).into(imageView);
	}
}
