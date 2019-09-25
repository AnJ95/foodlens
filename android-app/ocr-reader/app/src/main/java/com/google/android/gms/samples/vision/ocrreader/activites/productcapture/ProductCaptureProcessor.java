package com.google.android.gms.samples.vision.ocrreader.activites.productcapture;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.samples.vision.ocrreader.activites.blockcapture.BlockCaptureProcessor;
import com.google.android.gms.samples.vision.ocrreader.graphic.ProductGraphic;
import com.google.android.gms.samples.vision.ocrreader.index.Index;
import com.google.android.gms.samples.vision.ocrreader.index.Product;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.samples.vision.ocrreader.index.Word;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Element;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class ProductCaptureProcessor extends BlockCaptureProcessor {
    private final Index mIndex;

    private static final String TAG = "ProductCaptureProcessor";

    public ProductCaptureProcessor(GraphicOverlay ocrGraphicOverlay, Index index) {
        super(ocrGraphicOverlay);
        this.mIndex = index;

    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {


        SparseArray<TextBlock> textBlocks = detections.getDetectedItems();

        Map<Product, Set<Word>> matchingWords = new HashMap<>();
        Map<Word, Rect> boundingBoxes = new HashMap<>();

        // iterate Elements in Lines in TextBlocks
        // keep track of minimum bounding rect to normalize
        // GOAL: fill matchingWords with a list of recognized words for each relevant product
        for (int t = 0; t < textBlocks.size(); ++t) {
            TextBlock textBlock = textBlocks.valueAt(t);
            for (Text line : textBlock.getComponents()) {
                for (Element elem : (Iterable<Element>) line.getComponents()) {
                    
                    Rect rect = elem.getBoundingBox();
                    String text = elem.getValue();

                    Word word = mIndex.tryGetWord(text);
                    if (word != null) {
                        List<Product> products = mIndex.getProductsWithWord(word);
                        for (Product product : products) {
                            // Create List for the product if not exists
                            if (!matchingWords.containsKey(product)) {
                                matchingWords.put(product, new HashSet<Word>());
                            }
                            // Add Word to this product
                            matchingWords.get(product).add(word);

                            boundingBoxes.put(word, rect);
                        }
                    }
                }
            }
        }


        Queue<PossibleMatch> matches = new PriorityQueue<>();
        // Iterate each product candidate
        //   and its respective words
        for (Map.Entry<Product, Set<Word>> entry : matchingWords.entrySet()) {
            Product product = entry.getKey();
            Set<Word> wordsFound = entry.getValue();
            Set<Word> wordsExpected = mIndex.getWordsForProduct(product);

            // calc bounding box of product
            Rect productBB = null;

            // calc some metrics
            int numExpectedWords = wordsExpected.size();
            int numFoundWords = wordsFound.size();

            // calc (lengthBefore - lengthAfter) to ignore unwanted duplicates in wordsFound
            int lengthBefore = wordsExpected.size();
            for (Word word : wordsFound) {
                wordsExpected.remove(word); // remove to count

                Rect wordBB = boundingBoxes.get(word);
                if (productBB == null) {
                    productBB = wordBB;
                } else {
                    productBB.union(wordBB);
                }

            }
            int lengthAfter = wordsExpected.size();

            // "How many of the expected Words fit?"
            float fracOfExpectedWords = (lengthBefore - lengthAfter) / (float) numExpectedWords;

            matches.add(new PossibleMatch(product, productBB, fracOfExpectedWords));
        }

        // Make sure nothing overlaps
        //   by starting with high confidence
        mGraphicOverlay.clear();
        PossibleMatch match;
        List<Rect> chosenRects = new LinkedList<>();
        while ((match = matches.poll()) != null) {

            // go to next if overlapping with previous
            boolean overlap = false;
            for (Rect chosenRect : chosenRects) {
                if (match.productBB.intersect(chosenRect)) {
                    overlap = true;
                }
            }

            if (!overlap) {
                mGraphicOverlay.add(match.createProductGraphic());
                chosenRects.add(match.productBB);
            }

        }

    }


    private class PossibleMatch implements Comparable<PossibleMatch> {
        private final Product product;
        private final Rect productBB;
        private final float confidence;

        private PossibleMatch(Product product, Rect productBB, float confidence) {
            this.product = product;
            this.productBB = productBB;
            this.confidence = confidence;
        }

        @Override
        public int compareTo(@NonNull PossibleMatch possibleMatch) {
            return -Float.compare(confidence, possibleMatch.confidence);
        }

        private ProductGraphic createProductGraphic() {
            return new ProductGraphic(mGraphicOverlay, product, productBB, confidence);
        }
    }
}
