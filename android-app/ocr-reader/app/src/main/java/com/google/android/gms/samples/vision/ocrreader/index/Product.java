package com.google.android.gms.samples.vision.ocrreader.index;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {
    public final String productCode;
    private final Map<String, WordOccurence> occurences = new HashMap<>();

    Product(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public synchronized int hashCode() {
        return productCode.hashCode();
    }

    void addOccurence(String text, WordOccurence occurence) {
        occurences.put(text, occurence);
    }
}
