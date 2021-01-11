package com.example.loginregister.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginregister.MainActivity;
import com.example.loginregister.R;
import com.example.loginregister.login;
import com.facebook.login.Login;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executor;

import io.grpc.Context;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ProfileFragment extends Fragment {

    Button btnVeranderFoto;
    ImageButton mLogout;
    ImageView ProfielFoto;
    TextView naam, gsm, mail, amountBoxes, amountHarvests, mAddress;


    DatabaseReference reff;
    FirebaseUser muser;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String userID;
    String storagePath = "Users_Profile_Imgs/";
    Uri image_uri;
    ImageView mprofilePic;
    FloatingActionButton fab;
    ProgressDialog pd;

    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA__CODE = 400;

    String cameraPermissions[];
    String storagePermissions[];


    public ProfileFragment() {
    }




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        naam = view.findViewById(R.id.username_Textview);
        gsm = view.findViewById(R.id.phonenumber_Textview);
        mail = view.findViewById(R.id.userEmail_Textview);
        mprofilePic = view.findViewById(R.id.profilePic);
        fab = view.findViewById(R.id.EditProfile);
        amountBoxes = view.findViewById(R.id.txtAmountBoxes);
        amountHarvests = view.findViewById(R.id.txtAmountHarvests);
        mAddress = view.findViewById(R.id.adress_textView);
        mLogout = view.findViewById(R.id.logout_button);

        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        muser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        // databaseReference = firebaseDatabase.getReference("Users");

        userID = mAuth.getCurrentUser().getUid();
        storageReference = getInstance().getReference(); //fbase stor ref

        pd = new ProgressDialog(getActivity());


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation();

        } else {
            //no permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        //Code stef die reeds werkt --> betere optie mogelijk
        DocumentReference documentReference = mStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String _naam = documentSnapshot.getString("uname");
                String _gsm = documentSnapshot.getString("phone");
                String _mail = documentSnapshot.getString("email");
                String _foto = documentSnapshot.getString("image");
                String _amountBoxes = documentSnapshot.getString("amountBoxes");
                String _amountHarvests = documentSnapshot.getString("amountHarvests");
                naam.setText(_naam);
                gsm.setText(_gsm);
                mail.setText(_mail);
                amountBoxes.setText(_amountBoxes);
                amountHarvests.setText((_amountHarvests));
                try {
                    Picasso.get().load(_foto).fit().transform(new CropCircleTransformation()).centerCrop().rotate(90).into(mprofilePic);
                   // Picasso.get().load(_foto).into(mprofilePic);

                } catch (Exception a) {
                    Picasso.get().load(R.drawable.ic_settings);
                }
            }
        });


        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertsignout();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowEditProfileDialog();

            }
        });

        return view;
    }

    @SuppressLint("MissingPermission")
    private void getlocation() {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null){


                        try {
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );
                            mAddress.setText(addresses.get(0).getLocality() +"," + addresses.get(0).getCountryName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

   /*
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);


    }


   */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission(){
        //min api verhogen
        requestPermissions(storagePermissions ,STORAGE_REQUEST_CODE);
    }



    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }
    //min api verhogen
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }


    private void ShowEditProfileDialog(){
        /*
        show dialog heeft volgende opties
        1. foto kunnen bewerken --> zowel uit album als camera
        2. naam, telefoon etc (user info dus) aanpassen.
         */
        String options[] = {"Edit profile picture"," Edit name","Edit phone number","Edit e-mail"};
        
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //edit profile picture
                        pd.setMessage("updating profile picture");
                        showImagePicDialog();
                        break;
                    case 1:
                        // edit name
                        pd.setMessage("updating name");
                        showNamePhoneUpdateDialog("uname");

                        break;
                    case 2:
                        //edit phone number
                        pd.setMessage("updating phone number");
                        showNamePhoneUpdateDialog("phone");

                        break;
                    case 3:
                        //edit email
                        pd.setMessage("updating mail");

                        break;
                    default:
                        break;
                }

            }
        });
        builder.create().show();



    }

    private void showNamePhoneUpdateDialog(final String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("update"+ key);
        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        //add edit text
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter"+key); //edit update name or photo
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(value)){

                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);
                    DocumentReference documentReference = mStore.collection("Users").document(userID);
                    documentReference.update(key, value);

                }
                else {
                    Toast.makeText(getActivity(), "please enter"+key, Toast.LENGTH_SHORT).show();

                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });
        //create and show dialog
        builder.create();
        builder.show();


    }

    private void showImagePicDialog(){
         /*
        show dialog heeft volgende opties
        1. foto kunnen bewerken --> zowel uit album als camera
         */
        String options[] = {"Camera"," Gallery"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //camera
                        if(!checkCameraPermission()){
                            requestCameraPermission();
                        }
                        else {
                            pickFromCamera();
                        }
                        break;
                    case 1:
                        // gallery
                        pd.setMessage("updating name");
                        if(!checkStoragePermission()){
                            requestStoragePermission();
                        }
                        else {
                            requestStoragePermission();
                        }

                        break;
                    default:
                        break;
                }

            }
        });
        builder.create().show();



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //deze methode wordt aangeroepen wanneer de user Allow of Deny kiest van de dialog
        //deze keuze wordt hier afgehandeld

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    //checken of we toegang hebben tot camera
                    boolean cameraAccepted =grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }
                }
                else {
                    //toegang geweigerd
                    Toast.makeText(getActivity(), "please enable camera & storage permission", Toast.LENGTH_SHORT).show();

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                //van gallerij: eerst checkn of we hiervoor toestemming hebben
                if (grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickFromGallery();
                    }
                }
                else {
                    //toegang geweigerd
                    Toast.makeText(getActivity(), "please enalble  storage permission", Toast.LENGTH_SHORT).show();

                }

            }
            break;
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //deze methode wordt opgeroepne na het nemen van een foto van camera of gallerij
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //abeelding gekozen vanuit de gallerij --> verkrijgen van uri van de image
                image_uri = data.getData();

                uploadProfileCoverphoto(image_uri);

            }
            if (requestCode == IMAGE_PICK_CAMERA__CODE) {
                //afbeelding gekozen met camera
                uploadProfileCoverphoto(image_uri);


            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void uploadProfileCoverphoto(final Uri uri) {


        //path and name of image t be stored in firebase storage
        String filePathandName = storagePath+ "" + "image" + "_"+ userID;

        StorageReference storageReference2 = storageReference.child(filePathandName);

        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUti = uriTask.getResult();

                        //check if image is dowloaded or not
                        if (uriTask.isSuccessful()){
                            //image upload
                            //add/update url in users database
                            HashMap<String, Object> results = new HashMap<>();
                            results.put("image", downloadUti.toString());
                            DocumentReference documentReference = mStore.collection("Users").document(userID);
                            documentReference.update("image", downloadUti.toString());

                        }
                        else {
                            //error
                            Toast.makeText(getContext(), "Some error occured", Toast.LENGTH_SHORT).show();


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });




    }

    private void pickFromCamera() {
        //intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA__CODE);

    }

    //check
    private void pickFromGallery(){
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);


    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getActivity(), login.class));
    }

    public void alertsignout()
    {
        AlertDialog.Builder alertDialog2 = new
                AlertDialog.Builder(
                getActivity());

        // Setting Dialog Title
        alertDialog2.setTitle("Confirm SignOut");

        // Setting Dialog Message
        alertDialog2.setMessage("Are you sure you want to Signout?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(),
                                login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Toast.makeText(getContext(),
                                "You clicked on NO", Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();


    }

}