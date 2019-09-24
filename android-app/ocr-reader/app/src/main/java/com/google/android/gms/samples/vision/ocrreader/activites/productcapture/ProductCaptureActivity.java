package com.google.android.gms.samples.vision.ocrreader.activites.productcapture;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.samples.vision.ocrreader.activites.blockcapture.BlockCaptureActivity;
import com.google.android.gms.samples.vision.ocrreader.wordindex.WordIndex;

public class ProductCaptureActivity extends BlockCaptureActivity {
    private static final String TAG = "ProductCaptureActivity";
    public static String WordIndex = "WordIndex";

    WordIndex wordIndex;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        wordIndex = (WordIndex)getIntent().getSerializableExtra(WordIndex);

        Log.d(TAG, "Received " + wordIndex);
    }
}
