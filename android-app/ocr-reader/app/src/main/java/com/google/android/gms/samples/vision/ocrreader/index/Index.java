package com.google.android.gms.samples.vision.ocrreader.index;


import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Index implements Serializable {
    private WordIndex wordIndex = new WordIndex();
    private ProductIndex productIndex = new ProductIndex();

    public void addWordOccurence(String text, String productCode, Rectangle<Float> normalizedRect) {
        WordOccurence occurence = wordIndex.addWordOccurence(text, productCode, normalizedRect);
        Word word = wordIndex.words.get(text);

        productIndex.addWordOcurence(productCode, word, occurence);
    }

    @Override
    public String toString() {
        return wordIndex.toString() + " " + productIndex.toString();
    }

    public Word tryGetWord(String text) {
        if (wordIndex.words.containsKey(text)) {
            return wordIndex.words.get(text);
        }
        return null;
    }

    public List<Product> getProductsWithWord(Word word) {
        List<Product> products = new LinkedList<>();

        List<WordOccurence> occurences = word.getOccurences();
        for (WordOccurence occurence : occurences) {
            Product product = productIndex.products.get(occurence.getProductCode());
            products.add(product);
        }

        return products;
    }

    public Set<Word> getWordsForProduct(Product product) {
        return productIndex.products.get(product.productCode).getWords();
    }
}
