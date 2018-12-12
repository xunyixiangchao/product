package com.leyou.library.le_library.comm.view.banner.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.leyou.library.le_library.comm.helper.ImageHelper;

public class NetworkFullScreenImageHolderView implements Holder<String> {
    private ImageView imageView;
    private int defaultImg;

    public NetworkFullScreenImageHolderView(int defaultBackgroundImg) {
        defaultImg = defaultBackgroundImg;
    }

    @Override
    public View createView(final Context context) {
        imageView = new ImageView(context);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        ImageHelper.with(context).asBitmap(true).centerCrop(false).load(data, defaultImg).into(imageView);
    }
}
