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
import com.google.firebase.database.DataSnapshot;
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

public class Auth implements FirebaseEventsListener {
    private Context context;
    private Activity activity;
    private String type;
    private String email;
    private String password;
    private String phone;
    private FirebaseAuth mAuth;
    private AuthenticationInterface.SignInWithPhoneAuthCredentialListener signInWithPhoneAuthCredentialListener;
    private AuthenticationInterface.LoginListener loginListener;
    private AuthenticationInterface.ForgetPassListener forgetPassListener;
    private AuthenticationInterface.SinUpListener sinUpListener;
    private AuthenticationInterface.PhoneVerificationListener phoneVerificationListener;

    public Auth(AuthenticationInterface.SinUpListener sinUpListener, AuthenticationInterface.PhoneVerificationListener phoneVerificationListener) {
        this.sinUpListener = sinUpListener;
        this.mAuth = FirebaseAuth.getInstance();
        this.phoneVerificationListener = phoneVerificationListener;
    }

    public Auth(AuthenticationInterface.LoginListener loginListener) {
        this.mAuth = FirebaseAuth.getInstance();
        this.loginListener = loginListener;
    }

    //K.A: overloading constructor to avoid making any conflict with existing code
    public Auth(AuthenticationInterface.SignInWithPhoneAuthCredentialListener signInWithPhoneAuthCredentialListener) {
        //no need to pass FirebaseAuth instance between classes as it's a signletone class
        this.mAuth = FirebaseAuth.getInstance();
        this.signInWithPhoneAuthCredentialListener = signInWithPhoneAuthCredentialListener;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void veifyphone() {

        activity = (Activity) context;
        final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("JEJE", "onVerificationCompleted:" + phoneAuthCredential);
                if (phoneVerificationListener != null) {
                    phoneVerificationListener.onVerificationCompleted(phoneAuthCredential);
                }
//                else {
//                    signInWithPhoneAuthCredential(phoneAuthCredential);
//                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (phoneVerificationListener != null) {
                    phoneVerificationListener.onVerificationFailed(e);
                } else {
                    Log.w("JEJE", "onVerificationFailed", e);
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Log.d("JEJE", "INVALID REQUEST");
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        Log.d("JEJE", "Too many Request");
                    }
                }

            }

            @Override
            public void onCodeSent(String VerficationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(VerficationID, forceResendingToken);

                if (phoneVerificationListener != null) {
                    phoneVerificationListener.onCodeSent(VerficationID, forceResendingToken);
                }
//                else {
//                    Log.d("JEJE", "onCodeSent:" + VerficationID);
//                    String userId = mAuth.getCurrentUser().getUid().toString();
//                    PrefSave prefSave = new PrefSave(context);
//                    prefSave.saveId(userId);
//                    prefSave.saveLogInStatus(true);
//                    prefSave.saveUserType(type);
//
//                    Intent intent = new Intent(context, SignUpConfirmCode.class);
//                    intent.putExtra("accountType", type);
//                    intent.putExtra("Verfication ID", VerficationID);
//                    intent.putExtra("mail", email);
//                    intent.putExtra("pass", password);
//                    intent.putExtra("resend", forceResendingToken);
//                    context.startActivity(intent);
//
//                }


            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallBacks);        // OnVerificationStateChangedCallback
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void forgetPass(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("g", "Email sent.");
                            if (forgetPassListener != null) {
                                forgetPassListener.onForgetPassListener(true);
                            }
                        }
                    }
                });
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        activity = (Activity) context;
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserData userData = new UserData(email, phone, type, mAuth.getCurrentUser().getUid());
                            Log.d("userMail", userData.email);
                            Log.d("userPhone", userData.phone);
                            Log.d("userType", userData.type);
                            WriteData writeData = new WriteData(Auth.this);
                            writeData.writeNewUserInfo(userData);

                            //K.A: this is an example for using one of authentication listeners
                            if (signInWithPhoneAuthCredentialListener != null) {
                                signInWithPhoneAuthCredentialListener.onSignInWithPhoneAuthCredentialCompleted(true);
                            }
//                            else {
//                                //K.A: this condition to deal with other caller methods in this class which doesn't implement the listener
//                                // to solve this use other interfaces in their activities and call this method appropriately.
//                                String userId = mAuth.getCurrentUser().getUid();
//                                Log.d("userID", userId);
//                                PrefSave prefSave = new PrefSave(context);
//                                prefSave.saveId(userId);
//                                // K.A: why do you get user type and then set it again?
//                                prefSave.saveLogInStatus(true);
//                                prefSave.saveUserType(type);
//                                PrefsManager prefsManager = new PrefsManager(context);
//                                prefsManager.goMainProfile(context);
//                            }
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("PhoneVERifier", "signInWithCredential:failure", task.getException());
                            if (signInWithPhoneAuthCredentialListener != null) {
                                signInWithPhoneAuthCredentialListener.onSignInWithPhoneAuthCredentialCompleted(false);
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
                            if (task.getException() != null) {
                                Toast.makeText(context, "Authentication failed." + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Log.e("FBAuthenticationError", task.getException().toString());
                                if (sinUpListener != null) {
                                    sinUpListener.onSignUpWithEmailAndPasswordComplete(false);
                                }
                            }
                        } else {
                            if (sinUpListener != null) {
                                sinUpListener.onSignUpWithEmailAndPasswordComplete(true);
                            }
                        }
                    }
                });


    }

    public void login(String email, String password, final Context context) {
        Activity activity = (Activity) context;
        this.email = email;
        this.password = password;
        Log.d("email", email);
        Log.d("pass", password);
        Log.d("acitivity", activity.toString());
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if (loginListener != null) {
                                loginListener.onUserLoginComplete(true);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            if (loginListener != null) {
                                loginListener.onUserLoginComplete(false);
                            }

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
                //signInWithPhoneAuthCredential(phoneAuthCredential);


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

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        if (writeSuccessful) {
            Log.v("Aut", "Adding new user done successfully");
        }
    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }

}
