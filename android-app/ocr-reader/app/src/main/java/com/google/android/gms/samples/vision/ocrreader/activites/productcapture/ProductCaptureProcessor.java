package com.google.android.gms.samples.vision.ocrreader.activites.productcapture;

import android.graphics.Rect;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductCaptureProcessor extends BlockCaptureProcessor {
    private final Index mIndex;

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


        mGraphicOverlay.clear();
        for (Map.Entry<Product, Set<Word>> entry : matchingWords.entrySet()) {
            Product product = entry.getKey();
            Set<Word> wordsFound = entry.getValue();
            Set<Word> wordsExpected = mIndex.getWordsForProduct(product);

            // calc bounding box of product
            Rect productBB = null;

            // calc some metrics
            int numExpectedWords = wordsExpected.size();
            int numFoundWords = wordsFound.size();

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

            float fracOfExpectedWords = wordsExpected.size() / (float) (lengthBefore - lengthAfter);

            if (numFoundWords >= 3 || fracOfExpectedWords >= 0.5) {
                ProductGraphic graphic = new ProductGraphic(mGraphicOverlay, product, productBB);
                mGraphicOverlay.add(graphic);
            }
        }


        //OverlayGraphic graphic = new OverlayGraphic(mGraphicOverlay, product, unionRect);
        //mGraphicOverlay.add(graphic);

    }

}
