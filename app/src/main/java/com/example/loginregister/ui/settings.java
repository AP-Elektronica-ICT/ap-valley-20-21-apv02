package com.example.loginregister.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.loginregister.R;
import com.example.loginregister.ui.home.HomeFragment;

public class settings extends Fragment {




    public settings() {
        // Required empty public constructor
    }
    private static final String BACK_STACK_ROOT_TAG = "settings2";
    Button btnCancel, btnSave;
    NavController navc;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        btnSave = view.findViewById(R.id.save_button);
        btnCancel = view.findViewById(R.id.cancel_button);

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





        // Inflate the layout for this fragment
        return view;
    }
}