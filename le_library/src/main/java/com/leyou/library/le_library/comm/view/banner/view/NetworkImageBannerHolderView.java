package com.leyou.library.le_library.comm.view.banner.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.leyou.library.le_library.comm.helper.ImageHelper;
import com.leyou.library.le_library.model.ImageVo;

/**
 * Created by lis on 2017/2/24.
 */

public class NetworkImageBannerHolderView implements Holder<ImageVo> {
	private ImageView imageView;
	private int defaultImg;
	private String mBannerTag;
	private int tagId;
	public NetworkImageBannerHolderView(int defaultBackgroundImg) {
		defaultImg = defaultBackgroundImg;
	}
	public NetworkImageBannerHolderView(int defaultBackgroundImg,int tag_id,String bannerTag) {
		defaultImg = defaultBackgroundImg;
		mBannerTag= bannerTag;
		tagId= tag_id;
	}
	@Override
	public View createView(Context context) {
		imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		return imageView;
	}

	@Override
	public void UpdateUI(Context context, int position, ImageVo data) {
//		if(!TextUtils.isEmpty(mBannerTag)){
//			imageView.setTag(tagId,mBannerTag+data.link);
//		}
		ImageHelper.with(context).load(data.url, defaultImg).centerCrop(false).into(imageView);
	}
}
