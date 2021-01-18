package com.example.loginregister.ui.home;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class ExampleJobService extends JobService {
    //FirebaseDatabase database;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String[] pumps = {"PUMP1", "PUMP2", "PUMP3", "PUMP4"};
    int Week = 0;
    int time;
    int interval;
    int lightTime;
    int lightInterval;
    //Basilicum
    int[] BasilicumTime = {7500, 10001, 15000, 10001};
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
   List<Double> waterverbruikWeek = new List<Double>() {
       @Override
       public int size() {
           return 0;
       }

       @Override
       public boolean isEmpty() {
           return false;
       }

       @Override
       public boolean contains(@Nullable Object o) {
           return false;
       }

       @NonNull
       @Override
       public Iterator<Double> iterator() {
           return null;
       }

       @NonNull
       @Override
       public Object[] toArray() {
           return new Object[0];
       }

       @NonNull
       @Override
       public <T> T[] toArray(@NonNull T[] a) {
           return null;
       }

       @Override
       public boolean add(Double aDouble) {
           return false;
       }

       @Override
       public boolean remove(@Nullable Object o) {
           return false;
       }

       @Override
       public boolean containsAll(@NonNull Collection<?> c) {
           return false;
       }

       @Override
       public boolean addAll(@NonNull Collection<? extends Double> c) {
           return false;
       }

       @Override
       public boolean addAll(int index, @NonNull Collection<? extends Double> c) {
           return false;
       }

       @Override
       public boolean removeAll(@NonNull Collection<?> c) {
           return false;
       }

       @Override
       public boolean retainAll(@NonNull Collection<?> c) {
           return false;
       }

       @Override
       public void clear() {

       }

       @Override
       public Double get(int index) {
           return null;
       }

       @Override
       public Double set(int index, Double element) {
           return null;
       }

       @Override
       public void add(int index, Double element) {

       }

       @Override
       public Double remove(int index) {
           return null;
       }

       @Override
       public int indexOf(@Nullable Object o) {
           return 0;
       }

       @Override
       public int lastIndexOf(@Nullable Object o) {
           return 0;
       }

       @NonNull
       @Override
       public ListIterator<Double> listIterator() {
           return null;
       }

       @NonNull
       @Override
       public ListIterator<Double> listIterator(int index) {
           return null;
       }

       @NonNull
       @Override
       public List<Double> subList(int fromIndex, int toIndex) {
           return null;
       }
   };
   Double[] growshizlle = new Double[7];




    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "Job started");

        doBackgroundWork(params);
        return true;
    }
    private void doBackgroundWork(final JobParameters params) {
        Week++;
        int [] mChosenpositions =  params.getExtras().getIntArray("mChosenPositions");
        String CurrentName = params.getExtras().getString("CurrentName");
        int position = params.getExtras().getInt("position");
        String [] titles = params.getExtras().getStringArray("mTitle");
       // List<String> mTitle = new ArrayList<String>(Arrays.asList(params.getExtras().getString("mTitle"))); //new ArrayList is only needed if you absolutely need an ArrayList
        String plant = params.getExtras().getString("plant");

        Boolean on_light = true;
        Boolean off_light = false;



        new Thread(new Runnable() {
            @Override
            public void run() {
                // hier worden de growschedules gestart om de zoveel tijd


        /*        Map<String, Object> box = new HashMap<>();
                box.put("growing", titles[position]);
                box.put("naam", params.getExtras().getString("CurrentName"));
                box.put("url", "https://www.thespruceeats.com/thmb/qsrUxBu670oOJd26FgEPk0mFToU=/3333x3333/smart/filters:no_upscale()/various-fresh-herbs-907728974-cc6c2be53aac46de9e6a4b47a0e630e4.jpg");
                box.put("waterusage", waterverbruikWeek);*/

                /*
                if(Week == 4){
                    for(int i =0; i<4; i++) {
                        DatabaseReference myRefTime = database.getReference(CurrentName + "/" + pumps[i] + "/Time");
                        DatabaseReference myRefInter = database.getReference(CurrentName + "/" + pumps[i] + "/Interval");
                        DatabaseReference mRefLightinteval = database.getReference(CurrentName + "/light/INTERVAL");
                        DatabaseReference mRefLighttime = database.getReference(CurrentName + "/light/TIME");
                        DatabaseReference myRefCurrentGrow = database.getReference(CurrentName + "/CurrentGrowSchedule");
                        DatabaseReference isUpdated = database.getReference(CurrentName + "/" + pumps[i] + "/isUpdated");
                        DatabaseReference on_off = database.getReference(CurrentName + "/" + pumps[i] + "/ON_OFF");
                        DatabaseReference lighUpdate = database.getReference(CurrentName + "/light/isUpdated");
                        DatabaseReference lighton_off = database.getReference(CurrentName + "/light/ON_OFF");

                        myRefTime.setValue(100000000);
                        myRefInter.setValue(100000000);
                        mRefLightinteval.setValue(100000000);
                        mRefLighttime.setValue(100000000);
                        myRefCurrentGrow.setValue(100000000);
                        isUpdated.setValue(true);
                        on_off.setValue(false);
                        lighUpdate.setValue(true);
                        lighton_off.setValue(false);


                    }
                        Log.d(TAG, "Job finished");
                    jobFinished(params, false);
                }else if(Week<4){

                 */
                    setTimeAndInterval(Week, plant, params );
                    for(int i =0; i<4; i++){
                        DatabaseReference myRefTime = database.getReference(CurrentName + "/" + pumps[i] + "/Time" );
                        DatabaseReference myRefon_off = database.getReference(CurrentName + "/" + pumps[i] + "/ON_OFF" );
                        DatabaseReference myRefInter = database.getReference(CurrentName + "/" +  pumps[i] +  "/Interval" );
                        DatabaseReference mRefLightinteval = database.getReference(CurrentName+ "/light/Interval");
                        DatabaseReference mRefLighttime = database.getReference(CurrentName + "/light/Time");
                        DatabaseReference myRefCurrentGrow = database.getReference(CurrentName + "/CurrentGrowSchedule");
                        DatabaseReference isUpdated = database.getReference(CurrentName + "/" + pumps[i] + "/isUpdated" );
                        DatabaseReference lighUpdate = database.getReference(CurrentName + "/light/isUpdated");
                        DatabaseReference lighton_off = database.getReference(CurrentName + "/light/ON_OFF");


                        myRefCurrentGrow.setValue(titles[position]);
                        if (mChosenpositions[i] == 0){

                            myRefon_off.setValue(Boolean.FALSE);
                            isUpdated.setValue(Boolean.TRUE);
                           // lighUpdate.setValue(Boolean.FALSE);
                          //  lighton_off.setValue(Boolean.TRUE);
                            //myRefon_off.setValue(Boolean.FALSE);
                            //   Log.d("chosen: ", Integer.toString( mChosenpositions[i]));
                        }else if (mChosenpositions[i] != 0){
                            myRefTime.setValue(100000000);
                            myRefInter.setValue(100000000);
                            mRefLightinteval.setValue(100000000);
                            myRefTime.setValue(time);
                            myRefInter.setValue(interval);
                            mRefLightinteval.setValue(lightInterval);
                            mRefLighttime.setValue(lightTime);
                            myRefCurrentGrow.setValue(plant);
                            isUpdated.setValue(Boolean.FALSE);
                            lighUpdate.setValue(Boolean.FALSE);
                            lighton_off.setValue(Boolean.TRUE);
                            myRefon_off.setValue(Boolean.TRUE);

                            //  Log.d("not chosen: ", Integer.toString( mChosenpositions[i]));

                        }

                  //  }


                }


            }
        }).start();
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    public void setTimeAndInterval(int week, String plant, final JobParameters params){
      Log.d("WEEK", "value = " + week);
        Log.d("PLANT", "value = " + plant);
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        String url = "https://www.thespruceeats.com/thmb/qsrUxBu670oOJd26FgEPk0mFToU=/3333x3333/smart/filters:no_upscale()/various-fresh-herbs-907728974-cc6c2be53aac46de9e6a4b47a0e630e4.jpg";
        int position = params.getExtras().getInt("position");
        String [] titles = params.getExtras().getStringArray("mTitle");

        switch (plant){
            case "BASILICUM":
                DocumentReference dr = mStore.collection("Growboxes").document(params.getExtras().getString("CurrentName"));
                for (int i = 0; i<7; i++){
                    growshizlle[i]= (double) (BasilicumTime[Week -1] / 1000.0);
                    Log.d("WATERUSAGE", "" + growshizlle[i]);

                }
                Growbox grb = new Growbox(params.getExtras().getString("CurrentName"),url,  titles[position], Arrays.asList(growshizlle));
                dr.set(grb);

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
