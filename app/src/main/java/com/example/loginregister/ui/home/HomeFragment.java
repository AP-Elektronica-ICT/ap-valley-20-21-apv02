package com.example.loginregister.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Area;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.loginregister.IgrowIntroActivity;
import com.example.loginregister.R;
import com.example.loginregister.ui.settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.anychart.enums.Anchor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class HomeFragment extends Fragment {
    private static final String TAG = "MainActivity";

    private static final String BACK_STACK_ROOT_TAG = "navigation_home";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;
    ImageButton navigatie,btnLiveview, btnSettings ;
    NavController navc;
    TextView currentGrowSchedule, txtwaterverbruik;
    String GrowboxName, Url,currentGrow,growpogingtot;
    ImageView currentGrowImage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);


        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        userID = mAuth.getCurrentUser().getUid();
        DocumentReference drf = mStore.collection("Users").document(userID);
        drf.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                GrowboxName = documentSnapshot.getString("currentGrowbox");

                if (GrowboxName.equals("None")) {
                    // None zal enkel voorkomen wanneer de user zich nog niet heeft aangemeld
                    // in dat geval zullen we een welcome shizzle laten afspelen alvorens we naar de Homefragment gaan
                    Intent intent = new Intent(getContext(), IgrowIntroActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "growboxname is none");

                }else{
                    txtwaterverbruik=view.findViewById(R.id.txtwaterverbruik);

                    AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);

                    ProgressBar progressBar = view.findViewById(R.id.progressBar2);
                    anyChartView.setProgressBar(progressBar);

                    Cartesian cartesian = AnyChart.line();

                    cartesian.animation(true);

                    cartesian.padding(10d, 20d, 15d, 20d);

                    cartesian.crosshair().enabled(true);
                    cartesian.crosshair()
                            .yLabel(false)
                            // TODO ystroke
                            .yStroke((Stroke) null, null, null, (String) null, (String) null);

                    cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

                    cartesian.title("WATER USAGE (ml)");

                    cartesian.yAxis(0).title("");
                    cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

                    mStore = FirebaseFirestore.getInstance();

                    DocumentReference documentReference = mStore.collection("Growboxes").document(GrowboxName);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                            List<Double> waterverbruikLijst = (List<Double>) documentSnapshot.get("waterusage");

                            List<DataEntry> seriesData = new ArrayList<>();

                            int i=0;

                            for (Double item : waterverbruikLijst){
                                String dag="";
                                if(i==0) dag="Monday";
                                if(i==1) dag="Tuesday";
                                if(i==2) dag="Wednesday";
                                if(i==3) dag="Thursday";
                                if(i==4) dag="Friday";
                                if(i==5) dag="Saturday";
                                if(i==6) dag="Sunday";
                                seriesData.add(new CustomDataEntry(dag,waterverbruikLijst.get(i)));
                                i++;
                            }

                            Calendar calendar = Calendar.getInstance();
                            int day = calendar.get(Calendar.DAY_OF_WEEK);

                            switch (day) {
                                case Calendar.SUNDAY:
                                    txtwaterverbruik.setText(waterverbruikLijst.get(6).toString()+" ml/d");
                                    break;
                                case Calendar.SATURDAY:
                                    txtwaterverbruik.setText(waterverbruikLijst.get(5).toString()+" ml/d");
                                    break;
                                case Calendar.FRIDAY:
                                    txtwaterverbruik.setText(waterverbruikLijst.get(4).toString()+" ml/d");
                                    break;
                                case Calendar.THURSDAY:
                                    txtwaterverbruik.setText(waterverbruikLijst.get(3).toString()+" ml/d");
                                    break;
                                case Calendar.WEDNESDAY:
                                    txtwaterverbruik.setText(waterverbruikLijst.get(2).toString()+" ml/d");
                                    break;
                                case Calendar.TUESDAY:
                                    txtwaterverbruik.setText(waterverbruikLijst.get(1).toString()+" ml/d");
                                    break;
                                case Calendar.MONDAY:
                                    txtwaterverbruik.setText(waterverbruikLijst.get(0).toString()+" ml/d");
                                    break;
                            }

                            Set set = Set.instantiate();
                            set.data(seriesData);
                            Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

                            Area series1 = cartesian.area(series1Mapping);
                            series1.hovered().markers().enabled(true);
                            series1.color("#77B255");
                            series1.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series1.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);




                            cartesian.legend().enabled(false);
                            cartesian.legend().fontSize(13d);
                            cartesian.legend().padding(0d, 0d, 10d, 0d);

                            anyChartView.setChart(cartesian);
                            cartesian.background().fill("white");
                            cartesian.background().cornerType("cut");
                            cartesian.background().corners(10);
                        }
                    });

                     }
                }

        });




        btnSettings = view.findViewById(R.id.settings);
        btnLiveview=view.findViewById(R.id.btnLiveview);
        currentGrowSchedule = view.findViewById(R.id.currentGrowSchedule);
        currentGrowImage = view.findViewById(R.id.currentPlantImage);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();


        DocumentReference documentReference2 = mStore.collection("Users").document(userID);
        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                GrowboxName = documentSnapshot.getString("currentGrowbox");
                if (GrowboxName.equals("None")) {
                    // None zal enkel voorkomen wanneer de user zich nog niet heeft aangemeld
                    // in dat geval zullen we een welcome shizzle laten afspelen alvorens we naar de Homefragment gaan
                    Intent intent = new Intent(getContext(), IgrowIntroActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "growboxname is none");

                } else {
                    DatabaseReference myRefTime = database.getReference(GrowboxName + "/CurrentGrowSchedule");
                    myRefTime.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            currentGrow = dataSnapshot.getValue(String.class);
                            currentGrowSchedule.setText(currentGrow);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

                }
            });


        mStore = FirebaseFirestore.getInstance();
        mStore.collection("GrowSchedules").document("Fruit").collection("0").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();


                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId().toString());
                        String naam = document.getString("naam");
                        Log.d("naam", naam);
                        DocumentReference documentReference = mStore.collection("Users").document(userID);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                GrowboxName =documentSnapshot.getString("currentGrowbox");
                                if (GrowboxName == "None") {
                                    // None zal enkel voorkomen wanneer de user zich nog niet heeft aangemeld
                                    // in dat geval zullen we een welcome shizzle laten afspelen alvorens we naar de Homefragment gaan
                                    Intent intent = new Intent(getContext(), IgrowIntroActivity.class);
                                    startActivity(intent);
                                    Log.d("heeeeeelp", "growboxname is none");

                                }
                                DatabaseReference myRefTime = database.getReference(GrowboxName + "/CurrentGrowSchedule");
                                myRefTime.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        growpogingtot = dataSnapshot.getValue(String.class);

                                        if(naam == growpogingtot){
                                            Url = document.getString("url");

                                            Picasso.get().load(Url).into(currentGrowImage);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });


                    }


                    Log.d("grow it", list.toString());
                } else {
                    Log.d("wassup", "wassjp");
                }

            }
        });


        navigatie = view.findViewById(R.id.navigation_button);
        navigatie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrowSchedules growschedules = new GrowSchedules();

                // Add the new tab fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, growschedules)
                        .addToBackStack(BACK_STACK_ROOT_TAG)
                        .commit();
                navc = Navigation.findNavController(view);
                navc.navigate(R.id.action_navigation_home_to_growSchedules);


            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings settings = new settings();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, settings)
                        .addToBackStack(BACK_STACK_ROOT_TAG)
                        .commit();
                navc = Navigation.findNavController(view);
                navc.navigate(R.id.action_navigation_home_to_settings2);

            }
        });

        btnLiveview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Liveview liveview = new Liveview();

                // Add the new tab fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, liveview)
                        .addToBackStack(BACK_STACK_ROOT_TAG)
                        .commit();
                navc = Navigation.findNavController(view);
                navc.navigate(R.id.action_navigation_home_to_liveview3);


            }
        });
        return view;
    }


}