package com.example.loginregister.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.core.cartesian.series.Area;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.example.loginregister.IgrowIntroActivity;
import com.example.loginregister.R;
import com.example.loginregister.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class settings extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;


    public settings() {
        // Required empty public constructor
    }
    private static final String BACK_STACK_ROOT_TAG = "settings2";
    Button btnCancel, btnSave;
    NavController navc;
    TextView watertxt,lighttxt;
    ImageButton btnwateradd,btnwatersub,btnlightadd,btnlightsub;
    int huidigedag;
    List<Double> waterverbruikLijst;
    String _growing;
    String _naam;
    String _url;
    String GrowboxName;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        watertxt=view.findViewById(R.id.watertxt);
        lighttxt=view.findViewById(R.id.lighttxt);
        btnwateradd=view.findViewById(R.id.waterAdd);
        btnwatersub=view.findViewById(R.id.waterSub);
        btnlightadd=view.findViewById(R.id.lightAdd);
        btnlightsub=view.findViewById(R.id.lightSub);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        btnSave = view.findViewById(R.id.save_button);
        btnCancel = view.findViewById(R.id.cancel_button);

        mStore = FirebaseFirestore.getInstance();
        String userID;
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference2 = mStore.collection("Users").document(userID);
        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                GrowboxName = documentSnapshot.getString("currentGrowbox");

                DocumentReference documentReference = mStore.collection("Growboxes").document(GrowboxName);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        waterverbruikLijst = (List<Double>) documentSnapshot.get("waterusage");

                        _growing=documentSnapshot.getString("growing");
                        _naam=documentSnapshot.getString("naam");
                        _url=documentSnapshot.getString("url");



                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_WEEK);

                        switch (day) {
                            case Calendar.SUNDAY:
                                watertxt.setText(waterverbruikLijst.get(6).toString());
                                huidigedag=6;
                                break;
                            case Calendar.SATURDAY:
                                watertxt.setText(waterverbruikLijst.get(5).toString());
                                huidigedag=5;
                                break;
                            case Calendar.FRIDAY:
                                watertxt.setText(waterverbruikLijst.get(4).toString());
                                huidigedag=4;
                                break;
                            case Calendar.THURSDAY:
                                watertxt.setText(waterverbruikLijst.get(3).toString());
                                huidigedag=3;
                                break;
                            case Calendar.WEDNESDAY:
                                watertxt.setText(waterverbruikLijst.get(2).toString());
                                huidigedag=2;
                                break;
                            case Calendar.TUESDAY:
                                watertxt.setText(waterverbruikLijst.get(1).toString());
                                huidigedag=1;
                                break;
                            case Calendar.MONDAY:
                                watertxt.setText(waterverbruikLijst.get(0).toString());
                                huidigedag=0;
                                break;
                        }
                    }
                });



                btnwateradd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Double> _lijst=waterverbruikLijst;
                        double getal=_lijst.get(huidigedag);
                        getal+=0.5;
                        _lijst.set(huidigedag,getal);
                        DocumentReference documentReference = mStore.collection("Growboxes").document(GrowboxName);
                        Map<String,Object> box=new HashMap<>();
                        box.put("growing",_growing);
                        box.put("naam",_naam);
                        box.put("url",_url);
                        box.put("waterusage",_lijst);
                        documentReference.set(box);
                        watertxt.setText(String.valueOf(getal));
                    }
                });

                btnwatersub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Double> _lijst=waterverbruikLijst;
                        double getal=_lijst.get(huidigedag);
                        getal-=0.5;
                        _lijst.set(huidigedag,getal);
                        DocumentReference documentReference = mStore.collection("Growboxes").document(GrowboxName);
                        Map<String,Object> box=new HashMap<>();
                        box.put("growing",_growing);
                        box.put("naam",_naam);
                        box.put("url",_url);
                        box.put("waterusage",_lijst);
                        documentReference.set(box);
                        watertxt.setText(String.valueOf(getal));
                    }
                });

                btnlightadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double getal=Double.parseDouble(lighttxt.getText().toString());
                        getal++;
                        lighttxt.setText(getal.toString());
                    }
                });

                btnlightsub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double getal=Double.parseDouble(lighttxt.getText().toString());
                        getal++;
                        lighttxt.setText(getal.toString());
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeFragment homeFragment = new HomeFragment();

                        // Add the new tab fragment
                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, homeFragment)
                                .addToBackStack(BACK_STACK_ROOT_TAG)
                                .commit();
                        navc = Navigation.findNavController(view);
                        navc.navigate(R.id.action_settings2_to_navigation_home);

                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeFragment homeFragment = new HomeFragment();

                        // Add the new tab fragment
                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, homeFragment)
                                .addToBackStack(BACK_STACK_ROOT_TAG)
                                .commit();
                        navc = Navigation.findNavController(view);
                        navc.navigate(R.id.action_settings2_to_navigation_home);

                    }
                });



            }
        });






        // Inflate the layout for this fragment
        return view;
    }
}