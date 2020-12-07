package com.example.loginregister.ui.home;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginregister.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.JOB_SCHEDULER_SERVICE;


public class GrowSchedules extends Fragment {

    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userID;
    FirebaseAuth mAuth;
    List<String> mTitle = new ArrayList<>();
    List<String> mDescription = new ArrayList<>();
    List<String> images = new ArrayList<>();
    List<String> mDescriptionlong = new ArrayList<>();
    String[] pumps = {"PUMP1", "PUMP2", "PUMP3", "PUMP4"};
    String CurrentName;


    FirebaseFirestore mStore;

    public GrowSchedules() {
        // Required empty public constructor

    }

    boolean jos = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = mStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               CurrentName = documentSnapshot.getString("currentGrowbox");


            }
        });

        // inladen van lists van firebase
        mStore = FirebaseFirestore.getInstance();
        mStore.collection("GrowSchedules").document("Fruit").collection("0").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId().toString());
                        mTitle.add(document.getString("naam"));
                        mDescription.add(document.getString("beschrijving"));
                        images.add(document.getString("url"));
                        mDescriptionlong.add(document.getString("beschrijvinglang"));
                    }
                    listView = (ListView) listView.findViewById(R.id.listview);
                    MyAdapter adapter = new MyAdapter(getActivity(), mTitle, mDescription, images);
                    listView.setAdapter(adapter);

                    Log.d("grow it", list.toString());
                } else {
                    Log.d("wassup", "wassjp");
                }

            }
        });

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // clickable list --> op itemclick gaat alert open
        View view = inflater.inflate(R.layout.fragment_grow_schedules, container, false);
        listView = view.findViewById(R.id.listview);
        MyAdapter adapter = new MyAdapter(getActivity(), mTitle, mDescription, images);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Log.d("greetz", "clicked");
                        showYesOrNo("ALERT", position);
                        break;
                    case 1:
                        Toast.makeText(getContext(), mDescription.get(1), Toast.LENGTH_LONG);
                        showYesOrNo("ALERT", position);
                        break;
                    case 2:
                        Toast.makeText(getContext(), mDescription.get(2), Toast.LENGTH_LONG);
                        showYesOrNo("ALERT", position);
                        break;
                    case 3:
                        Toast.makeText(getContext(), mDescription.get(3), Toast.LENGTH_LONG);
                        showYesOrNo("ALERT", position);
                        break;
                    case 4:
                        Toast.makeText(getContext(), mDescription.get(4), Toast.LENGTH_LONG);
                        showYesOrNo("ALERT", position);
                        break;
                    case 5:
                        Toast.makeText(getContext(), mDescription.get(5), Toast.LENGTH_LONG);
                        showYesOrNo("ALERT", position);
                        break;
                    case 6:
                        Toast.makeText(getContext(), mDescription.get(6), Toast.LENGTH_LONG);
                        showYesOrNo("ALERT", position);
                        break;
                    case 7:
                        Toast.makeText(getContext(), mDescription.get(7), Toast.LENGTH_LONG);
                        showYesOrNo("ALERT", position);
                        break;
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rTitle;
        List<String> rDescription;
        List<String> rImgs;

        MyAdapter(Context c, List<String> title, List<String> description, List<String> imgs) {
            super(c, R.layout.row_list_grow, R.id.title_main1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;


        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_list_grow, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.title);
            TextView myDescription = row.findViewById(R.id.info);

            // now set our resources on views
            Picasso.get().load(rImgs.get(position)).into(images);
          //  images.setImageResource(rImgs.get(position));
            myTitle.setText(rTitle.get(position));
            myDescription.setText(rDescription.get(position));
            return row;
        }
    }


    // aparte klasse maken voor Alerts?
    // alert dialog over zeker zijn
    private void showYesOrNo(final String key, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(key);
        //set layout of dialog
        builder.setMessage("Are You sure you want to start a new grow of " + mTitle.get(position) + "? " + mDescriptionlong.get(position));
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        //add edit text

        builder.setView(linearLayout);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                Log.d("actie:", "aantalplaten ingeven");

               showNuberOfPlants(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create();
        builder.show();


    }

    // aantal planten ingeven
    private void showNuberOfPlants(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Geef het aantal planten");
        //set layout of dialog
        builder.setMessage("Hoeveel planten?" );
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editText.setHint("Enter nuber of plants"); //edit update name or photo
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text

                String value = editText.getText().toString().trim();
                int valuenumb =  Integer.parseInt(editText.getText().toString().trim());

                if (!TextUtils.isEmpty(value) && valuenumb < 5){

                   // activateGrowSgeschedule(position, valuenumb);
                    startiets(valuenumb,position);
                    Log.d("actie:", "start groeien");


                }
                else {
                    Toast.makeText(getActivity(), "please enter number of plants with a maimum of 4", Toast.LENGTH_SHORT).show();
                    editText.getText().clear();
                    showNuberOfPlants(position);

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create();
        builder.show();


    }
/*
    private void activateGrowSgeschedule(int position, int numbOfPlants){
        switch (position){
            case 0:
                // start aardbeiproces
                startiets(numbOfPlants, position);
                break;
            case 1:
                startiets(numbOfPlants,position);
                break;
            case 2:
                startiets(numbOfPlants,position);
                break;
            case 4:
                startiets(numbOfPlants,position);
                break;
            case  5:
                startiets(numbOfPlants,position);
                break;
            case 6:
                break;
            case 7:
                break;
        }
    }
    */

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    private void StartAardbeiGroei(int numbOfPlants){

        // schrijf naar grobox x pad x

        for(int i =0; i<numbOfPlants; i++){
            DatabaseReference myRefTime = database.getReference("Growbox1/" + pumps[i] + "/Time" );
            DatabaseReference myRefInter = database.getReference("Growbox1/" + pumps[i] +  "/Interval" );
            DatabaseReference mRefLightinteval = database.getReference("Growbox1/light/INTERVAL");
            DatabaseReference mRefLighttime = database.getReference("Growbox1/light/TIME");
            myRefTime.setValue(5000);
            myRefInter.setValue(2500);
            mRefLightinteval.setValue(2500);
            mRefLighttime.setValue(3000);
        }


    }
    private void startiets(int numbOfPlants, int position){
        for(int i =0; i<numbOfPlants; i++){
            DatabaseReference myRefTime = database.getReference(CurrentName + "/" + pumps[i] + "/Time" );
            DatabaseReference myRefInter = database.getReference(CurrentName + "/" +  pumps[i] +  "/Interval" );
            DatabaseReference mRefLightinteval = database.getReference(CurrentName + "/light/INTERVAL");
            DatabaseReference mRefLighttime = database.getReference(CurrentName + "/light/TIME");
            DatabaseReference myRefCurrentGrow = database.getReference(CurrentName + "/CurrentGrowSchedule");
            myRefCurrentGrow.setValue(mTitle.get(position));
            myRefTime.setValue(2000);
            myRefInter.setValue(200);
            mRefLightinteval.setValue(200);
            mRefLighttime.setValue(200);
        }
    }





/// jobschedular nog nodig?
    public void scheduleJob(View v) {
        ComponentName componentName = new ComponentName(getActivity(), ExampleJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getContext().getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("start", "Job scheduled");
        } else {
            Log.d("message", "Job scheduling failed");
        }
    }

    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getContext().getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("message", "Job cancelled");
    }


}