package com.google.android.gms.samples.vision.ocrreader.index;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class ProductIndex implements Serializable {
    Map<String, Product> products = new HashMap<>();

    ProductIndex() {

    }

    public void addWordOcurence(String productCode, Word word, WordOccurence occurence) {
        if (!products.containsKey(productCode)) {
            products.put(productCode, new Product(productCode));
        }

        products.get(productCode).addOccurence(word, occurence);
    }

    @Override
    public String toString() {
        return "ProductIndex(" + products.size() + " products)";
    }
}
