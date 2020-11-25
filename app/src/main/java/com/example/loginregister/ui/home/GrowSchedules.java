package com.example.loginregister.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginregister.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class GrowSchedules extends Fragment {

    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<String> mTitle = new ArrayList<>();
    List<String> mDescription = new ArrayList<>();
    List<Integer> images = new ArrayList<>();
    String titel;
    boolean ok = false;

    FirebaseFirestore mStore;
    public GrowSchedules() {
        // Required empty public constructor

    }
    boolean jos = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // onderstaand van firebase halen!
        //--> bij selecteren een altert laten opkomen --> bevestiging hiervan start nieuwe groei

        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
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


        View view = inflater.inflate(R.layout.fragment_grow_schedules, container, false);

        listView = view.findViewById(R.id.listview);
        // now create an adapter class

        MyAdapter adapter = new MyAdapter(getActivity(), mTitle, mDescription, images);
        listView.setAdapter(adapter);
        // there is my mistake...
        // now again check this..

        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Toast.makeText(getContext(), "mango beschrijving", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getContext(), "apple beschrijving", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getContext(), "banana beschrijving", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getContext(), "grapes beschrijving", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getContext(), "grapes beschrijving", Toast.LENGTH_SHORT).show();
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
        List<Integer> rImgs;

        MyAdapter (Context c, List<String> title, List<String> description, List<Integer> imgs) {
            super(c, R.layout.row_list_grow, R.id.title_main1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_list_grow, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.title);
            TextView myDescription = row.findViewById(R.id.info);

            // now set our resources on views
            images.setImageResource(rImgs.get(position));
            myTitle.setText(rTitle.get(position));
            myDescription.setText(rDescription.get(position));
            return row;
        }
    }
}