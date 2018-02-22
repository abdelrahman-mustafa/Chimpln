package com.indeves.chmplinapp.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.indeves.chmplinapp.R;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    RadioButton radioButton, radioButton2, radioButton3;
    RadioGroup radioGroup;
    Button createAccount;
    private static String phoneNum;
    EditText mail, phone, pass, confirmPass;
    private static final String TAG = "EmailPassword";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
  //  private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;


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


        // get selected radio button from radioGroup

        // find the radiobutton by returned id

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mail", mail.getText().toString());
                mAuth.createUserWithEmailAndPassword(mail.getText().toString().trim(), pass.getText().toString().trim())
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {


                                    final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                            Log.d("JEJE", "onVerificationCompleted:" + phoneAuthCredential);
                                        }

                                        @Override
                                        public void onVerificationFailed(FirebaseException e) {
                                            Log.w("JEJE", "onVerificationFailed", e);
                                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                                Log.d("JEJE", "INVALID REQUEST");
                                            } else if (e instanceof FirebaseTooManyRequestsException) {
                                                Log.d("JEJE", "Too many Request");
                                            }
                                        }
                                        @Override
                                        public void onCodeSent(String VerficationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            super.onCodeSent(VerficationID, forceResendingToken);
                                            Log.d("JEJE", "onCodeSent:" + VerficationID);
                                            mResendToken = forceResendingToken;
                                            phoneNum = phone.getText().toString();
                                          // pass the verfication id and phomne number to check code
                                            if (radioButton.isChecked()) {
                                                Intent intent = new Intent(SignUp.this, SignUpConfirmCode.class);
                                                intent.putExtra("accountType", "user");
                                                intent.putExtra("Verfication ID",VerficationID);
                                                startActivity(intent);
                                            } else if (radioButton2.isChecked()) {
                                                Intent intent = new Intent(SignUp.this, SignUpConfirmCode.class);
                                                intent.putExtra("accountType", "pro");
                                                intent.putExtra("Verfication ID",VerficationID);
                                                startActivity(intent);

                                            } else {
                                                Intent intent = new Intent(SignUp.this, SignUpConfirmCode.class);
                                                intent.putExtra("accountType", "stu");
                                                intent.putExtra("Verfication ID",VerficationID);
                                                startActivity(intent);
                                            }
                                        }
                                    };
                                    verifyPhone(phone.getText().toString().trim(),mCallBacks);


                                }
                            }
                        });


            }
        });
    }





    public void verifyPhone(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallback
    }
}
