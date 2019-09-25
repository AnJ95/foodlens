package com.google.android.gms.samples.vision.ocrreader.index;


import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

import java.io.Serializable;

public class Index implements Serializable {
    private WordIndex wordIndex = new WordIndex();
    private ProductIndex productIndex = new ProductIndex();

    public void addWordOccurence(String text, String productCode, Rectangle<Float> normalizedRect) {
        WordOccurence occurence = wordIndex.addWordOccurence(text, productCode, normalizedRect);
        productIndex.addWordOcurence(productCode, occurence);
    }

    @Override
    public String toString() {
        return wordIndex.toString() + " " + productIndex.toString();
    }
}
