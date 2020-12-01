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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

    Uri videoUri;
    String imageUri;
    ProgressBar progressBar;
    Button button;
    ImageView imageView;

    private StorageReference mStorageRef;

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


        videoView=view.findViewById(R.id.videoView);
        progressBar=view.findViewById(R.id.progressBar);

        imageView=view.findViewById(R.id.imageView);

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


        button = (Button) view.findViewById(R.id.button);
        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                try{
                    File file=new File(this.getExternalCacheDir());
                }
            }
        });
        */
        return view;
    }
}