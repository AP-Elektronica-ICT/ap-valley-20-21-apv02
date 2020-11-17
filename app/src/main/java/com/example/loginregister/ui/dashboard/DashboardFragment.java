package com.example.loginregister.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginregister.R;

public class DashboardFragment extends Fragment {

    ListView lv;
    String mTitel[] = {"Groeibox 1","Groeibox 2"};
    String mBeschrijving[] = {"Tijm", "Rozemarijn"};
    int image[] = {R.drawable.fotogroeibox, R.drawable.fotogroeibox};

    //SearchView searchView;
    //ArrayAdapter<String> adapter;
    //String[] data = {"Groeibox1","Groeibox2","Groeibox3"};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        lv = (ListView) view.findViewById(R.id.idListView);
        //adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,data);
        //lv.setAdapter(adapter);
        MijnAdapter adapter = new MijnAdapter(getContext(),mTitel,mBeschrijving,image);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Toast.makeText(getContext(), "Groeibox 1", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Groeibox 2", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return view;
    }

    class MijnAdapter extends ArrayAdapter<String>{
        Context context;
        String rTitel[];
        String rBeschrijving[];
        int rImgs[];

        MijnAdapter (Context c, String titel[], String beschrijving[], int imgs[]){
            super(c, R.layout.row_list_home, R.id.title_main1, titel);
            this.context = c;
            this.rTitel= titel;
            this.rBeschrijving = beschrijving;
            this.rImgs = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rij = layoutInflater.inflate(R.layout.row_list_home, parent, false);
            ImageView images = rij.findViewById(R.id.image);
            TextView mijnTitel = rij.findViewById(R.id.title_main1);
            TextView mijnBeschrijving = rij.findViewById(R.id.title_sub1);

            images.setImageResource(rImgs[position]);
            mijnTitel.setText(rTitel[position]);
            mijnBeschrijving.setText(rBeschrijving[position]);

            return rij;
        }
    }

}