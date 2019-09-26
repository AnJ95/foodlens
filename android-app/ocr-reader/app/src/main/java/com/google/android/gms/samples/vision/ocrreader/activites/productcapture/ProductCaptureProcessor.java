package com.google.android.gms.samples.vision.ocrreader.activites.productcapture;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.samples.vision.ocrreader.activites.blockcapture.BlockCaptureProcessor;
import com.google.android.gms.samples.vision.ocrreader.graphic.ProductGraphic;
import com.google.android.gms.samples.vision.ocrreader.index.Index;
import com.google.android.gms.samples.vision.ocrreader.index.Product;
import com.google.android.gms.samples.vision.ocrreader.index.WordOccurence;
import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;
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

        Map<Product, Map<Word, Set<Rect>>> matchingWords = new HashMap<>();

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
                            // Create Map for the product if not exists
                            if (!matchingWords.containsKey(product)) { matchingWords.put(product, new HashMap<Word, Set<Rect>>()); }
                            // Create Set for this words found bounding boxes (when same word found multiple times!)
                            if (!matchingWords.get(product).containsKey(word)) { matchingWords.get(product).put(word, new HashSet<Rect>()); }
                            // Add Rect of this Word to the product
                            matchingWords.get(product).get(word).add(rect);
                        }
                    }
                }
            }
        }


        Queue<PossibleMatch> matches = new PriorityQueue<>();
        // Iterate each product candidate
        //   and create data structure for possible match
        for (Map.Entry<Product, Map<Word, Set<Rect>>> entry : matchingWords.entrySet()) {
            Product product = entry.getKey();
            Map<Word, Set<Rect>> foundWordBoundingBoxes = entry.getValue();

            matches.add(new PossibleMatch(product, foundWordBoundingBoxes));
        }

        PossibleMatch match;
        /*
        Queue<PossibleMatch> reducedMatches = new PriorityQueue<>();
        while ((match = matches.poll()) != null) {
            Map<Word, Set<Rect>> found = match.possibleWords;
            Map<Word, Set<WordOccurence>> expected = match.product.getWordsAndOccurences();
            //match.product.getWordsAndOccurences()
        }
        */

        // Make sure nothing overlaps
        //   by starting with high confidence
        //   and removing PossibleMatches that intersect
        //   with previous PossibleMatches
        mGraphicOverlay.clear();
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
        private Rect productBB;
        private float confidence;
        private Map<Word, Set<Rect>> possibleWords;

        private PossibleMatch(Product product, Map<Word, Set<Rect>> possibleWords) {
            this.product = product;
            this.possibleWords = possibleWords;
            this.update();
        }

        private void update() {
            // update bounding box
            productBB = null;
            for (Set<Rect> rects : possibleWords.values()) {
                for (Rect rect : rects) {
                    if (productBB == null) {
                        productBB = rect;
                    } else {
                        productBB.union(rect);
                    }
                }
            }


            // calc confidence
            //   by checking how many of the expected words are found
            //   beware: product could have same word multiple times!
            int numExpectedWords = 0;
            int numFoundWords = 0;
            for (Word word : product.getWordsAndOccurences().keySet()) {
                int numOccurencesOfThisWord = product.getWordsAndOccurences().get(word).size();
                numExpectedWords += numOccurencesOfThisWord;
                numFoundWords += Math.min(numOccurencesOfThisWord,
                        possibleWords.containsKey(word) ? possibleWords.get(word).size() : 0
                );
            }
            confidence = numFoundWords / (float) numExpectedWords;



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
