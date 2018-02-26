package com.indeves.chmplinapp.API;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.indeves.chmplinapp.Activities.SignUp;
import com.indeves.chmplinapp.Activities.SignUpConfirmCode;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.PrefsManager.PrefGet;
import com.indeves.chmplinapp.PrefsManager.PrefSave;
import com.indeves.chmplinapp.PrefsManager.PrefsManager;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Created by boda on 2/24/18.
 */

public class Auth {
    private Context context;
    private Activity activity;
    private String type;
    private String email;
    private String password;
    private String phone;
    private UserData userData;
    private FirebaseAuth mAuth;
    private FirebaseDatabase k;

    public Auth(FirebaseAuth mAuth, FirebaseDatabase k) {
        this.mAuth = mAuth;
        this.k = k;
    }

    public void veifyphone() {

        activity = (Activity) context;
        final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("JEJE", "onVerificationCompleted:" + phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);


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
                String userId = mAuth.getCurrentUser().getUid().toString();
                PrefSave prefSave = new PrefSave(context);
                prefSave.saveId(userId);
                prefSave.saveLogInStatus(true);
                prefSave.saveUserType(type);

                Intent intent = new Intent(context, SignUpConfirmCode.class);
                intent.putExtra("accountType", type);
                intent.putExtra("Verfication ID", VerficationID);
                intent.putExtra("mail", email);
                intent.putExtra("pass", password);
                intent.putExtra("resend", forceResendingToken);
                context.startActivity(intent);


            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallBacks);        // OnVerificationStateChangedCallback
    }


    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userData = new UserData("", email, phone, type);
                            Log.d("user", userData.email);
                            Log.d("user", userData.phone);
                            Log.d("user", userData.type);
                            WriteData writeData = new WriteData();
                            writeData.writeNewUserInfo(userData, mAuth);
                            String userId = mAuth.getCurrentUser().getUid();
                            Log.d("user", userId);
                            PrefSave prefSave = new PrefSave(context);
                            prefSave.saveId(userId);
                            PrefGet prefGet = new PrefGet(context);
                            prefGet.getUserType();
                            Log.d("user", prefGet.getUserType());
                            prefSave.saveLogInStatus(true);
                            prefSave.saveUserType(type);
                            PrefsManager prefsManager = new PrefsManager(context);
                            prefsManager.goMainProfile(context);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("PhoneVERifier", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void createNewAccount(String email, String password, final String phone, String type, final Context context) {
        this.context = context;
        this.type = type;
        this.email = email;
        this.password = password;
        this.phone = phone;
        Activity activity = (Activity) context;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            veifyphone();
                        }
                    }
                });


    }

    public void login(String email, String password, final Context context) {
        Activity activity = (Activity) context;
        this.email = email;
        this.password = password;
        Log.d("email", email.toString());
        Log.d("pass", password.toString());
        Log.d("pass", activity.toString());
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String userId = mAuth.getCurrentUser().getUid();
                            PrefSave prefSave = new PrefSave(context);
                            prefSave.saveId(userId);
                            prefSave.saveLogInStatus(true);
                            ReadData readData = new ReadData();
                            readData.readUserInfo(userId, context);

                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token, final Context context) {
        activity = (Activity) context;
        final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("JEJE", "onVerificationCompleted:" + phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);


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
                String userId = mAuth.getCurrentUser().getUid().toString();
                PrefSave prefSave = new PrefSave(context);
                prefSave.saveId(userId);
                prefSave.saveLogInStatus(true);
                prefSave.saveUserType(type);


            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallBacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
}
