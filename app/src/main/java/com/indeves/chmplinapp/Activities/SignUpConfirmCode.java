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
                auth.setContext(SignUpConfirmCode.this);
                auth.setEmail(getIntent().getExtras().getString("mail"));
                auth.setPassword(getIntent().getExtras().getString("pass"));
                auth.setPhone(getIntent().getExtras().getString("phone"));
                auth.setType(getIntent().getExtras().getString("accountType"));
                auth.signInWithPhoneAuthCredential(credential);

            }
        });
    }

    @Override
    public void onSignInWithPhoneAuthCredentialCompleted(boolean codeVerified) {
        if (codeVerified) {
            Log.d("userID", mAuth.getCurrentUser().getUid());
            PrefSave prefSave = new PrefSave(this);
            prefSave.saveId(mAuth.getCurrentUser().getUid());
            prefSave.saveLogInStatus(true);
            prefSave.saveUserType(accountType);
            switch (accountType) {
                case "stu":
                    startActivity(new Intent(SignUpConfirmCode.this, ProRegActivity.class));
                    SignUpConfirmCode.this.finish();
                    break;
                case "user":
                    startActivity(new Intent(SignUpConfirmCode.this, UserProfileMain.class));
                    SignUpConfirmCode.this.finish();
                    break;
                case "pro":
                    startActivity(new Intent(SignUpConfirmCode.this, ProRegActivity.class));
                    SignUpConfirmCode.this.finish();
                    break;
            }
        } else {
            Toast.makeText(this, "Invalid verification code", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
