package com.example.trougevin.metsy;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;


public class BarcodeScanner extends Activity {

    TextView result;
    TextView ingredients;
    SurfaceView camera;
    BarcodeDetector detector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    String EAN;
    String PrintAllergens = "";


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

        camera = findViewById(R.id.surfaceView);
        result = findViewById(R.id.textView);



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
                            //result.setText(EAN);
                            String test = EAN;
                            getAllergens();

                        }
                    });
                }
            }
        });

    }



    private void getAllergens() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://fr.openfoodfacts.org/api/v0/produit/"+EAN+".json", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObject = jsonResponse.getJSONObject("product");
                    String name = jsonObject.getString("product_name");
                    String allergen = jsonObject.getString("allergens_from_ingredients");
                    String trace = jsonObject.getString("traces");
                    String imageURL = jsonObject.getString("image_url");
                    String[] allergens = allergen.split(",");


                    Scanner scan = new Scanner(getResources().openRawResource(R.raw.allergens));
                    while (scan.hasNextLine()) {
                        String line = scan.nextLine();
                        String[] col = line.split(",");

                        PrintAllergens = "";

                            for (String i : col) {
                                i = i.toUpperCase();
                                for (String j : allergens) {
                                    j = j.toUpperCase();
                                    if (j != "" && i.contains(j)) {
                                        if (!PrintAllergens.contains(i)) {
                                            PrintAllergens += i + ", ";
                                        }
                                    }
                                }
                            }
                    }

                    ImageView imageView = new ImageView(BarcodeScanner.this);
                    Picasso.get()
                            .load(imageURL)
                            .into(imageView);

                    if (PrintAllergens != "") {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanner.this, R.style.AlertDialogStyle);
                        builder.setTitle(name);
                        builder.setView(imageView);
                        builder.setMessage("Attention ! Allergènes présents : " + PrintAllergens);

                        AlertDialog alertDialog = builder.create();

                        if(!alertDialog.isShowing()){
                            alertDialog.show();
                        }

                    } else {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(BarcodeScanner.this, R.style.AlertDialogStyle2);
                        builder2.setTitle(name);
                        builder2.setView(imageView);
                        builder2.setMessage("Ok ! Aucun allergène trouvé !");

                        AlertDialog alertDialog = builder2.create();

                        if(!alertDialog.isShowing()){
                            alertDialog.show();
                        }
                    }

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
