package com.example.loginregister.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.loginregister.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class Liveview extends Fragment {

    VideoView videoView;

    Uri videoUri, fotoUri;
    String imageUri;
    ProgressBar progressBar;
    ImageButton button1,button2,button3,button4,button5, pijl;
    NavController navc;

    private static final String BACK_STACK_ROOT_TAG = "navigation_liveview";


    private StorageReference mStorageRef, mStorageRef2;

    public Liveview() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liveview, container, false);

        setHasOptionsMenu(true);

        videoView=view.findViewById(R.id.videoView);
        progressBar=view.findViewById(R.id.progressBar);


        mStorageRef = FirebaseStorage.getInstance().getReference();

        mStorageRef.child("video/video.mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                videoUri=uri;
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();
            }
        });


        videoView.setVideoURI(videoUri);
        videoView.requestFocus();

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                if(what==mp.MEDIA_INFO_BUFFERING_START){
                    progressBar.setVisibility(View.VISIBLE);
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });

        videoView.start();


        button1 = view.findViewById(R.id.imageButton);
        button2 = view.findViewById(R.id.imageButton2);
        button3 = view.findViewById(R.id.imageButton8);
        button4 = view.findViewById(R.id.imageButton4);
        button5 = view.findViewById(R.id.imageButton9);
        pijl = view.findViewById(R.id.imgpijl);

        mStorageRef2 = FirebaseStorage.getInstance().getReference();

        mStorageRef2.child("foto/foto.JPG").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                fotoUri=uri;
            }
        });


        //Voorlopig allemaal dezelfde code => wordt nog aangepast !

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/JPG");

                intent.putExtra(Intent.EXTRA_STREAM, fotoUri);
                startActivity(Intent.createChooser(intent , "Share"));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/JPG");

                intent.putExtra(Intent.EXTRA_STREAM, fotoUri);
                startActivity(Intent.createChooser(intent , "Share"));
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/JPG");

                intent.putExtra(Intent.EXTRA_STREAM, fotoUri);
                startActivity(Intent.createChooser(intent , "Share"));
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/JPG");

                intent.putExtra(Intent.EXTRA_STREAM, fotoUri);
                startActivity(Intent.createChooser(intent , "Share"));
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/JPG");

                intent.putExtra(Intent.EXTRA_STREAM, fotoUri);
                startActivity(Intent.createChooser(intent , "Share"));
            }
        });


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        pijl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();

                // Add the new tab fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, homeFragment)
                        .addToBackStack(BACK_STACK_ROOT_TAG)
                        .commit();
                navc = Navigation.findNavController(view);
                navc.navigate(R.id.action_liveview3_to_navigation_home);


            }
        });


        return view;
    }
}