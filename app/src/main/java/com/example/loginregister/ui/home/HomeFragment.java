package com.example.loginregister.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.squareup.picasso.Picasso;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

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
                if (GrowboxName == "None") {
                    // None zal enkel voorkomen wanneer de user zich nog niet heeft aangemeld
                    // in dat geval zullen we een welcome shizzle laten afspelen alvorens we naar de Homefragment gaan

                } else {
                    DatabaseReference myRefTime = database.getReference(GrowboxName + "/CurrentGrowSchedule");
                    myRefTime.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            currentGrow = dataSnapshot.getValue(String.class);
                            //Log.d("actie",currentGrow);
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
                                DatabaseReference myRefTime = database.getReference(GrowboxName + "/CurrentGrowSchedule");
                                myRefTime.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        growpogingtot = dataSnapshot.getValue(String.class);
                                        //Log.d("growpogingtot", growpogingtot);

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