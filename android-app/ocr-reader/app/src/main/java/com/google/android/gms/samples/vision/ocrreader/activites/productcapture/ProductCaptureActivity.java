package com.google.android.gms.samples.vision.ocrreader.activites.productcapture;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.samples.vision.ocrreader.activites.blockcapture.BlockCaptureActivity;
import com.google.android.gms.samples.vision.ocrreader.index.Index;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

public class ProductCaptureActivity extends BlockCaptureActivity {
    private static final String TAG = "ProductCaptureActivity";
    public static String Index = "Index";

    com.google.android.gms.samples.vision.ocrreader.index.Index index;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        index = (Index) getIntent().getSerializableExtra(Index);

        Log.d(TAG, "Received " + index);
    }

    @Override
    protected Detector.Processor<TextBlock> createProcessor() {
        return new ProductCaptureProcessor(mGraphicOverlay, index);
    }


}
