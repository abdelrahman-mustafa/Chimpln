package com.indeves.chmplinapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.indeves.chmplinapp.R;

public class SignUpConfirmCode extends AppCompatActivity {

    EditText codeEdit;
    private static final String TAG = "PhoneAuthActivity";
    Button button;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm_code);
        button = findViewById(R.id.confirm_code);
        mAuth = FirebaseAuth.getInstance();
        codeEdit = findViewById(R.id.signup_verfication_code);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationId =getIntent().getExtras().getString("Verfication ID");
                String code = codeEdit.getText().toString().trim();
                // [START verify_with_code]
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);



                if (getIntent().getStringExtra("accountType").equals("user")) {
                    startActivity(new Intent(SignUpConfirmCode.this, UserProfileMain.class));
                } else if (getIntent().getStringExtra("accountType").equals("pro")) {
                    startActivity(new Intent(SignUpConfirmCode.this, ProLandingPage.class));
                } else {
                    startActivity(new Intent(SignUpConfirmCode.this, StuLandingPage.class));
                }
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG, user.getEmail().toString());

                            // [START_EXCLUDE]
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
}
