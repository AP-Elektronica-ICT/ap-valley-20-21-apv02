package com.example.loginregister.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.loginregister.R;

public class settings extends Fragment {




    public settings() {
        // Required empty public constructor
    }

    Button btnCancel, btnSave;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnSave = view.findViewById(R.id.save_button);
        btnCancel = view.findViewById(R.id.cancel_button);




        btnCancel.setOnClickListener(new view.oncl);

        // Inflate the layout for this fragment
        return view;
    }
}