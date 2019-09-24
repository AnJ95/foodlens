package com.google.android.gms.samples.vision.ocrreader.wordindex;

import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WordIndex implements Serializable {

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
