package com.example.loginregister.ui.home;

import android.content.Context;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class GrowSchedules extends Fragment {

    ListView listView;
    List<String> mTitle = new ArrayList<>();
    List<String> mDescription = new ArrayList<>();
    List<Integer> images = new ArrayList<>();

    private DatabaseReference mDatabase;
    public GrowSchedules() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // onderstaand van firebase halen!
        //--> bij selecteren een altert laten opkomen --> bevestiging hiervan start nieuwe groei
        mTitle.add("Mango");
        mTitle.add("Apple");
        mTitle.add("Banana");
        mTitle.add("Grapes");
        mTitle.add("Mango");
        mTitle.add("Apple");
        mTitle.add("Banana");
        mTitle.add("Grapes");

        mDescription.add("mango beschrijving");
        mDescription.add("apple beschrijving");
        mDescription.add("banana beschrijving");
        mDescription.add("grapes beschrijving");
        mDescription.add("mango beschrijving");
        mDescription.add("apple beschrijving");
        mDescription.add("banana beschrijving");
        mDescription.add("grapes beschrijving");

        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);
        images.add(R.drawable.com_facebook_button_icon);



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