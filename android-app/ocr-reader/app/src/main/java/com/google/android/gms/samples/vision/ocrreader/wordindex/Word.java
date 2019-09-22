package com.google.android.gms.samples.vision.ocrreader.wordindex;

import android.graphics.Rect;

import com.google.android.gms.samples.vision.ocrreader.Rectangle;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

class Word extends Hashtable {

    private List<WordOccurence> occurences = new LinkedList<>();

    private String text;

    public Word(String text) {
        this.text = text;
    }

    @Override
    public synchronized int hashCode() {
        return text.hashCode();
    }

    public void addOccurence(int productCode, Rectangle normalizedRect) {
        occurences.add(new WordOccurence(productCode, normalizedRect));
    }
}
