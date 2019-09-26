package com.google.android.gms.samples.vision.ocrreader.index;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Product implements Serializable {
    public final String productCode;
    private final Map<Word, Set<WordOccurence>> occurences = new HashMap<>();

    Product(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public synchronized int hashCode() {
        return productCode.hashCode();
    }

    void addOccurence(Word word, WordOccurence occurence) {
        if (!occurences.containsKey(word)) { occurences.put(word, new HashSet<WordOccurence>()); }
        occurences.get(word).add(occurence);
    }

    public Set<Word> getWords() {
        return new HashSet<>(occurences.keySet());
    }

    public String toLongString() {
        List<String> words = new LinkedList<>();
        for (Word word : occurences.keySet()) {
            words.add(word.toString());
        }
        return "Product(" + productCode + ", " + occurences.size() + " words: {" + TextUtils.join(", ", words) + "})";
    }

    public Map<Word, Set<WordOccurence>> getWordsAndOccurences() {
        return occurences;
    }
}
