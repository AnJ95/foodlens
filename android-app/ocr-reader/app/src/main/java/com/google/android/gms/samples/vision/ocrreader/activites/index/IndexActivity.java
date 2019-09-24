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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.samples.vision.ocrreader.R;
import com.google.android.gms.samples.vision.ocrreader.graphic.OverlayGraphic;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

/**
 * Activity for the multi-tracker app.  This app detects text and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and contents of each TextBlock.
 */
public final class IndexActivity extends AppCompatActivity  {
    private static final String TAG = "IndexActivity";

    private ImageView mPreview;
    private GraphicOverlay<OverlayGraphic> mGraphicOverlay;
    private IndexProcessor processor;

    TextRecognizer textRecognizer;
    private ProgressBar mProgressBar;


    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.ocr_index);



        mPreview = (ImageView) findViewById(R.id.imageView);
        mGraphicOverlay = (GraphicOverlay<OverlayGraphic>) findViewById(R.id.graphicOverlay);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        processor = new IndexProcessor(mGraphicOverlay);
        textRecognizer = createTextRecognizer();


        //iterateFiles(getAssets(), "productImages");
        new Worker(getAssets(), "productImages").start();


    }


    class Worker extends Thread {

        private int currentProgress = -1;
        private Drawable image;
        private String productCode;
        private boolean currentWasDetected = true;

        private final String path;
        private final String[] fileList;

        Worker(AssetManager mgr, String path) {
            String[] fileList;
            try {
                fileList = mgr.list(path);
                mProgressBar.setMax(fileList.length);
            } catch (IOException e) {
                Log.v("List error:", "can't list" + path);
                fileList = null;
                mProgressBar.setMax(0);
            }
            this.fileList = fileList;
            this.path = path;

        }

        public void run() {
            while (true) {

                // check if to start next iteration/file
                if (currentWasDetected) {
                    if (!next()) {
                        break;
                    }
                }


                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // check if processor is done detecting
                if (processor.hasReceivedSinceCurrentProductCodeChanged()) {
                    currentWasDetected = true;
                }
            }

            // TODO Done
        }

        private boolean next() {
            // update iteration state
            currentWasDetected = false;
            currentProgress++;
            if (currentProgress >= fileList.length) {
                return false;
            }
            mProgressBar.setProgress(currentProgress);



            // update drawable
            String fileName = fileList[currentProgress];
            image = null;
            try {
                image = Drawable.createFromStream(getAssets().open(path + "/" + fileName), null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // update product code
            productCode = fileName.replaceAll("[^0-9]", "");


            Snackbar.make(mGraphicOverlay, currentProgress + "/" + fileList.length + " " + productCode,
                    Snackbar.LENGTH_LONG)
                    .show();

            processor.setCurrentProductCode(productCode);
            index(image);

            return true;
        }
    }



    private void index(final Drawable image) {
        // update view (must be in main thread)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPreview.setImageDrawable(image);
                setGraphicOverlaySize(image);
            }
        });

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
        textRecognizer.setProcessor(processor);

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
