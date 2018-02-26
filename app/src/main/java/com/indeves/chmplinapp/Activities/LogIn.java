package com.indeves.chmplinapp.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indeves.chmplinapp.API.Auth;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.R;

public class LogIn extends AppCompatActivity {

    EditText mail, pass;
    UserData user;
    Button signUp, login;
    private FirebaseAuth mAuth;
    String TAG = "This Activiy";

FirebaseDatabase k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        final ImageView splash = (ImageView) findViewById(R.id.splash);
        //splash.startAnimation(anim);
        k = FirebaseDatabase.getInstance();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(splash, "translationY", -1000, 0),
                ObjectAnimator.ofFloat(splash, "alpha", 0, 1)
        );
        animatorSet.setDuration(2000);
        animatorSet.start();


        mail = findViewById(R.id.logIn_userName);
        pass = findViewById(R.id.logIn_pass);
        signUp = findViewById(R.id.LogIn_button_SignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);

            }
        });

        login = findViewById(R.id.LogIn_button_SignIn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mail.getText().toString().trim();
                String passw = pass.getText().toString().trim();

                Auth auth = new Auth(mAuth,k);
                auth.login(email, passw, LogIn.this);


            }
        });


    }
}
