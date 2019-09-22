package com.google.android.gms.samples.vision.ocrreader.wordindex;

import android.graphics.Rect;

import com.google.android.gms.samples.vision.ocrreader.Rectangle;

class WordOccurence {
    private int productCode;
    private Rectangle normalizedRect;

    protected WordOccurence(int productCode, Rectangle normalizedRect) {
        this.productCode = productCode;
        this.normalizedRect = normalizedRect;
    }

}
