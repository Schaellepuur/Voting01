package ch.stadtzug.geja.voting01;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    TextView qr_inhalt;
    final int requestCameraPermissionID = 1001;

    String qr_daten;
    String url;

    //Variabeln für VotingActivity
    String abstimmungsID;
    String authkey;
    String abstimmungsText = "Keine Abstimmung gefunden.";
    String option1;
    String option2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case requestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        qr_inhalt = (TextView) findViewById(R.id.qr_inhalt);
        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        //Kamera starten für das Scannen des QR-Code
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Berechtigung abfragen
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, requestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });

        //BarcodeScanner erstellen und beim erkennen einen http GET Call aufrufen, um ein JSON Objekt zu erhalten
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0)
                {
                    qr_inhalt.post(new Runnable() {
                        @Override
                        public void run() {
                            //Vibrieren
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);

                            //QR Code Inhalt auslesen und Kamera stoppen

                            //QR Inhalt im Textfeld darstellen (für Test)
                            qr_inhalt.setText(qrcodes.valueAt(0).displayValue);

                            //QR Daten in ArrayList speichern
                            qr_daten = qrcodes.valueAt(0).displayValue;

                            //Key splitten und speichern
                            String[] separated = qr_daten.split(":");
                            separated = separated[1].split("\\s");
                            authkey = separated[1];

                            Toast.makeText(MainActivity.this, authkey, Toast.LENGTH_LONG).show();

                            //Kameraaufnahme stoppen
                            cameraSource.stop();

                            //Hole die Abstimmungsdaten anhand ID aus QR Code
                            if (qr_daten.contains("voteID:1"))
                            {
                                url = "http://212.4.70.74/abstimmungen/1";
                                getAbstimmung();

                            }

                            else if (qr_daten.contains("voteID:2"))
                            {
                                url = "http://212.4.70.74/abstimmungen/2";
                                getAbstimmung();
                            }

                            else{

                                Toast.makeText(MainActivity.this,"Dieser QR Code ist nicht bekannt, die Anwendung wird beendet.", Toast.LENGTH_LONG).show();
                                finishAndRemoveTask();
                            }

                        }
                    });
                }
            }
        });

    }

    private void getAbstimmung() {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            abstimmungsText = response.getString("text");
                            option1 = response.getString("option1");
                            option2 = response.getString("option2");

                            startVotingActivity();

                        } catch (JSONException e) {

                            Toast.makeText(MainActivity.this,"Etwas hat da nicht geklappt.", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this,"Etwas hat da nicht geklappt.", Toast.LENGTH_LONG).show();

                    }
                });

        ApplicationController.getInstance(MainActivity.this).addToRequestQueue(request);
    }

    public void startVotingActivity() {
        Intent i = new Intent(MainActivity.this, votingActivity.class);
        i.putExtra("abstimmungsText", abstimmungsText);
        i.putExtra("option1", option1);
        i.putExtra("option2", option2);
        i.putExtra("authkey", authkey);
        startActivity(i);
    }




}
