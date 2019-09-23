package com.google.android.gms.samples.vision.ocrreader.wordindex;

import android.graphics.Rect;

import com.google.android.gms.samples.vision.ocrreader.Rectangle;

class WordOccurence {
    private String productCode;
    private Rectangle normalizedRect;

    protected WordOccurence(String productCode, Rectangle normalizedRect) {
        this.productCode = productCode;
        this.normalizedRect = normalizedRect;
    }

}
