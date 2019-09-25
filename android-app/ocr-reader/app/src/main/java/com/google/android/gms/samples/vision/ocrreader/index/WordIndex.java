package com.google.android.gms.samples.vision.ocrreader.index;

import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class WordIndex implements Serializable {

    Map<String, Word> words = new HashMap<>();

    WordIndex() {

    }

    WordOccurence addWordOccurence(String text, String productCode, Rectangle normalizedRect) {
        if (!words.containsKey(text)) {
            words.put(text, new Word(text));
        }

        return words.get(text).addOccurence(productCode, normalizedRect);
    }

    @Override
    public String toString() {
        return "WordIndex(" + words.size() + " words)";
    }

}
