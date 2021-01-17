package com.example.loginregister.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
    TextView watertxt,lighttxt;
    ImageButton btnwateradd,btnwatersub,btnlightadd,btnlightsub;


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

        btnwateradd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double getal=Double.parseDouble(watertxt.getText().toString());
                getal++;
                watertxt.setText(getal.toString());
            }
        });

        btnwatersub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double getal=Double.parseDouble(watertxt.getText().toString());
                getal--;
                watertxt.setText(getal.toString());
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





        // Inflate the layout for this fragment
        return view;
    }
}