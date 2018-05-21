package com.indeves.chmplinapp.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.indeves.chmplinapp.PrefsManager.PrefGet;
import com.indeves.chmplinapp.R;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.splash);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(imageView, "translationY", -1000, 0),
                ObjectAnimator.ofFloat(imageView, "alpha", 0, 1)
        );
        animatorSet.setDuration(2000);
        /*animatorSet.start();*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PrefGet prefGet = new PrefGet(SplashActivity.this);
                if (prefGet.getLogInStatus() && FirebaseAuth.getInstance().getCurrentUser() != null && prefGet.getUserType() != null) {
                    switch (prefGet.getUserType()) {
                        case "stu":
                            startActivity(new Intent(SplashActivity.this, StuLandingPage.class));
                            SplashActivity.this.finish();
                            break;
                        case "user":
                            startActivity(new Intent(SplashActivity.this, UserProfileMain.class));
                            SplashActivity.this.finish();
                            break;
                        case "pro":
                            startActivity(new Intent(SplashActivity.this, ProLandingPage.class));
                            SplashActivity.this.finish();
                            break;
                    }

                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }

            }
        }, 2000);

    }
}

