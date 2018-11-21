package com.example.trougevin.metsy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BarcodeScanner extends Activity {

    TextView result;
    TextView ingredients;
    List ingredientList = new ArrayList();
    SurfaceView camera;
    BarcodeDetector detector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    String EAN;

    private static final String URL ="https://fr.openfoodfacts.org/api/v0/produit/3029330003533.json";


    //Demande à l'utilisateur l'autorisation d'acceder à sa camera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        cameraSource.start(camera.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        result = findViewById(R.id.textView1);
        ingredients = findViewById(R.id.textView2);
        camera = findViewById(R.id.surfaceView);


        //import barcode detetector de Google : param = ALL FORMATS (EAN, QR CODE...)
        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        if (!detector.isOperational()) {
            result.setText("Could not set up the detector!");
            return;
        }

        //affiche la camera en preview directement dans l'appli et permet le focus de la camera du telephone
        cameraSource = new CameraSource.Builder(this, detector)
                .setAutoFocusEnabled(true)
                .build();



        //permet de controler la taille, pixels de la SurfaceView
        camera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BarcodeScanner.this,
                            new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);

                    return;
                }
                try {
                    cameraSource.start(camera.getHolder());
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


        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> codes = detections.getDetectedItems();
                if (codes.size() != 0) {
                    result.post(new Runnable() {
                        @Override
                        public void run() {
                            EAN = codes.valueAt(0).displayValue;
                            result.setText(EAN);
                            getAllergens();

                        }
                    });
                }
            }
        });

        //EAN = "3029330003533";


    }



    private void getAllergens() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://fr.openfoodfacts.org/api/v0/produit/"+EAN+".json", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObject = jsonResponse.getJSONObject("product");
                    String allergen = jsonObject.getString("allergens_from_user");
                    allergen = allergen.substring(4);
                    String[] allergens = allergen.split(",");
                    String FinalAllergens = "";

                    for(String i:allergens){
                        FinalAllergens += i+", ";
                    }

                    ingredients.setText(FinalAllergens);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.setText("failure");
            }
        });
        requestQueue.add(request);
    }
}

