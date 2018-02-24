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
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.R;

public class LogIn extends AppCompatActivity {

    EditText mail, pass;
    UserData user;
    Button signUp, login;
    private FirebaseAuth mAuth;
    String TAG = "This Activiy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        final ImageView splash = (ImageView) findViewById(R.id.splash);
        //splash.startAnimation(anim);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(splash, "translationY", -1000, 0),
                ObjectAnimator.ofFloat(splash, "alpha", 0, 1)
        );
        animatorSet.setDuration(2000);
       /* animatorSet.addListener(new AnimatorListenerAdapter(){
            @Override public void onAnimationEnd(Animator animation) {

                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playTogether(
                        ObjectAnimator.ofFloat(splash,"scaleX", 1f, 0.5f, 1f),
                        ObjectAnimator.ofFloat(splash,"scaleY", 1f, 0.5f, 1f)
                );
                animatorSet2.setInterpolator(new AccelerateInterpolator());
                animatorSet2.setDuration(1000);
                animatorSet2.start();

            }
        });
       */
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

                mAuth.signInWithEmailAndPassword(email, passw)
                        .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                            @SuppressLint("CommitPrefEdits")
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    String userId = mAuth.getCurrentUser().getUid();
                                    Log.d("uid", userId);
                                    SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(LogIn.this);
                                    mypreference.edit().putBoolean("Log In", true);
                                    mypreference.edit().putString("user id", userId);
                                    readData(userId);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                }

                                // ...
                            }
                        });


            }
        });


    }

    private void readData(String userId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserData.class);
                Log.d("h", user.toString());
                if (user.type.equals("user")) {
                    Intent intent = new Intent(LogIn.this, UserProfileMain.class);
                    startActivity(intent);
                } else if (user.type.equals("pro")) {
                    Intent intent = new Intent(LogIn.this, ProLandingPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LogIn.this, StuLandingPage.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
