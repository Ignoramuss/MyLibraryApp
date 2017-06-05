package com.ignoramuss.foo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;

import android.content.Intent;
import android.content.res.Configuration;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;
import android.view.KeyEvent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView formatTxt, contentTxt, alertTxt;
    private CompoundBarcodeView mBarcodeView;
    private BeepManager beepManager;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                handleDecode(result);
            }
        }

        @Override
        public void possibleResultPoints(List resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        beepManager = new BeepManager(this);

        final MainActivity activity = this;

        Button scan = (Button) findViewById(R.id.scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setContentView(R.layout.scan_activity);
//                mBarcodeView = (CompoundBarcodeView) findViewById(R.id.scanner);
//                mBarcodeView.decodeContinuous(callback);
//                mBarcodeView.resume();

                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setCaptureActivity(ContinuousCaptureActivity.class);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        setContentView(R.layout.activity_main);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
//            String scanContent = scanResult.getContents();
//            String scanFormat = scanResult.getFormatName();
//            formatTxt.setText("FORMAT: " + scanFormat);
//            contentTxt.setText("CONTENT: " + scanContent);
//            Log.d("code", re);
        } else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
        // else continue with any other code you need in the method
    }

//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.d("HE BAGH", "LOL");
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Log.d("HE BAGH", "LOL");
////            IntentIntegrator integrator = new IntentIntegrator(this);
////            integrator.setOrientationLocked(false);
////            integrator.initiateScan();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
////            setContentView(R.layout.scan_activity);
////            mBarcodeView = (CompoundBarcodeView) findViewById(R.id.scanner);
////            mBarcodeView.decodeContinuous(callback);
////            mBarcodeView.resume();
//        }
//    }

    public void handleDecode(BarcodeResult rawResult) {
//        mBarcodeView.pause();//Pause preview
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        formatTxt = (TextView)findViewById(R.id.scan_format);
//        contentTxt = (TextView)findViewById(R.id.scan_content);
        alertTxt = (TextView)findViewById(R.id.scan_alert);
        String scanContent = rawResult.getText();
        String scanFormat = rawResult.getBarcodeFormat().toString();
//        formatTxt.setText("FORMAT: " + scanFormat);
//        contentTxt.setText("CONTENT: " + scanContent);
        alertTxt.setText(scanContent);

        beepManager.playBeepSoundAndVibrate();

//        DialogInterface.OnClickListener dialogOkClick = new DialogInterface.OnClickListener() { // OK
//            // button
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if (writeNote) {
//                    EditText txtNote = (EditText) promptsView.findViewById(R.id.txt_dialog_note);
//                    //code to merge value of txtNote with result
//                }
//
//                dialog.dismiss();
//                mBarcodeView.resume();//Resume scanning after pressing confirm button
//                Toast.makeText(MainActivity.this, R.string.dialog_save_qr_alert, Toast.LENGTH_SHORT).show();
//            }
//        };
//        AlertDialog dialog = DialogHelper.CreateDialog(this, DialogHelper.SAVE_QR_CODE, result, dialogOkClick, dialogCancelClick, promptsView);
//        dialog.show();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        mBarcodeView.resume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        mBarcodeView.pause();
//    }
//
//    public void pause(View view) {
//        mBarcodeView.pause();
//    }
//
//    public void resume(View view) {
//        mBarcodeView.resume();
//    }
//
//    public void triggerScan(View view) {
//        mBarcodeView.decodeSingle(callback);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return mBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}

