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
    int Week = 0;
    int time;
    int interval;
    int lightTime;
    int lightInterval;
    //Basilicum
    int[] BasilicumTime = {7500, 10000, 15000, 10000};
    int[] BasilicumInterval = { 86400000,  86400000,  86400000, 43200000};
    int[] BasilLightTime = {28800000, 28800000, 28800000, 28800000};
    int[] BasilLightinterval = {57600000 , 57600000 , 57600000 , 57600000 };

    Growschedule growBasil = new Growschedule("BASILICUM",BasilicumTime, BasilicumInterval,4, BasilLightTime, BasilLightinterval);

    //Tuinkers
    int[] TuinkersTime = {5000, 10000, 10000, 10000};
    int[] TuinkersInterval = { 86400000,  86400000,  86400000, 86400000};
    int[] TuinkersLightTime = {28800000, 28800000, 28800000, 28800000};
    int[] TuinkersLightinterval = {57600000 , 57600000 , 57600000 , 57600000 };
    Growschedule tuinkers = new Growschedule("TUINKERS",TuinkersTime, TuinkersInterval,4, TuinkersLightTime, TuinkersLightinterval);

    //Marjolein
    int[] MarjoleinTime = {5000, 10000, 10000, 10000};
    int[] MarjoleinInterval = { 86400000,  86400000,  86400000, 86400000};
    int[] MarjoleinLightTime = {28800000, 28800000, 28800000, 28800000};
    int[] MarjoleinLightinterval = {57600000 , 57600000 , 57600000 , 57600000 };
    Growschedule marjolein = new Growschedule("MARJOLEIN",MarjoleinTime, MarjoleinInterval,4, MarjoleinLightTime, MarjoleinLightinterval);

    //Thijm
    int[] ThijmTime = {7500, 10000, 10000, 15000};
    int[] ThijmInterval = { 86400000,  86400000,  43200000, 43200000};
    int[] ThijmLightTime = {28800000, 28800000, 28800000, 28800000};
    int[] ThijmLightinterval = {57600000 , 57600000 , 57600000 , 57600000 };
    Growschedule thijm = new Growschedule("MARJOLEIN",ThijmTime, ThijmInterval,4, ThijmLightTime, ThijmLightinterval);




    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(JobParameters params) {
        Week++;
        Log.d(TAG, "Job started");
        database = FirebaseDatabase.getInstance();

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

                setTimeAndInterval(Week, plant );
                for(int i =0; i<4; i++){
                    DatabaseReference myRefTime = database.getReference(CurrentName + "/" + pumps[i] + "/Time" );
                    DatabaseReference myRefInter = database.getReference(CurrentName + "/" +  pumps[i] +  "/Interval" );
                    DatabaseReference mRefLightinteval = database.getReference(CurrentName+ "/light/INTERVAL");
                    DatabaseReference mRefLighttime = database.getReference(CurrentName + "/light/TIME");
                    DatabaseReference myRefCurrentGrow = database.getReference(CurrentName + "/CurrentGrowSchedule");

                    myRefCurrentGrow.setValue(titles[position]);
                    if (mChosenpositions[i] == 0){
                        myRefTime.setValue(0);
                        myRefInter.setValue(0);
                        mRefLightinteval.setValue(0);
                        Log.d("chosen: ", Integer.toString( mChosenpositions[i]));
                    }else if (mChosenpositions[i] != 0){
                        myRefTime.setValue(time);
                        myRefInter.setValue(interval);
                        mRefLightinteval.setValue(lightInterval);
                        mRefLighttime.setValue(lightTime);
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

    public void setTimeAndInterval(int week, String plant){
        switch (plant){
            case "BASILICUM":
                switch (week){
                    case 1:
                        time = growBasil.Time[0];
                        interval = growBasil.Interval[0];
                        lightTime = growBasil.LightInterval[0];
                        lightInterval = growBasil.LightInterval[0];
                        break;
                    case 2:
                        time = growBasil.Time[1];
                        interval = growBasil.Interval[1];
                        lightTime = growBasil.LightInterval[1];
                        lightInterval = growBasil.LightInterval[1];
                        break;
                    case 3:
                        time = growBasil.Time[2];
                        interval = growBasil.Interval[2];
                        lightTime = growBasil.LightInterval[2];
                        lightInterval = growBasil.LightInterval[2];
                        break;
                    case 4:
                        time = growBasil.Time[3];
                        interval = growBasil.Interval[3];
                        lightTime = growBasil.LightInterval[3];
                        lightInterval = growBasil.LightInterval[3];
                        break;
                    default:
                        Log.d("NOTPOSSIBLE", "ERROR 404");
                        break;
                }
                break;
            case "TUINKERS":
                switch (week){
                    case 1:
                        time = tuinkers.Time[0];
                        interval = tuinkers.Interval[0];
                        lightTime = tuinkers.LightInterval[0];
                        lightInterval = tuinkers.LightInterval[0];
                        break;
                    case 2:
                        time = tuinkers.Time[1];
                        interval = tuinkers.Interval[1];
                        lightTime = tuinkers.LightInterval[1];
                        lightInterval = tuinkers.LightInterval[1];
                        break;
                    case 3:
                        time = tuinkers.Time[2];
                        interval = tuinkers.Interval[2];
                        lightTime = tuinkers.LightInterval[2];
                        lightInterval = tuinkers.LightInterval[2];
                        break;
                    case 4:
                        time = tuinkers.Time[3];
                        interval = tuinkers.Interval[3];
                        lightTime = tuinkers.LightInterval[3];
                        lightInterval = tuinkers.LightInterval[3];
                        break;
                    default:
                        Log.d("NOTPOSSIBLE", "ERROR 404");
                        break;
                }
                break;
            case "MARJOLEIN":
                switch (week){
                    case 1:
                        time = marjolein.Time[0];
                        interval = marjolein.Interval[0];
                        lightTime = marjolein.LightInterval[0];
                        lightInterval = marjolein.LightInterval[0];
                        break;
                    case 2:
                        time = marjolein.Time[1];
                        interval = marjolein.Interval[1];
                        lightTime = marjolein.LightInterval[1];
                        lightInterval = marjolein.LightInterval[1];
                        break;
                    case 3:
                        time = marjolein.Time[2];
                        interval = marjolein.Interval[2];
                        lightTime = marjolein.LightInterval[2];
                        lightInterval = marjolein.LightInterval[2];
                        break;
                    case 4:
                        time = marjolein.Time[3];
                        interval = marjolein.Interval[3];
                        lightTime = marjolein.LightInterval[3];
                        lightInterval = marjolein.LightInterval[3];
                        break;
                    default:
                        Log.d("NOTPOSSIBLE", "ERROR 404");
                        break;
                }
                break;
            case "THIJM":
                switch (week){
                    case 1:
                        time = thijm.Time[0];
                        interval = thijm.Interval[0];
                        lightTime = thijm.LightInterval[0];
                        lightInterval = thijm.LightInterval[0];
                        break;
                    case 2:
                        time = thijm.Time[1];
                        interval = thijm.Interval[1];
                        lightTime = thijm.LightInterval[1];
                        lightInterval = thijm.LightInterval[1];
                        break;
                    case 3:
                        time = thijm.Time[2];
                        interval = thijm.Interval[2];
                        lightTime = thijm.LightInterval[2];
                        lightInterval = thijm.LightInterval[2];
                        break;
                    case 4:
                        time = thijm.Time[3];
                        interval = thijm.Interval[3];
                        lightTime = thijm.LightInterval[3];
                        lightInterval = thijm.LightInterval[3];
                        break;
                    default:
                        Log.d("NOTPOSSIBLE", "ERROR 404");
                        break;
                }
                break;
        }






    }


}
