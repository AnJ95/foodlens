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

package com.google.android.gms.samples.vision.ocrreader.activites;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.ocrreader.R;
import com.google.android.gms.samples.vision.ocrreader.Serializer;
import com.google.android.gms.samples.vision.ocrreader.activites.blockcapture.BlockCaptureActivity;
import com.google.android.gms.samples.vision.ocrreader.activites.index.IndexActivity;
import com.google.android.gms.samples.vision.ocrreader.activites.productcapture.ProductCaptureActivity;
import com.google.android.gms.samples.vision.ocrreader.wordindex.WordIndex;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * recognizes text.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // Use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;

    private static final int RC_INDEX = 9006;
    private static final int RC_BLOCK_CAPTURE = 9003;
    private static final int RC_PRODUCT_CAPTURE = 9009;

    private static final String TAG = "MainActivity";

    private static final String WORD_INDEX_PATH = "wordIndex.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue = (TextView)findViewById(R.id.text_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_text).setOnClickListener(this);
        findViewById(R.id.index_products).setOnClickListener(this);

        // show product detection only if WordIndex exists
        onWordIndexExistsChanged();

        //WordIndex index = new WordIndex();
        //index.addWordOccurence("A", "123", new Rectangle<Float>(0f,0f,0.5f,0.5f));
        //index.addWordOccurence("B", "123", new Rectangle<Float>(0.5f,0.5f,0f,0f));

        //Serializer<WordIndex> serializer = new Serializer<>();
        //serializer.save(getBaseContext(), WORD_INDEX_PATH, index);
        //WordIndex newWordIndex = new Serializer<>();serializer.load(getBaseContext(), WORD_INDEX_PATH);
        //Log.d("Serializer", newWordIndex.toString());
    }

    private void onWordIndexExistsChanged() {
        boolean wordIndexExists = Serializer.exists(getBaseContext(), WORD_INDEX_PATH);
        ((CheckBox)findViewById(R.id.are_products_indexed)).setChecked(wordIndexExists);
        findViewById(R.id.detect_products).setVisibility(wordIndexExists ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_products:
                Intent intent = new Intent(this, IndexActivity.class);
                startActivityForResult(intent, RC_INDEX);
                break;
            case R.id.read_text:
                intent = new Intent(this, BlockCaptureActivity.class);
                intent.putExtra(BlockCaptureActivity.AutoFocus, autoFocus.isChecked());
                intent.putExtra(BlockCaptureActivity.UseFlash, useFlash.isChecked());
                startActivityForResult(intent, RC_BLOCK_CAPTURE);
                break;
            case R.id.detect_products:
                intent = new Intent(this, ProductCaptureActivity.class);
                intent.putExtra(ProductCaptureActivity.WordIndex, new Serializer<WordIndex>().load(getBaseContext(), WORD_INDEX_PATH));
                startActivityForResult(intent, RC_PRODUCT_CAPTURE);
                break;
        }


    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_BLOCK_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(BlockCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        if(requestCode == RC_INDEX) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    WordIndex index = (WordIndex) data.getSerializableExtra(IndexActivity.WordIndex);

                    (new Serializer<WordIndex>()).save(getBaseContext(), WORD_INDEX_PATH, index);
                    onWordIndexExistsChanged();
                    Log.d(TAG, "Received WordIndex: " + index.toString());
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No WordIndex received, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
