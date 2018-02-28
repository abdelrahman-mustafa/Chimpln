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
import android.widget.Toast;

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
import com.indeves.chmplinapp.API.AuthenticationInterface;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.PrefsManager.PrefGet;
import com.indeves.chmplinapp.PrefsManager.PrefSave;
import com.indeves.chmplinapp.PrefsManager.PrefsManager;
import com.indeves.chmplinapp.R;

public class SignUpConfirmCode extends AppCompatActivity implements AuthenticationInterface.SignInWithPhoneAuthCredentialListener {

    private static final String TAG = "PhoneAuthActivity";
    EditText codeEdit;
    Button button;
    UserData userData;
    FirebaseDatabase k;
    String accountType;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm_code);
        button = findViewById(R.id.confirm_code);
        mAuth = FirebaseAuth.getInstance();
        codeEdit = findViewById(R.id.signup_verfication_code);
        k = FirebaseDatabase.getInstance();
        accountType = getIntent().getStringExtra("accountType");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationId = getIntent().getExtras().getString("Verfication ID");
                String code = codeEdit.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                //  signInWithPhoneAuthCredential(credential);
                Auth auth = new Auth(SignUpConfirmCode.this);
                auth.signInWithPhoneAuthCredential(credential);

            }
        });
    }

    @Override
    public void onSignInWithPhoneAuthCredentialCompleted(boolean codeVerified) {
        if (codeVerified) {
            String userId = mAuth.getCurrentUser().getUid();
            Log.d("userID", userId);
            PrefSave prefSave = new PrefSave(this);
            prefSave.saveId(userId);
            // K.A: why do you get user type and then set it again?
            PrefGet prefGet = new PrefGet(this);
            prefGet.getUserType();
            Log.d("user", prefGet.getUserType());
            prefSave.saveLogInStatus(true);
            prefSave.saveUserType(accountType);
            PrefsManager prefsManager = new PrefsManager(this);
            prefsManager.goMainProfile(this);
        } else {
            Toast.makeText(this, "Invalid verification code", Toast.LENGTH_SHORT).show();
        }

    }
}
