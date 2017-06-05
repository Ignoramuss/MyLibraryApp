package com.ignoramuss.foo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @Copyright 2017, ignoramuss, All rights reserved
 * Modification of ContinuousCaptureActivity.java from zxing-android-embedded repository
 */

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    public DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    final private String API_KEY = BuildConfig.API_KEY;
    final private String serverAPI = BuildConfig.serverAPIAddress;
    final private String API_CALL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    public String bookName, thumbnailURL;
    public volatile boolean dependency_satisfied = false;
//    public Bitmap bitmap;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(final BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            final ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            final Bitmap bitmap = result.getBitmapWithResultPoints(Color.YELLOW);
            String books_api = API_CALL + result.getText() + "&key=" + API_KEY;
//            String BookName = new RetrieveFeedTask(API_CALL + result.getText() + "&key=" + API_KEY, result.getText()).retrieve(barcodeView, beepManager, bitmap, imageView);
            AsyncRequest books_api_call = new AsyncRequest(){
                @Override
                public void onPostExecute(byte[] data) {
                    try{
                        String response = new String(data, "UTF-8");
                        if(response == null){
                            bookName = "Unknown Book";
                        } else {
                            JSONObject result = new JSONObject(response);
                            JSONArray items = result.getJSONArray("items");
                            if(items != null){
                                JSONObject jsonObject = items.getJSONObject(0);
                                bookName = jsonObject.getJSONObject("volumeInfo").optString("title");
                                thumbnailURL = jsonObject.getJSONObject("volumeInfo").getJSONObject("imageLinks").optString("thumbnail");
                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        barcodeView.setStatusText(bookName);
                        beepManager.playBeepSoundAndVibrate();
                        imageView.setImageBitmap(bitmap);
                        String server_request = serverAPI + "cgi-bin/store_in_database.py?ISBN=" + result.getText();
                        AsyncRequest server_call = new AsyncRequest();
                        server_call.execute(server_request);
                        AsyncRequest image_retrieve = new AsyncRequest(){
                            @Override
                            public void onPostExecute(byte[] data) {
                                Bitmap thumbnail = BitmapFactory.decodeByteArray(data, 0, data.length);
                                if(thumbnail != null){
                                    imageView.setImageBitmap(thumbnail);
                                }
                            }
                        };
                        image_retrieve.execute(thumbnailURL);
                    }
                }
            };
            books_api_call.execute(books_api);

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.continuous_scan);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        barcodeView.resume();

        beepManager = new BeepManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
