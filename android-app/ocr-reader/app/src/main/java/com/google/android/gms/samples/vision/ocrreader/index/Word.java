package com.google.android.gms.samples.vision.ocrreader.index;

import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Word implements Serializable {

    private List<WordOccurence> occurences = new LinkedList<>();

    private String text;

    Word(String text) {
        this.text = text;
    }

    @Override
    public synchronized int hashCode() {
        return text.hashCode();
    }

    @Override
    public String toString() {
        return text;
    }

    WordOccurence addOccurence(String productCode, Rectangle normalizedRect) {
        WordOccurence occurence = new WordOccurence(productCode, normalizedRect);
        occurences.add(occurence);
        return occurence;
    }

    public List<WordOccurence> getOccurences() {
        return occurences;
    }
}
