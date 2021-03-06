package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;

import com.facebook.login.Login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class login extends AppCompatActivity implements View.OnClickListener {

    EditText mUsername, mPassword;
    Button mLoginbtn, mregisterbtn;
    FirebaseAuth mAuth;
    //SignInButton signInButton;
    ImageButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    String TAG = "MainActivity";
    private static int RC_SIGN_IN = 1;
    FirebaseFirestore mStore;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.UsernameLogin);
        mPassword = findViewById(R.id.PasswordLogin);
        mAuth = FirebaseAuth.getInstance();
        mLoginbtn = findViewById(R.id.Login_button);
        mregisterbtn = findViewById(R.id.Register_button);
        signInButton = findViewById(R.id.googleicon);
        mStore = FirebaseFirestore.getInstance();




        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



        // voor als ge al ingelogd zijt --> direct naar mainactivity --> naar onstart doen
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }

        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    mUsername.setError("USERNAME= IS REQUIRED");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("PASSWORD IS REQUIRED");
                }

                mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //verification email
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(login.this, "Login works!.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else {
                                Toast.makeText(login.this, "VERIFY EMAIL! CHECK SPAM!", Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            Toast.makeText(login.this, "Login failed!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

    }


    public void Register(View view) {
        startActivity(new Intent(getApplicationContext(), register.class));

    }

    public void forget(View view) {
        startActivity(new Intent(getApplicationContext(), forgotpassword.class));

    }


    private  void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }

    }

    private void handleSignInResult( Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(acc);
            Toast.makeText(login.this, "Google Sign in Works!", Toast.LENGTH_SHORT).show();
        }
        catch (ApiException e){
            Toast.makeText(login.this, "Signing FAiled", Toast.LENGTH_SHORT).show();
             FirebaseGoogleAuth(null);

        }

    }

    private  void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Toast.makeText(login.this, " Successful!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    // krijgen van user info --> naar profile verplaatsen?
    private void updateUI(FirebaseUser fUser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            userID = mAuth.getCurrentUser().getUid();
            DocumentReference documentReference = mStore.collection("Users").document(userID);
            //data die we willen wegschrijven
            Map<String, Object> user = new HashMap<>();
            user.put("uname", personName);
            user.put("email", personEmail);
            user.put("phone", "/");
            user.put("image", "");
            user.put("coverImage","");
            user.put("amountBoxes","0");
            user.put("amountHarvests","0");
            user.put("currentGrowbox","None");
            //  user.put("phone", phoneNumber);
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG","user profile created for " +userID);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            });

            documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG","user profile creation in database failed");
                }
            });

            Toast.makeText(login.this, userID, Toast.LENGTH_SHORT).show();


        }


    }



    @Override
    public void onClick(View v) {

    }
}