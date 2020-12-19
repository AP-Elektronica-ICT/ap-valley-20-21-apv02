package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import static java.lang.Integer.parseInt;
import static java.util.logging.Logger.global;

public class ScanActivity extends AppCompatActivity {
    //public static final int CAMERA_PERMISSION_CODE = 100;

    private SurfaceView surfaceView;
    CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;

    //private Button camera;
    private Button addGrowbox;

    // firebaseshizzle
    DatabaseReference reff;
    FirebaseUser muser;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String userID;

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

        muser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();




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
                int amount = getAmountGrowboxes();
                Log.d("aantal", String.valueOf(amount));
                FragmentManager fm= getSupportFragmentManager();
                DashboardFragment fragment = new DashboardFragment();
                fm.beginTransaction().replace(R.id.scanActivity,fragment).commit();
            }
        });
    }

private int getAmountGrowboxes(){
    final int[] amount = new int[1];
        DocumentReference documentReference = mStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
               String _amountBoxes = documentSnapshot.getString("amountBoxes");
              amount[0] = Integer.parseInt(_amountBoxes);

            }
        });


    return amount[0];
    }

}