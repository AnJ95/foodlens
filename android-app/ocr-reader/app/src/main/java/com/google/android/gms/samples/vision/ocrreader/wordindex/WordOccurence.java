package com.google.android.gms.samples.vision.ocrreader.wordindex;

import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

class WordOccurence {
    private String productCode;
    private Rectangle normalizedRect;

    protected WordOccurence(String productCode, Rectangle normalizedRect) {
        this.productCode = productCode;
        this.normalizedRect = normalizedRect;
    }

}
