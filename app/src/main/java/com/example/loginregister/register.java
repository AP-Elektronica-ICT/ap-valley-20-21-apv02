package com.example.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity implements View.OnClickListener {

    EditText mUsername, mEmail, mPassword, mRepeatpassword, mPhoneNumber;

    Button mRegisterbtn;
    TextView mLoginbtn;
    private FirebaseAuth mAuth;
    //Progressbar progressbar
    FirebaseFirestore mStore;
    String userID;

    /*
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }
    }
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.UnameRegis);
        mEmail = findViewById(R.id.EmailRegister);
        mPassword = findViewById(R.id.PasswordRegister);
        mRepeatpassword = findViewById(R.id.PasswordRepeatRegister);
        mPhoneNumber = findViewById(R.id.PhoneRegister);
        mRegisterbtn = findViewById(R.id.RegisterButton);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();



        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String username = mUsername.getText().toString();
                final String phoneNumber = mPhoneNumber.getText().toString();

                // valideren van user input --> nog verbeteren
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("password is required");
                    return;
                }

                if (password.length() < 8){
                    mPassword.setError(" password must be longer dan 8 characters");
                    return;
                }


                //register user in firebase

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //versturen van verificatie email.
                            Toast.makeText(register.this, "USER CREATED.", Toast.LENGTH_SHORT).show();
                            mAuth.getCurrentUser().sendEmailVerification();
                            //voor we naar login gaan eerst user profile gegevens wegschrijven naar de database
                            //userID nemen van de registerende user
                            userID = mAuth.getCurrentUser().getUid();

                            //selecteren van de kolom waar je wilt opslagen
                            DocumentReference documentReference = mStore.collection("Users").document(userID);

                            //data die we willen wegschrijven
                            Map<String, Object> user = new HashMap<>();
                            user.put("uname", username);
                            user.put("email", email);
                            user.put("phone", phoneNumber);
                            user.put("image", "");
                            user.put("coverImage","");
                            user.put("amountBoxes","0");
                            user.put("amountHarvests","0");
                            user.put("currentGrowbox","None");


                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","user profile created for " +userID);
                                }
                            });

                            //documentReference.set(documentReference.collection("0"));
                            //documentReference.set(documentReference.collection("0"));

                            documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","user profile creation in database failed");
                                }
                            });


                            //inloggen
                          //  startActivity(new Intent(getApplicationContext(), login.class));
                        }else {
                            //indien de user niet kon worden aangemaakt
                            Toast.makeText(register.this, "USER REGISTER FAILED.", Toast.LENGTH_LONG).show();
                            Toast.makeText(register.this, "ERROR!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


    }

    public void Login(View view) {
        startActivity(new Intent(getApplicationContext(), login.class));

    }

    @Override
    public void onClick(View v) {

    }
}