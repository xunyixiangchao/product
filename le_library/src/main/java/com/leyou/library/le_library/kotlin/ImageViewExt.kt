package com.leyou.library.le_library.kotlin

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.leyou.library.le_library.model.ImageVo


/**
 * 从指定的url读取图片
 */

fun ImageView.loadFromUrl(context: Context, defaultImageId: Int, url: String?) {
    if (null == url) {
        setBackgroundResource(defaultImageId)
    } else {
        val request = Glide.with(context.applicationContext).load(url)

//    if (listener != null) {
//        request.listener(object : RequestListener<String, GlideDrawable> {
//            override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
//                return false
//            }
//
//            override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
//                listener.onLoadOk()
//                return false
//            }
//        })
//    }

        scaleType = ImageView.ScaleType.CENTER_CROP
        request.diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .error(defaultImageId)
                .placeholder(defaultImageId)

//    if (transformations != null) {
//        request.transform(*transformations)
//    }
        request.into(this)
    }


}

/**
 * 通过imageVo填充view，可以根据服务端返回的宽高来调整view自适应大小
 */
fun ImageView?.loadFromUrl(context: Context, defaultImageId: Int, imageVo: ImageVo) {
    if (this@loadFromUrl == null) {
        return
    }

    loadFromUrl(context, defaultImageId, imageVo.url)

    post {
        if (imageVo.width != 0 && imageVo.high != 0) {
            val angle = imageVo.high.toFloat() / imageVo.width.toFloat()
            val viewWidth = measuredWidth
            val viewHeight = viewWidth * angle

            val layoutParams = layoutParams
            layoutParams.width = viewWidth
            layoutParams.height = viewHeight.toInt()
            setLayoutParams(layoutParams)
            scaleType = ImageView.ScaleType.FIT_XY
        }
    }
}
