package com.example.loginregister.ui.home;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.loginregister.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class HomeFragment extends Fragment {
    private static final String TAG = "MainActivity";

    private static final String BACK_STACK_ROOT_TAG = "navigation_home";

    Button cancel, start, navigatie, btnLiveview;
    NavController navc;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        start = view.findViewById(R.id.schedule_button);
        cancel = view.findViewById(R.id.cancel_button);
        btnLiveview=view.findViewById(R.id.btnLiveview);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);



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