package com.google.android.gms.samples.vision.ocrreader.graphic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.android.gms.samples.vision.ocrreader.index.Product;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;


public class ProductGraphic extends GraphicOverlay.Graphic   {

    private int mId;

    private Paint sRectPaint;
    private Paint sTextPaint;
    private final Product mProduct;
    private final Rect mRect;

    private final static int MIN_ALPHA = 80;
    private final float confidence;



    public ProductGraphic(GraphicOverlay overlay, Product product, Rect rect, float confidence) {
        super(overlay);

        mProduct = product;
        mRect = rect;
        this.confidence = confidence;

        int color = Color.argb(MIN_ALPHA + Math.round(clamp(confidence, 0, 1) * (255-MIN_ALPHA)), 255, 255, 255);
        if (sRectPaint == null) {
            sRectPaint = new Paint();
            sRectPaint.setColor(color);
            sRectPaint.setStyle(Paint.Style.STROKE);
            sRectPaint.setStrokeWidth(4.0f);
        }

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(color);
            sTextPaint.setTextSize(84.0f);
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    private float clamp(float val, float min, float max) {
        return val < min ? min : (val > max ? max : val);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }


    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    public boolean contains(float x, float y) {
        if (mRect == null) {
            return false;
        }
        RectF rect = new RectF(mRect);
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
    }

    @Override
    public String getText() {
        if (mProduct == null) {
            return null;
        }
        return mProduct.productCode;
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        if (mRect == null || mProduct == null) {
            return;
        }

        RectF rect = new RectF(mRect);
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);

        canvas.drawRect(rect, sRectPaint);
        canvas.drawText(mProduct.productCode, rect.left, rect.bottom, sTextPaint);
        canvas.drawText(Float.toString(Math.round(confidence * 100f) / 100f), rect.right, rect.top, sTextPaint);

    }
}
