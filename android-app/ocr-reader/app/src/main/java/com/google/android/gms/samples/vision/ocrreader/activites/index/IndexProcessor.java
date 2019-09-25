/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.ocrreader.activites.index;


import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.samples.vision.ocrreader.activites.blockcapture.BlockCaptureProcessor;
import com.google.android.gms.samples.vision.ocrreader.graphic.OverlayGraphic;
import com.google.android.gms.samples.vision.ocrreader.index.Index;
import com.google.android.gms.samples.vision.ocrreader.primitive.Rectangle;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Element;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.LinkedList;
import java.util.List;

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class IndexProcessor extends BlockCaptureProcessor {

    public String currentProductCode;
    private boolean hasReceived = false;

    private Index index = new Index();


    IndexProcessor(GraphicOverlay<OverlayGraphic> ocrGraphicOverlay) {
        super(ocrGraphicOverlay);
    }


    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        super.receiveDetections(detections);
        SparseArray<TextBlock> textBlocks = detections.getDetectedItems();

        Rect minRect = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Rect maxRect = new Rect(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

        List<Element> allElements = new LinkedList<>();


        // iterate Elements in Lines in TextBlocks
        // keep track of minimum bounding rect to normalize
        for (int t = 0; t < textBlocks.size(); ++t) {
            // add TextBlock visually
            TextBlock textBlock = textBlocks.valueAt(t);
            for (Text line : textBlock.getComponents()) {
                for (Element elem : (Iterable<Element>) line.getComponents()) {
                    Rect rect = elem.getBoundingBox();

                    allElements.add(elem);

                    minRect = minRect(minRect, rect);
                    maxRect = maxRect(maxRect, rect);
                }
            }
        }

        // iterate all elements again to normalize and add to WordIndex
        for (Element elem : allElements) {
            Rect rect = elem.getBoundingBox();
            Rectangle normalizedRect = new Rectangle<Float>(
                    normalize(minRect.left, maxRect.left, rect.left),
                    normalize(minRect.top, maxRect.top, rect.top),
                    normalize(minRect.right, maxRect.right, rect.right),
                    normalize(minRect.bottom, maxRect.bottom, rect.bottom)
            );
            index.addWordOccurence(elem.getValue(), currentProductCode, normalizedRect);
            //Log.d("Detection", normalizedRect + ": " + elem.getValue());
        }

        Log.d("Detection", "detected " + allElements.size() + " words in " + currentProductCode + " => " + index.toString());

        hasReceived = true;
    }

    private float normalize(float min, float max, float value) {
        return (value - min) / (max - min);
    }

    // Note: Rectangle a is changed
    private Rect minRect(Rect a, Rect b) {
        a.top = Math.min(a.top, b.top);
        a.right = Math.min(a.right, b.right);
        a.bottom = Math.min(a.bottom, b.bottom);
        a.left = Math.min(a.left, b.left);
        return a;
    }
    // Note: Rectangle a is changed
    private Rect maxRect(Rect a, Rect b) {
        a.top = Math.max(a.top, b.top);
        a.right = Math.max(a.right, b.right);
        a.bottom = Math.max(a.bottom, b.bottom);
        a.left = Math.max(a.left, b.left);
        return a;
    }

    public void setCurrentProductCode(String currentProductCode) {
        this.hasReceived = false;
        this.currentProductCode = currentProductCode;
    }

    // best method name award incoming
    public boolean hasReceivedSinceCurrentProductCodeChanged() {
        return this.hasReceived;
    }

    public Index getIndex() {
        return index;
    }
}
