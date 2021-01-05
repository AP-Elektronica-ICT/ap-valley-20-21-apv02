package com.example.loginregister.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "MainActivity";

    private static final String BACK_STACK_ROOT_TAG = "navigation_home";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;
    ImageButton navigatie,btnLiveview, btnSettings ;
    NavController navc;
    TextView currentGrowSchedule;
    String GrowboxName, Url,currentGrow,growpogingtot;
    ImageView currentGrowImage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    /*private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }*/

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }

    }

    /*
    AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));


        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(false)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("WATER USAGE");

        cartesian.yAxis(0).title("");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("1986", 3.6, 2.3, 2.8));
        seriesData.add(new CustomDataEntry("1987", 7.1, 4.0, 4.1));
        seriesData.add(new CustomDataEntry("1988", 8.5, 6.2, 5.1));
        seriesData.add(new CustomDataEntry("1989", 9.2, 11.8, 6.5));
        seriesData.add(new CustomDataEntry("1990", 10.1, 13.0, 12.5));
        seriesData.add(new CustomDataEntry("1991", 11.6, 13.9, 18.0));
        seriesData.add(new CustomDataEntry("1992", 16.4, 18.0, 21.0));
        seriesData.add(new CustomDataEntry("1993", 18.0, 23.3, 20.3));
        seriesData.add(new CustomDataEntry("1994", 13.2, 24.7, 19.2));
        seriesData.add(new CustomDataEntry("1995", 12.0, 18.0, 14.4));
        seriesData.add(new CustomDataEntry("1996", 3.2, 15.1, 9.2));
        seriesData.add(new CustomDataEntry("1997", 4.1, 11.3, 5.9));
        seriesData.add(new CustomDataEntry("1998", 6.3, 14.2, 5.2));
        seriesData.add(new CustomDataEntry("1999", 9.4, 13.7, 4.7));
        seriesData.add(new CustomDataEntry("2000", 11.5, 9.9, 4.2));
        seriesData.add(new CustomDataEntry("2001", 13.5, 12.1, 1.2));
        seriesData.add(new CustomDataEntry("2002", 14.8, 13.5, 5.4));
        seriesData.add(new CustomDataEntry("2003", 16.6, 15.1, 6.3));
        seriesData.add(new CustomDataEntry("2004", 18.1, 17.9, 8.9));
        seriesData.add(new CustomDataEntry("2005", 17.0, 18.9, 10.1));
        seriesData.add(new CustomDataEntry("2006", 16.6, 20.3, 11.5));
        seriesData.add(new CustomDataEntry("2007", 14.1, 20.7, 12.2));
        seriesData.add(new CustomDataEntry("2008", 15.7, 21.6, 10));
        seriesData.add(new CustomDataEntry("2009", 12.0, 22.5, 8.9));

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Growbox1");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Growbox2");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Growbox3");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
     */







    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);


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

        cartesian.title("WATER USAGE");

        cartesian.yAxis(0).title("");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);



        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("11/12", 3.6));
        seriesData.add(new CustomDataEntry("12/12", 7.1));
        seriesData.add(new CustomDataEntry("13/12", 8.5));
        seriesData.add(new CustomDataEntry("14/12", 9.2));
        seriesData.add(new CustomDataEntry("15/12", 10.1));
        seriesData.add(new CustomDataEntry("16/12", 11.6));
        seriesData.add(new CustomDataEntry("17/12", 16.4));
        seriesData.add(new CustomDataEntry("18/12", 18.0));
        seriesData.add(new CustomDataEntry("19/12", 13.2));
        seriesData.add(new CustomDataEntry("20/12", 12.0));
        seriesData.add(new CustomDataEntry("21/12", 3.2));
        seriesData.add(new CustomDataEntry("22/12", 4.1));
        seriesData.add(new CustomDataEntry("23/12", 6.3));
        seriesData.add(new CustomDataEntry("24/12", 9.4));
        seriesData.add(new CustomDataEntry("25/12", 11.5));
        seriesData.add(new CustomDataEntry("26/12", 13.5));
        seriesData.add(new CustomDataEntry("27/12", 14.8));
        seriesData.add(new CustomDataEntry("28/12", 16.6));
        seriesData.add(new CustomDataEntry("29/12", 18.1));
        seriesData.add(new CustomDataEntry("30/12", 17.0));
        seriesData.add(new CustomDataEntry("31/12", 16.6));
        seriesData.add(new CustomDataEntry("01/01", 14.1));
        seriesData.add(new CustomDataEntry("02/01", 15.7));
        seriesData.add(new CustomDataEntry("03/01", 12.0));

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


        btnSettings = view.findViewById(R.id.settings);
        btnLiveview=view.findViewById(R.id.btnLiveview);
        currentGrowSchedule = view.findViewById(R.id.currentGrowSchedule);
        currentGrowImage = view.findViewById(R.id.currentPlantImage);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();


        DocumentReference documentReference = mStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
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