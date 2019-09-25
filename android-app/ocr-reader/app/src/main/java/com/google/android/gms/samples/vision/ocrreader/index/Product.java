package com.google.android.gms.samples.vision.ocrreader.index;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Product implements Serializable {
    public final String productCode;
    private final Map<Word, WordOccurence> occurences = new HashMap<>();

    Product(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public synchronized int hashCode() {
        return productCode.hashCode();
    }

    void addOccurence(Word word, WordOccurence occurence) {
        occurences.put(word, occurence);
    }

    public Set<Word> getWords() {
        return new HashSet<>(occurences.keySet());
    }
}
