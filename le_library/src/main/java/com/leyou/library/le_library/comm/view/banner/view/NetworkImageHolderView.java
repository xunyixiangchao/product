package com.leyou.library.le_library.comm.view.banner.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.leyou.library.le_library.comm.helper.ImageHelper;

public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;
    private int defaultImg;

    public NetworkImageHolderView(int defaultBackgroundImg) {
        defaultImg = defaultBackgroundImg;
    }

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        ImageHelper.with(context).load(data, defaultImg).into(imageView);
    }
}
