package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.ui.dashboard.DashboardFragment;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {
    //public static final int CAMERA_PERMISSION_CODE = 100;

    private SurfaceView surfaceView;
    CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;

    //private Button camera;
    private Button addGrowbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        surfaceView = findViewById(R.id.camera);
        textView = findViewById(R.id.textScan);

        //camera = findViewById(R.id.ScanCamera);
        addGrowbox = findViewById(R.id.AddGrowbox);

        addGrowbox.setVisibility(View.INVISIBLE);

        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(),barcodeDetector).setRequestedPreviewSize(640,480).build();

        /*camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            }
        });*/

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try{
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcode = detections.getDetectedItems();
                if(qrcode.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(qrcode.valueAt(0).displayValue);
                            addGrowbox.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        addGrowbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                String valueToegevoegd = textView.toString();
                bundle.putString("message", valueToegevoegd);
                //set Fragmentclass Arguments
                DashboardFragment fragobj=new DashboardFragment();
                fragobj.setArguments(bundle);

                FragmentManager fm= getSupportFragmentManager();
                DashboardFragment fragment = new DashboardFragment();
                fm.beginTransaction().replace(R.id.scanActivity,fragment).commit();
            }
        });
    }


/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(ScanActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ScanActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}