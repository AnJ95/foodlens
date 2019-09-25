package com.google.android.gms.samples.vision.ocrreader.activites.productcapture;

import android.graphics.Rect;
import android.util.SparseArray;

import com.google.android.gms.samples.vision.ocrreader.activites.blockcapture.BlockCaptureProcessor;
import com.google.android.gms.samples.vision.ocrreader.graphic.OverlayGraphic;
import com.google.android.gms.samples.vision.ocrreader.index.Product;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.samples.vision.ocrreader.index.Word;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Element;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.HashMap;
import java.util.Map;

public class ProductCaptureProcessor extends BlockCaptureProcessor {
    public ProductCaptureProcessor(GraphicOverlay<OverlayGraphic> ocrGraphicOverlay) {
        super(ocrGraphicOverlay);
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {

        SparseArray<TextBlock> textBlocks = detections.getDetectedItems();

        Map<Product, Word> matchingWords = new HashMap<>();

        // iterate Elements in Lines in TextBlocks
        // keep track of minimum bounding rect to normalize
        for (int t = 0; t < textBlocks.size(); ++t) {
            // add TextBlock visually
            TextBlock textBlock = textBlocks.valueAt(t);
            for (Text line : textBlock.getComponents()) {
                for (Element elem : (Iterable<Element>) line.getComponents()) {
                    Rect rect = elem.getBoundingBox();

                }
            }
        }

    }
}
