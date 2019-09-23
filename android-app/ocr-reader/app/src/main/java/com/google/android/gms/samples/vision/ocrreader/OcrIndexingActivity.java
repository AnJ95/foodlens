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
package com.google.android.gms.samples.vision.ocrreader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.images.Size;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.CameraSourcePreview;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;

/**
 * Activity for the multi-tracker app.  This app detects text and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and contents of each TextBlock.
 */
public final class OcrIndexingActivity extends AppCompatActivity  {
    private static final String TAG = "OcrIndexingActivity";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    private ImageView mPreview;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private OcrIndexingProcessor processor;

    TextRecognizer textRecognizer;


    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.ocr_index);


        mPreview = (ImageView) findViewById(R.id.imageView);
        mGraphicOverlay = (GraphicOverlay<OcrGraphic>) findViewById(R.id.graphicOverlay);
        processor = new OcrIndexingProcessor(mGraphicOverlay);
        textRecognizer = createTextRecognizer();

        iterateFiles(getAssets(), "productImages");

        /*Snackbar.make(mGraphicOverlay, "Tap to capture. Pinch/Stretch to zoom",
                Snackbar.LENGTH_LONG)
                .show();*/
        //index(R.drawable.front_010430652874676217); // landscape
        //index(R.drawable.front_2000000032318); // portrait
    }



    private void iterateFiles(AssetManager mgr, String path) {
        try {
            String list[] = mgr.list(path);
            if (list != null) {

                for (String file : list) {

                    Drawable image = Drawable.createFromStream(getAssets().open(path + "/" + file), null);

                    if (image != null) {
                        // front_03481514.jpg => 03481514
                        String productCode = file.replaceAll("[^0-9]", "");
                        index(image, productCode);

                    }
                }
            }
        } catch (IOException e) {
            Log.v("List error:", "can't list" + path);
        }

    }

    private void index(Drawable image, String productCode) {
        // update view
        mPreview.setImageDrawable(image);
        setGraphicOverlaySize(image);

        // set current product code; used during text recognition
        processor.setCurrentProductCode(productCode); // TODO is receiveFrame even sync?

        // detect text
        Frame frame = new Frame.Builder().setBitmap(drawableToBitmap(image)).build();
        textRecognizer.receiveFrame(frame);
        textRecognizer.detect(frame);
    }

    private void setGraphicOverlaySize(Drawable image) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double imageHeight = image.getIntrinsicHeight();
        double imageWidth = image.getIntrinsicWidth();
        double screenHeight = displayMetrics.heightPixels + getNavigationBarHeight();
        double screenWidth = displayMetrics.widthPixels;

        if (screenWidth/screenHeight > imageWidth/imageHeight) { // stripes left and right
            double newWidth = imageHeight / (screenHeight / screenWidth);
            double v_to_s_factor = imageHeight / screenHeight;
            double newLeft = (screenWidth - imageWidth/v_to_s_factor) / 2.0;
            mGraphicOverlay.setCameraInfo((int)Math.round(newWidth), (int)Math.round(imageHeight), (int)Math.round(newLeft), 0, CameraSource.CAMERA_FACING_BACK);

        } else { // stripes above and below
            double newHeight = imageWidth / (screenWidth / screenHeight);
            double v_to_s_factor = imageWidth / screenWidth;
            double newTop = (screenHeight - imageHeight/v_to_s_factor) / 2.0;
            mGraphicOverlay.setCameraInfo((int)Math.round(imageWidth), (int)Math.round(newHeight), 0, (int)Math.round(newTop), CameraSource.CAMERA_FACING_BACK);
        }
    }



    // https://stackoverflow.com/questions/4743116/get-screen-width-and-height-in-android
    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }





    @SuppressLint("InlinedApi")
    private TextRecognizer createTextRecognizer() {
        Context context = getApplicationContext();

        // A text recognizer is created to find text.  An associated processor instance
        // is set to receive the text recognition results and display graphics for each text block
        // on screen.
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        textRecognizer.setProcessor(new OcrIndexingProcessor(mGraphicOverlay));

        if (!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        return textRecognizer;
    }



    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }




    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {

        }
    }



}
