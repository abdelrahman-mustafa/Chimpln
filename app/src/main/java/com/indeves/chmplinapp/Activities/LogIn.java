package com.indeves.chmplinapp.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.indeves.chmplinapp.API.Auth;
import com.indeves.chmplinapp.API.AuthenticationInterface;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.PrefsManager.PrefSave;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CheckError;
import com.indeves.chmplinapp.Utility.Toasts;
import com.wang.avi.AVLoadingIndicatorView;


public class LogIn extends AppCompatActivity implements AuthenticationInterface.LoginListener, FirebaseEventsListener {
    AlertDialog dialog0;
    EditText mail, pass;
    Button signUp, login;
    TextView forgPass;
    String TAG = "This Activiy";
    com.wang.avi.AVLoadingIndicatorView avi;
    Toasts toasts;
    FirebaseDatabase k;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        final ImageView splash = (ImageView) findViewById(R.id.splash);
        //splash.startAnimation(anim);
        k = FirebaseDatabase.getInstance();
        forgPass = findViewById(R.id.logIn_Button_forgetPass);
        forgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.popup_select_email_forget_pass, null);


                AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                builder.setView(customView);

                dialog0 = builder.create();
                dialog0.show();

                final EditText email = customView.findViewById(R.id.get_email);
                Button button = customView.findViewById(R.id.select);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Auth auth = new Auth(LogIn.this);
                        auth.forgetPass(email.getText().toString());
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LogIn.this, "Done, Check your mail", Toast.LENGTH_SHORT).show();
                                            dialog0.dismiss();
                                        } else {
                                            Toast.makeText(LogIn.this, "Something went wrong, try later", Toast.LENGTH_SHORT).show();
                                            dialog0.dismiss();
                                        }
                                    }
                                });
                    }
                });
            }
        });
        toasts = new Toasts(LogIn.this);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(splash, "translationY", -1000, 0),
                ObjectAnimator.ofFloat(splash, "alpha", 0, 1)
        );
        animatorSet.setDuration(2000);
        animatorSet.start();

        avi = (AVLoadingIndicatorView) findViewById(R.id.indicator);

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
        stopAnim();

        login = findViewById(R.id.LogIn_button_SignIn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mail.getText().toString().trim();
                String passw = pass.getText().toString().trim();

                CheckError checkError = new CheckError();
                if (!checkError.checkEmpty(email, " ", passw, " ")) {
                    // toast error
                    toasts.completeData();
                    stopAnim();
                } else {
                    Auth auth = new Auth(LogIn.this);
                    auth.login(email, passw, LogIn.this);
                    startAnim();
                }


            }
        });

    }

    @Override
    public void onUserLoginComplete(boolean loginSuccessful) {
        if (loginSuccessful) {
            Log.v("LoggedInUID", mAuth.getCurrentUser().getUid());
            ReadData readData = new ReadData(this);
            readData.readUserInfo(mAuth.getCurrentUser().getUid());
        } else {
            toasts.wrongMe();
            stopAnim();
        }
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
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        stopAnim();
        if (dataSnapshot == null) {
            toasts.wrongMe();
        } else {
            UserData user = dataSnapshot.getValue(UserData.class);
            if (user != null && user.type != null) {
                Log.i("MyUserData", user.toString());
                PrefSave prefSave = new PrefSave(LogIn.this);
                prefSave.saveUserType(user.type.trim());
                prefSave.saveId(mAuth.getCurrentUser().getUid());
                prefSave.saveLogInStatus(true);
                switch (user.type) {
                    case "stu":
                        startActivity(new Intent(LogIn.this, StuLandingPage.class));
                        LogIn.this.finish();
                        break;
                    case "user":
                        startActivity(new Intent(LogIn.this, UserProfileMain.class));
                        LogIn.this.finish();
                        break;
                    case "pro":
                        startActivity(new Intent(LogIn.this, ProLandingPage.class));
                        LogIn.this.finish();
                        break;
                }
            } else {
                toasts.wrongMe();
            }
        }

    }

}

