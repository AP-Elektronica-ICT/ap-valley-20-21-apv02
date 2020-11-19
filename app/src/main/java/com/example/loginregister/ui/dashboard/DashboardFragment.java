package com.example.loginregister.ui.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginregister.R;
import com.example.loginregister.ScanActivity;


public class DashboardFragment extends Fragment {
    public static final int CAMERA_PERMISSION_CODE = 100;

    private Button scan;

    ListView lv;
    String mTitel[] = {"Groeibox 1","Groeibox 2"};
    String mBeschrijving[] = {"Tijm", "Rozemarijn"};
    int image[] = {R.drawable.fotogroeibox, R.drawable.fotogroeibox};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        lv = (ListView) view.findViewById(R.id.idListView);

        MijnAdapter adapter = new MijnAdapter(getActivity(),mTitel,mBeschrijving,image);
        lv.setAdapter(adapter);

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
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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