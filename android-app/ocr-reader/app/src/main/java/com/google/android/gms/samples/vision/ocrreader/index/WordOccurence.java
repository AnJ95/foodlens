package com.google.android.gms.samples.vision.ocrreader.index;

import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

import java.io.Serializable;

class WordOccurence implements Serializable {
    private String productCode;
    private Rectangle normalizedRect;

    protected WordOccurence(String productCode, Rectangle normalizedRect) {
        this.productCode = productCode;
        this.normalizedRect = normalizedRect;
    }

}
