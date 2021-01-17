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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    String userID, _naam, _growing, _url,_uname,_phone,_image,_email,_currentGrow,_Coverimage,_amountH,_amountB;
    int amount;
    Map<String, Object> box = new HashMap<>();
    Map<String, Object> userd = new HashMap<>();
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

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        muser = FirebaseAuth.getInstance().getCurrentUser();
        userID = muser.getUid();



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
                String valueToegevoegd = textView.getText().toString();
                setAddGrowbox(valueToegevoegd);

                //set Fragmentclass Arguments
                DashboardFragment fragobj=new DashboardFragment();
                fragobj.setArguments(bundle);
                FragmentManager fm= getSupportFragmentManager();
                DashboardFragment fragment = new DashboardFragment();
                surfaceView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                addGrowbox.setVisibility(View.INVISIBLE);
                fm.beginTransaction().replace(R.id.scanActivity,fragment).commit();
            }
        });
    }

    // growbox toevoegen in de database -->

    private void setAddGrowbox(String naam){

        Log.d("growboxnaam", naam);
        DocumentReference documentReference = mStore.collection("Growboxes").document(naam);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                _naam = value.getString("naam");
                _growing = value.getString("growing");
              //    _growing= "iets";
                _url = value.getString("url");
                int amount = getAmountGrowboxes();
                amount+=1;
                Log.d("AMOUNTBOXES", "value" + amount);
                String aantal = String.valueOf(amount);
                // onderstaande moet van realtime growbox worden gehaald

                box.put("naam",naam);
                box.put("url", _url);
                box.put("growing", _growing);

                DocumentReference documentref = mStore.collection("Users").document(userID);

                documentref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        _uname = value.getString("uname");
                        _amountH = value.getString("amountHarvests");
                        _Coverimage = value.getString("coverImage");
                        _phone = value.getString("phone");
                        _image = value.getString("image");
                        _email = value.getString("email");

                        DocumentReference dr = mStore.collection("Users").document(userID);
                        userd.put("amountBoxes", aantal);
                        userd.put("currentGrowbox", naam);
                        userd.put("uname", _uname);
                        userd.put("amountHarvests", _amountH);
                        userd.put("phone", _phone);
                        userd.put("image", _image);
                        userd.put("email", _email);
                        userd.put("coverImage", _Coverimage);
                        dr.set(userd);

                    }
                });

                documentref.collection("0").document(aantal).set(box);
                addGrowbox.setVisibility(View.INVISIBLE);



            }
        });






    }


    // verkrijgen van het aantal growboxes van de user
    private int getAmountGrowboxes(){

        DocumentReference documentReference = mStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                String _amountBoxes = documentSnapshot.getString("amountBoxes");
                amount = Integer.parseInt(_amountBoxes);

            }
        });
        Log.d("amountboxes", String.valueOf(amount));
        return amount;
    }

    // verkrijgen info growbox die gescant is --> krijgen van de realtime database.
    private String[] getGrowboxdata(String naam){
        final String[] _naam = new String[1];
        final String[] _currentGrow = new String[1];
        final String[] _url = new String[1];
        final String[] _data = new String[3];

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(naam).child("naam");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               _data[0] =  dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child(naam).child("CurrentGrowShedule");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _data[1] =  dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child(naam).child("url");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _data[2] =  dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return _data;
    }


}