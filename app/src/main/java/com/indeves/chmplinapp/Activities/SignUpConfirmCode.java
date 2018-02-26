package com.indeves.chmplinapp.Activities;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class SignUpConfirmCode extends AppCompatActivity {

    EditText codeEdit;
    private static final String TAG = "PhoneAuthActivity";
    Button button;
    private FirebaseAuth mAuth;
    UserData userData;
    FirebaseDatabase k;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm_code);
        button = findViewById(R.id.confirm_code);
        mAuth = FirebaseAuth.getInstance();
        codeEdit = findViewById(R.id.signup_verfication_code);
        k = FirebaseDatabase.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationId = getIntent().getExtras().getString("Verfication ID");
                String code = codeEdit.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                //  signInWithPhoneAuthCredential(credential);
                Auth auth = new Auth(mAuth,k);
                auth.signInWithPhoneAuthCredential(credential);

            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(SignUpConfirmCode.this, new OnCompleteListener<AuthResult>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "linkWithCredential:success");
                    String userId = mAuth.getCurrentUser().getUid().toString();
                    Log.d("uid", userId);
                    SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(SignUpConfirmCode.this);
                    mypreference.edit().putBoolean("Log In", true);
                    mypreference.edit().putString("user id", userId);
                    if (getIntent().getStringExtra("accountType").equals("user")) {
                        startActivity(new Intent(SignUpConfirmCode.this, UserProfileMain.class));
                    } else if (getIntent().getStringExtra("accountType").equals("pro")) {
                        startActivity(new Intent(SignUpConfirmCode.this, ProLandingPage.class));
                    } else {
                        startActivity(new Intent(SignUpConfirmCode.this, StuLandingPage.class));
                    }
                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.getException());
                }
            }
        });
    }

    private void writeData(UserData userData) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        String userId = mDatabase.push().getKey();

// pushing user to 'users' node using the userId
        mDatabase.child(userId).setValue(userData);
    }
}
