package com.google.android.gms.samples.vision.ocrreader.index;

import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

import java.io.Serializable;

public class WordOccurence implements Serializable {
    private String productCode;



    private Rectangle normalizedRect;

    protected WordOccurence(String productCode, Rectangle normalizedRect) {
        this.productCode = productCode;
        this.normalizedRect = normalizedRect;
    }

    public String getProductCode() {
        return productCode;
    }

    public Rectangle getNormalizedRect() {
        return normalizedRect;
    }

    @Override
    public int hashCode() {
        return 13*productCode.hashCode() + 23*normalizedRect.hashCode();
    }
}
