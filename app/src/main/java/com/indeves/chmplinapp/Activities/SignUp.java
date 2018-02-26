package com.indeves.chmplinapp.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indeves.chmplinapp.API.Auth;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.R;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    RadioButton radioButton, radioButton2, radioButton3;
    Button createAccount;
    private static String phoneNum;
    EditText mail, phone, pass, confirmPass;
    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    FirebaseDatabase k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        createAccount = findViewById(R.id.signUp_button_createAccount);
        radioButton = findViewById(R.id.signUp_radio_user_account);
        radioButton2 = findViewById(R.id.signUp_radio_pro_account);
        radioButton3 = findViewById(R.id.signUp_radio_stu_account);
        mail = findViewById(R.id.signUp_email);
        phone = findViewById(R.id.signUp_phone);
        pass = findViewById(R.id.signUp_pass);
        confirmPass = findViewById(R.id.signUp_confirm_pass);


        final ImageView splash = (ImageView) findViewById(R.id.splash);
        //splash.startAnimation(anim);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(splash, "translationY", -1000, 0),
                ObjectAnimator.ofFloat(splash, "alpha", 0, 1)
        );
        animatorSet.setDuration(2000);
        animatorSet.start();


        // create account
        mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mail", mail.getText().toString());
                final String email = mail.getText().toString();
                Auth auth = new Auth(mAuth, k);
                final String password = pass.getText().toString();
                phoneNum = phone.getText().toString();

                if (radioButton.isChecked()) {
                    auth.createNewAccount(email, password, phoneNum, "user", SignUp.this);

                } else if (radioButton2.isChecked()) {
                    auth.createNewAccount(email, password, phoneNum, "pro", SignUp.this);

                } else {
                    auth.createNewAccount(email, password, phoneNum, "stu", SignUp.this);

                }


            }
        });
    }


}
