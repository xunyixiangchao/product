package com.leyou.library.le_library.comm.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Glide 圆角 Transform
 * Created by ss on 20f16/10f/17.
 */

public class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 0f;
    private float diameter;
    private CornerType mCornerType;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int dp) {
        this(context, dp, CornerType.ALL);
    }

    public GlideRoundTransform(Context context, int dp, CornerType cornerType) {
        super(context);
        radius = Resources.getSystem().getDisplayMetrics().density * dp;
        diameter = 2 * radius;
        mCornerType = cornerType;
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
//        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
//        canvas.drawRoundRect(rectF, radius, radius, paint);
        drawRoundRect(canvas, paint, source.getWidth(), source.getHeight());
        return result;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }

    public enum CornerType {
        ALL,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        TOP, BOTTOM, LEFT, RIGHT,
        OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
        DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
    }


    private void drawRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        switch (mCornerType) {
            case ALL:
                canvas.drawRoundRect(new RectF(0f, 0f, right, bottom), radius, radius, paint);
                break;
            case TOP_LEFT:
                drawTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case TOP_RIGHT:
                drawTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_LEFT:
                drawBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_RIGHT:
                drawBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case TOP:
                drawTopRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM:
                drawBottomRoundRect(canvas, paint, right, bottom);
                break;
            case LEFT:
                drawLeftRoundRect(canvas, paint, right, bottom);
                break;
            case RIGHT:
                drawRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_LEFT:
                drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_RIGHT:
                drawOtherTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_LEFT:
                drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_RIGHT:
                drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_LEFT:
                drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_RIGHT:
                drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
                break;
            default:
                canvas.drawRoundRect(new RectF(0f, 0f, right, bottom), radius, radius, paint);
                break;
        }
    }

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(0f, 0f, diameter, diameter),
                radius, radius, paint);
        canvas.drawRect(new RectF(0f, radius, radius, bottom), paint);
        canvas.drawRect(new RectF(radius, 0f, right, bottom), paint);
    }

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, 0f, right, diameter), radius,
                radius, paint);
        canvas.drawRect(new RectF(0f, 0f, right - radius, bottom), paint);
        canvas.drawRect(new RectF(right - radius, radius, right, bottom), paint);
    }

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(0f, bottom - diameter, diameter, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(0f, 0f, diameter, bottom - radius), paint);
        canvas.drawRect(new RectF(radius, 0f, right, bottom), paint);
    }

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, bottom - diameter, right, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(0f, 0f, right - radius, bottom), paint);
        canvas.drawRect(new RectF(right - radius, 0f, right, bottom - radius), paint);
    }

    private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(0f, 0f, right, diameter), radius, radius,
                paint);
        canvas.drawRect(new RectF(0f, radius, right, bottom), paint);
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(0f, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(0f, 0f, right, bottom - radius), paint);
    }

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(0f, 0f, diameter, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(radius, 0f, right, bottom), paint);
    }

    private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, 0f, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(0f, 0f, right - radius, bottom), paint);
    }

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(0f, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(right - diameter, 0f, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(0f, 0f, right - radius, bottom - radius), paint);
    }

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(0f, 0f, diameter, bottom), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(0f, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(radius, 0f, right, bottom - radius), paint);
    }

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(0f, 0f, right, diameter), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(right - diameter, 0f, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(0f, radius, right - radius, bottom), paint);
    }

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right,
                                               float bottom) {
        canvas.drawRoundRect(new RectF(0f, 0f, right, diameter), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(0f, 0f, diameter, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(radius, radius, right, bottom), paint);
    }

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right,
                                                  float bottom) {
        canvas.drawRoundRect(new RectF(0f, 0f, diameter, diameter),
                radius, radius, paint);
        canvas.drawRoundRect(new RectF(right - diameter, bottom - diameter, right, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(0f, radius, right - diameter, bottom), paint);
        canvas.drawRect(new RectF(diameter, 0f, right, bottom - radius), paint);
    }

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right,
                                                   float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, 0f, right, diameter), radius,
                radius, paint);
        canvas.drawRoundRect(new RectF(0f, bottom - diameter, diameter, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(0f, 0f, right - radius, bottom - radius), paint);
        canvas.drawRect(new RectF(radius, radius, right, bottom), paint);
    }
}