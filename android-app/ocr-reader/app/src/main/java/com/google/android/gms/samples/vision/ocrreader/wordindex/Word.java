package com.google.android.gms.samples.vision.ocrreader.wordindex;

import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

class Word extends Hashtable {

    private List<WordOccurence> occurences = new LinkedList<>();

    private String text;

    Word(String text) {
        this.text = text;
    }

    @Override
    public synchronized int hashCode() {
        return text.hashCode();
    }

    void addOccurence(String productCode, Rectangle normalizedRect) {
        occurences.add(new WordOccurence(productCode, normalizedRect));
    }
}
