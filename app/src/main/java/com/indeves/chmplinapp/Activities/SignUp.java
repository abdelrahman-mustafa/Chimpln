package com.indeves.chmplinapp.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.indeves.chmplinapp.API.AuthenticationInterface;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.PrefsManager.PrefGet;
import com.indeves.chmplinapp.PrefsManager.PrefSave;
import com.indeves.chmplinapp.PrefsManager.PrefsManager;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CheckError;
import com.indeves.chmplinapp.Utility.Toasts;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity implements AuthenticationInterface.SinUpListener, AuthenticationInterface.PhoneVerificationListener, AuthenticationInterface.SignInWithPhoneAuthCredentialListener {
    RadioButton radioButton, radioButton2, radioButton3;
    Button createAccount;
    private static String phoneNum;
    EditText mail, phone, pass, confirmPass;
    private static final String TAG = "EmailPassword";
    private Toasts toasts;
    private FirebaseAuth mAuth;
    FirebaseDatabase k;
    String email;
    String password;

    com.wang.avi.AVLoadingIndicatorView avi;
    Auth auth = new Auth(SignUp.this, SignUp.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth.setContext(SignUp.this);
        createAccount = findViewById(R.id.signUp_button_createAccount);
        radioButton = findViewById(R.id.signUp_radio_user_account);
        radioButton2 = findViewById(R.id.signUp_radio_pro_account);
        radioButton3 = findViewById(R.id.signUp_radio_stu_account);
        mail = findViewById(R.id.signUp_email);
        phone = findViewById(R.id.signUp_phone);
        avi = (AVLoadingIndicatorView) findViewById(R.id.indicator);
        pass = findViewById(R.id.signUp_pass);
        confirmPass = findViewById(R.id.signUp_confirm_pass);

        toasts = new Toasts(SignUp.this);
        final ImageView splash = (ImageView) findViewById(R.id.splash);
        //splash.startAnimation(anim);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(splash, "translationY", -1000, 0),
                ObjectAnimator.ofFloat(splash, "alpha", 0, 1)
        );
        animatorSet.setDuration(2000);
        animatorSet.start();
        stopAnim();


        // create account
        mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mail", mail.getText().toString());
                startAnim();
                email = mail.getText().toString();
                password = pass.getText().toString();
                phoneNum = phone.getText().toString();
                String confirmpass = confirmPass.getText().toString();
                CheckError checkError = new CheckError();
                if (!checkError.checkEmpty(email, phoneNum, password, confirmpass)) {
                    // toast error
                    stopAnim();

                    toasts.completeData();
                } else if (!checkError.checkPassMatch(password, confirmpass)) {
                    // toast error
                    stopAnim();

                    toasts.passDismatch();

                } else {
                    if (radioButton.isChecked()) {
                        auth.createNewAccount(email, password, phoneNum, "user", SignUp.this);

                    } else if (radioButton2.isChecked()) {
                        auth.createNewAccount(email, password, phoneNum, "pro", SignUp.this);

                    } else if (radioButton3.isChecked()) {
                        auth.createNewAccount(email, password, phoneNum, "stu", SignUp.this);

                    } else {
                        // toast error
                        toasts.noType();
                        stopAnim();

                    }
                }


            }
        });
    }

    void startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    public void onSignUpWithEmailAndPasswordComplete(boolean signUpSuccessful) {

        if (signUpSuccessful) {
            auth.veifyphone();
        } else {
            stopAnim();
        }
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    @Override
    public void onCodeSent(String VerficationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        Log.d("JEJE", "onCodeSent:" + VerficationID);
        String userId = mAuth.getCurrentUser().getUid().toString();
        PrefSave prefSave = new PrefSave(SignUp.this);
        prefSave.saveId(userId);
        prefSave.saveLogInStatus(true);
        prefSave.saveUserType(auth.getType());

        Context context = SignUp.this;
        Intent intent = new Intent(context, SignUpConfirmCode.class);
        intent.putExtra("accountType", auth.getType());
        intent.putExtra("Verfication ID", VerficationID);
        intent.putExtra("mail", auth.getEmail());
        intent.putExtra("pass", auth.getPassword());
        intent.putExtra("resend", forceResendingToken);
        intent.putExtra("phone", auth.getPhone());
        context.startActivity(intent);

    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        stopAnim();

    }

    @Override
    public void onSignInWithPhoneAuthCredentialCompleted(boolean codeVerified) {
        if (codeVerified) {
            String userId = mAuth.getCurrentUser().getUid();
            Log.d("userID", userId);
            PrefSave prefSave = new PrefSave(this);
            prefSave.saveId(userId);
            prefSave.saveLogInStatus(true);
            prefSave.saveUserType(auth.getType());
            PrefsManager prefsManager = new PrefsManager(this);
            prefsManager.goCompleteData(this);
        } else {
            Toast.makeText(this, "Invalid verification code", Toast.LENGTH_SHORT).show();
            stopAnim();

        }
    }

}
