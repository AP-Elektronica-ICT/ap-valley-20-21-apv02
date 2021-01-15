package com.example.loginregister.ui.home;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleJobService extends JobService {
    FirebaseDatabase database;
    String[] pumps = {"PUMP1", "PUMP2", "PUMP3", "PUMP4"};

    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        database = FirebaseDatabase.getInstance();
        Log.d("CURRENTNAME", params.getExtras().getString("CurrentName"));

        doBackgroundWork(params);
        return true;
    }
    private void doBackgroundWork(final JobParameters params) {
        int [] mChosenpositions =  params.getExtras().getIntArray("mChosenPositions");
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        String CurrentName = params.getExtras().getString("CurrentName");
        int position = params.getExtras().getInt("position");
        List<String> mTitle = new ArrayList<String>(Arrays.asList(params.getExtras().getString("mTitle"))); //new ArrayList is only needed if you absolutely need an ArrayList
        String plant = params.getExtras().getString("plant");
        String [] titles = params.getExtras().getStringArray("mTitle");

        new Thread(new Runnable() {
            @Override
            public void run() {
                // hier worden de growschedules gestart om de zoveel tijd
                DocumentReference dr = mStore.collection("Growboxes").document(params.getExtras().getString("CurrentName"));
                Map<String, Object> box = new HashMap<>();
                box.put("growing", titles[position]);
                box.put("naam", params.getExtras().getString("CurrentName"));
                box.put("url", "https://www.thespruceeats.com/thmb/qsrUxBu670oOJd26FgEPk0mFToU=/3333x3333/smart/filters:no_upscale()/various-fresh-herbs-907728974-cc6c2be53aac46de9e6a4b47a0e630e4.jpg");
                dr.set(box);
                for(int i =0; i<4; i++){
                    DatabaseReference myRefTime = database.getReference(CurrentName + "/" + pumps[i] + "/Time" );
                    DatabaseReference myRefInter = database.getReference(CurrentName + "/" +  pumps[i] +  "/Interval" );
                    DatabaseReference mRefLightinteval = database.getReference(CurrentName+ "/light/INTERVAL");
                    DatabaseReference mRefLighttime = database.getReference(CurrentName + "/light/TIME");
                    DatabaseReference myRefCurrentGrow = database.getReference(CurrentName + "/CurrentGrowSchedule");
                    int [] Timingwater = {1000,2000,3000,4000};
                    int [] Timinglight = {1000,2000,3000,4000};


                    myRefCurrentGrow.setValue(mTitle.get(position));
                    if (mChosenpositions[i] == 0){
                        myRefTime.setValue(0);
                        myRefInter.setValue(0);
                        mRefLightinteval.setValue(0);
                        Log.d("chosen: ", Integer.toString( mChosenpositions[i]));
                    }else if (mChosenpositions[i] != 0){
                        myRefTime.setValue(100);
                        myRefInter.setValue(300);
                        mRefLightinteval.setValue(400);
                        mRefLighttime.setValue(500);
                        myRefCurrentGrow.setValue(plant);
                        Log.d("not chosen: ", Integer.toString( mChosenpositions[i]));

                    }


                }
                Log.d(TAG, "Job finished");
                jobFinished(params, false);
            }
        }).start();
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }




}
