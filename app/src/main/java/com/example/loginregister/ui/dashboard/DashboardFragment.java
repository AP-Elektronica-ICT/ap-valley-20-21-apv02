package com.example.loginregister.ui.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.loginregister.R;
import com.example.loginregister.ScanActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {
    public static final int CAMERA_PERMISSION_CODE = 100;

    private Button scan;
    private int Count=0;

    ListView listView;
    List<String> mTitle = new ArrayList<>();
    List<String> mDescription = new ArrayList<>();
    List<String> images = new ArrayList<>();

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;
    public DashboardFragment() {
        // Required empty public constructor

    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        listView = (ListView) view.findViewById(R.id.listViewId);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mStore = FirebaseFirestore.getInstance();
        mStore.collection("Users").document(userID).collection("0").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId().toString());
                        mTitle.add(document.getString("naam"));
                        mDescription.add(document.getString("growing"));
                        images.add(document.getString("url"));
                    }
                    MyAdapter adapter = new MyAdapter(getActivity(), mTitle, mDescription, images);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    Log.d("greetz", "clicked");
                                    showSelected("ALERT", position);
                                    break;
                                case 1:
                                    Toast.makeText(getContext(), mDescription.get(1), Toast.LENGTH_LONG);
                                    showSelected("ALERT", position);
                                    break;
                                case 2:
                                    Toast.makeText(getContext(), mDescription.get(2), Toast.LENGTH_LONG);
                                    showSelected("ALERT", position);
                                    break;
                                case 3:
                                    Toast.makeText(getContext(), mDescription.get(3), Toast.LENGTH_LONG);
                                    showSelected("ALERT", position);
                                    break;
                                case 4:
                                    Toast.makeText(getContext(), mDescription.get(4), Toast.LENGTH_LONG);
                                    showSelected("ALERT", position);
                                    break;

                            }
                        }
                    });

                    Log.d("geladen", list.toString());
                } else {
                    Log.d("wassup", "wassjp");
                }

            }
        });



        scan = (Button) view.findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

                if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                else{
                    Intent intent = new Intent(getActivity(), ScanActivity.class);
                    startActivity(intent);
                }

            }
        });

        if(Count>0){
            //Wanneer er van de Activity naar het fragement een bericht gestuurd wordt, wordt er een groeibox toegevoegd
            String strtext=getArguments().getString("message");
            if(strtext!=""){
                mTitle.add(strtext);
                mDescription.add("Default beschrijving");
                images.add("https://st.depositphotos.com/1654249/2526/i/600/depositphotos_25269357-stock-photo-3d-man-with-red-question.jpg");
            }
        }
        Count++;



        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"Permission Granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(getActivity(),permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        }else {
            Toast.makeText(getActivity(),"Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
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
            View row = layoutInflater.inflate(R.layout.row_list_home, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.title_main1);
            TextView myDescription = row.findViewById(R.id.title_sub1);

            Picasso.get().load(rImgs.get(position)).into(images);
            //  images.setImageResource(rImgs.get(position));
            myTitle.setText(rTitle.get(position));
            myDescription.setText(rDescription.get(position));
            return row;
        }
    }
    private void showSelected(final String key, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(key);
        //set layout of dialog
        builder.setMessage("Are You sure you want to select " + mTitle.get(position));
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        //add edit text

        builder.setView(linearLayout);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                Log.d("actie:", "aantalplaten ingeven");

                // selected growbox in firebase zetten
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create();
        builder.show();


    }

}