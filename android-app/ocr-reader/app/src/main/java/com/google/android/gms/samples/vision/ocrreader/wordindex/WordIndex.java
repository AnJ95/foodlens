package com.google.android.gms.samples.vision.ocrreader.wordindex;

import android.graphics.Rect;

import com.google.android.gms.samples.vision.ocrreader.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class WordIndex {

    private Map<String, Word> words = new HashMap<>();


    public void addWordOccurence(String text, String productCode, Rectangle normalizedRect) {
        if (!words.containsKey(text)) {
            words.put(text, new Word(text));
        }

        words.get(text).addOccurence(productCode, normalizedRect);
    }

    @Override
    public String toString() {
        return "WordIndex(" + words.size() + " words)";
    }

}
